package com.base.modules.customizesys.upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.base.common.utils.R;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.customizesys.upload.entity.UploadFilesEntity;
import com.base.modules.customizesys.upload.utils.UploadUtils;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 上传文件
 * @ClassName:  UploadController   
 * @Description:上传文件
 * @author:  huanw
 * @date:   2019年1月16日 上午9:59:35
 */
@Controller
@RequestMapping("/oldupload")
@Api("上传文件")
public class UploadController {
	/**远程服务器上传服务器路径*/
	@Value("${upload.remotePath}")
	private String remotePath;
	
	/** ftp地址  */
	@Value("${upload.ip}")
	private String ip;
	
	/** ftp端口   */
	@Value("${upload.port}")
	private String port;
	
	/** ftp   */
	@Value("${upload.password}")
	private String password ;
	
	/** ftp用户名   */
	@Value("${upload.username}")
	private String username;
	
	/** 可上传文件类型   */
	@Value("${upload.uploadFileType}")
	private String uploadFileTypes;
	
	/** nginx转发文件，下的子目录   */
	@Value("${upload.dirPath}")
	private String dirPath;
	
	/** 本地临时文件 */
	@Value("${upload.dir}")
	private String localTempdir;
	
	/** nginx代理ip */
	@Value("${nginx.serverip}")
	private String nginxServerip;
	
	/** nginx代理端口 */
	@Value("${nginx.serverport}")
	private String nginxServerport;
	
	/** 是否启用ftp上传，
	 * 1.如果没有单独的图片服务器，设置为false，即不走ftp上传，需要配置“本地临时文件dir，并且使用nginx转发 
	 * 2.有单独的图片服务器，需要配置ftp相关信息，并且做nginx转发”
	 * */
	@Value("${upload.ftp-enabled}")
	private boolean ftpEnabled;
 
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 上传文件
	 * @Title: upLoad   
	 * @Description: 上传文件
	 * @author: huanw
	 * @param: @param request
	 * @param: @param response
	 * @param: @param type      
	 * @return: void
	 * @date:   2019年1月16日 下午1:15:40       
	 * @throws
	 */
	@RequestMapping("/uploadFile")
	@ApiOperation("上传方法")
	@ResponseBody
	public R uploadFile(@RequestParam("file") MultipartFile file) {
		logger.debug("进入文件upLoad方法");
		//存储文件相关信息vo
		UploadFilesEntity uploadVo = new UploadFilesEntity();
		String uuid = ""; // 获取到唯一的名称
		try {
			logger.debug("file------->:" + file);
			if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
				//获取原文件的名称
				String OriginalFileName = file.getOriginalFilename().trim();
				logger.debug("原文件名OriginalFileName:" + OriginalFileName);
				// 任意文件上传 当文件是符合要求的文件，则上传
				if (UploadUtils.checkFileType(OriginalFileName, uploadFileTypes)) {
					String newFileName="";//新文件名称
					//获取文件后缀名
					String prefix = OriginalFileName.substring(OriginalFileName.indexOf(".") + 1);
					uuid = UUIDUtils.getRandomUUID(); // 获取到唯一的名称
					newFileName = uuid + "." + prefix; // 上传到服务器的文件名称
					uploadVo.setOldName(OriginalFileName);
//					new String( OriginalFileName.getBytes("UTF-8"), "UTF-8") 用新的字符编码生成字符串  
					uploadVo.setFileSize(String.valueOf(file.getSize()));
					uploadVo.setFileType(prefix);
					uploadVo.setNewFileName(newFileName);
					logger.info("接收到上传数据，文件名称为 ：" + OriginalFileName);
					//文件上传的路径
					String serverDirFile = "";//File.separator
					if(!ftpEnabled) {//无单独的图片服务器，走本地上传
						serverDirFile = localTempdir + dirPath+"/";
					}else {
						serverDirFile = remotePath + dirPath+"/";
					}
					uploadVo.setFileServerPath(serverDirFile + newFileName);
					uploadVo.setViewFilePathAndHttp(nginxServerip+":"+nginxServerport+dirPath+"/" + newFileName);//访问服务器的路径
					logger.info("文件上传成功,上传服务器路径为 ："+ serverDirFile + newFileName);
					try {
						if(UploadUtils.makeDirs(localTempdir)){
							if(!ftpEnabled) {//无单独的图片服务器，走本地上传，需要创建files文件
								UploadUtils.makeDirs(localTempdir+dirPath);	// 例如 localTempdir :c:/upload/tmp   dirPath：/files
								//"localTempdir+dirPath" 为c:/upload/tmp/files ,本地加上files，目的是nginx转发配置一致
								FileUtils.copyInputStreamToFile(file.getInputStream(), new File(localTempdir+dirPath +"/"+ newFileName));// 复制临时文件到指定目录下
							}else {
								FileUtils.copyInputStreamToFile(file.getInputStream(), new File(localTempdir +"/"+ newFileName));// 复制临时文件到指定目录下
							}
							if(ftpEnabled) {//ftpEnabled为true，走ftp上传
								UploadUtils.ftpFile(localTempdir +"/"+ newFileName, serverDirFile,ip,port,password,username);// 上传到服务器
								//发完ftp，删除本地文件
								UploadUtils.deleteFile(localTempdir +"/"+ newFileName);
							}
						}
					} catch (IOException e) {
						uploadVo.setReturnStatus(Integer.valueOf(1));
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			uploadVo.setReturnStatus(Integer.valueOf(1));
			logger.error("文件上传失败，失败原因:" + e.getMessage());
		}
		uploadVo.setFileId(uuid);
		uploadVo.setLastmodifytime(new Date());
		uploadVo.setFileStatus(Constant.FILE_STATUS_0);
//		uploadFilesService.insert(uploadVo);
//		Gson gson = new Gson();
//		return gson.toJson(uploadVo);
//		测试数据
//		UploadVo uploadVo = new UploadVo();
//		uploadVo.setFileServerPath("文件服务器路径:/usr/file");
//		uploadVo.setFileSize(10);
//		uploadVo.setIcon("图标目前不用");
//		uploadVo.setNewFileName("新文件名称：124847474774744884747.jpg");
//		uploadVo.setOldName("文件原名称：采集图片.jpg");
//		uploadVo.setReturnStatus(1);
//		uploadVo.setFileType("文件类型");
//		uploadVo.setViewFilePath("127.0.0.1:8080/aa/dd.jpg");
		R r=new R();
		if(uploadVo.getReturnStatus()==0) {
			r = R.ok().put("data", uploadVo);
		}else {
			r=  R.error("上传文件失败");
		}
		return r;
	}
}