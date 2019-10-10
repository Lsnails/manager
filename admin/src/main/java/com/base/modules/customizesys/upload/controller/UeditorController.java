package com.base.modules.customizesys.upload.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.customizesys.upload.entity.UeditorImage;
import com.base.modules.customizesys.upload.entity.UploadFilesEntity;
import com.base.modules.customizesys.upload.utils.UploadUtils;
import com.base.utils.UUIDUtils;

/**
 * @ClassName:  UeditorController   
 * @Description:Ueditor上传文件实现   
 * @author:  huanw
 * @date:   2019年1月10日 上午10:26:40
 */
@Controller
@RequestMapping("/oldupload")
public class UeditorController {
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
	 * @Title: config   
	 * @Description: 配合Ueditor获取config.json数据
	 * @author: huanw
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: String
	 * @date:   2019年1月10日 上午10:29:22       
	 * @throws
	 */
	@RequestMapping("/config")
	@ResponseBody
    public String config(HttpServletRequest request,HttpServletResponse response){
		//文件读取使用于eclipse和可执行jar包
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("statics/plugins/ueditor/1_4_3_3-utf8-jsp/jsp/config.json");
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = inputStream.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}
			return result.toString("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
            return "error";
		}
    }
//	使用与eclipse中
//	@RequestMapping("/config")
//	@ResponseBody
//    public String config(HttpServletRequest request,HttpServletResponse response){
//		try {
//            response.setContentType("application/json;charset=utf-8");
//            Resource resource = new ClassPathResource("config.json");
////            Resource resource = new ClassPathResource("statics/plugins/ueditor/1_4_3_3-utf8-jsp/jsp/config.json");
//            
//            File file = resource.getFile();
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null){
//                stringBuilder.append(line);
//            }
//            return stringBuilder.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//            return "error";
//        }
//    }
	
	/**
	 * Ueditor ajax 上传文件
	 * @Title: upLoad   
	 * @Description:  Ueditor ajax 上传文件
	 * @author: huanw
	 * @param: @param request
	 * @param: @param response
	 * @param: @param type
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/uploadImg")
	@ResponseBody
	public void upLoad(
			HttpServletRequest request,HttpServletResponse response,String type) {
		UeditorImage image = new UeditorImage();
		logger.debug("进入文件upLoad方法");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<UploadFilesEntity> uploadFileInfos = new ArrayList<UploadFilesEntity>();
		try {
			logger.debug("file------->:" + file);
			if (file != null && file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())) {
				String uuid ="";// 获取到唯一的名称
				UploadFilesEntity uploadVo = new UploadFilesEntity();
				String OriginalFileName = file.getOriginalFilename().trim();
				uploadVo.setOldName(OriginalFileName);
				uploadVo.setFileSize(String.valueOf(file.getSize()));
				logger.debug("原文件名OriginalFileName:" + OriginalFileName);
				// 任意文件上传 当文件是符合要求的文件，则上传
				if (UploadUtils.checkFileType(OriginalFileName, uploadFileTypes)) {
					String newFileName="";//新文件名称
					if (StringUtils.isNotEmpty(OriginalFileName)) {
						String prefix = OriginalFileName.substring(OriginalFileName.indexOf(".") + 1);
						uploadVo.setFileType(prefix);
						uuid =UUIDUtils.getRandomUUID(); // 获取到唯一的名称
						newFileName = uuid + "." + prefix; // 上传服务器名
						uploadVo.setNewFileName(newFileName);
						//用新的字符编码生成字符串  
						image.setOriginal(new String( OriginalFileName.getBytes("UTF-8"), "UTF-8"));
						logger.info("接收到上传数据，文件名称为 ：" + OriginalFileName);
						image.setTitle(new String( OriginalFileName.getBytes("UTF-8"), "UTF-8"));
					}
					
					//访问服务器的路径
					String viewFilePath=nginxServerip+":"+nginxServerport+dirPath+"/" + newFileName;
					uploadVo.setViewFilePathAndHttp(viewFilePath);
					image.setUrlAndHttp(viewFilePath);
					try {
						if(UploadUtils.makeDirs(localTempdir)){
							if(!ftpEnabled) {//无单独的图片服务器，走本地上传，需要创建files文件
								UploadUtils.makeDirs(localTempdir+dirPath);	// 例如 localTempdir :c:/upload/tmp   dirPath：/files
								//"localTempdir+dirPath" 为c:/upload/tmp/files ,本地加上files，目的是nginx转发配置一致
								String filePath=localTempdir+dirPath +"/"+ newFileName;
								uploadVo.setFileServerPath(filePath);
								logger.info("文件上传成功,上传路径为 ："+filePath);
								FileUtils.copyInputStreamToFile(file.getInputStream(), new File(localTempdir+dirPath +"/"+ newFileName));// 复制临时文件到指定目录下
							}else {
								FileUtils.copyInputStreamToFile(file.getInputStream(), new File(localTempdir +"/"+ newFileName));// 复制临时文件到指定目录下
							}
							if(ftpEnabled) {//ftpEnabled为true，走ftp上传
								String dirFile = remotePath + dirPath+"/";//File.separator
								uploadVo.setFileServerPath(dirFile+newFileName);//服务器文件路径
								UploadUtils.ftpFile(localTempdir +"/"+ newFileName, dirFile,ip,port,password,username);// 上传到服务器
								//发完ftp，删除本地文件
								UploadUtils.deleteFile(localTempdir +"/"+ newFileName);
							}
						}
					} catch (IOException e) {
						uploadVo.setReturnStatus(Integer.valueOf(1));
						e.printStackTrace();
					}
				}
//				uploadFileInfos.add(uploadVo);	
//				uploadVo.setFileId(uuid);
//				uploadVo.setLastmodifytime(new Date());
//				uploadVo.setFileStatus(Constant.FILE_STATUS_0);
//				uploadFilesService.insert(uploadVo);
				image.setState("SUCCESS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件上传失败，失败原因:" + e.getMessage());
	        image.setState("FAIL");
		}
	 	try {
			String jsonStrShow = JSONObject.toJSONString(image);
			response.setContentType("text/html;charset=UTF-8");  
			PrintWriter out = response.getWriter();  
			out.print(jsonStrShow);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}