package com.base.modules.customizesys.upload.controller;

import java.io.File;
import java.io.InputStream;
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

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.base.common.utils.R;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.customizesys.upload.entity.UploadFilesEntity;
import com.base.modules.customizesys.upload.utils.OssUtils;
import com.base.modules.customizesys.upload.utils.UploadUtils;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * oss上传文件
 * @ClassName:  OssUploadController   
 * @Description:oss上传文件
 * @author:  huanw
 * @date:   2019年4月17日 下午3:11:15
 */
@Controller
@RequestMapping("/upload")
@Api("上传文件")
public class OssUploadController {
	/**是否启用阿里oss服务  true表示启用   false 默认上传到本地*/
	@Value("${oss.alioss-enabled}")
	private boolean aliossEnabled;
	
	/** 可上传文件类型   */
	@Value("${oss.uploadFileType}")
	private String uploadFileTypes;
	
	/** alioss-enabled=false 上传文件本地目录，后面不带斜杠  */
	@Value("${oss.localdir}")
	private String localdir;
	
	/** aliossEnabled= false时候上传到项目的c:/upload/tmp/files 文件下  */
	@Value("${oss.files}")
	private String files;
	
	
	/** endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。 例如:http://oss-cn-beijing.aliyuncs.com  */
	@Value("${ossConfig.endpoint}")
	private String endpoint;
	
	/** accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看   */
	@Value("${ossConfig.accessKeyId}")
	private String accessKeyId ;
	
	/** accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看   */
	@Value("${ossConfig.accessKeySecret}")
	private String accessKeySecret ;
	
	
	/** 适用于文件url ，  endpointNotWithHttp值去除endpoint的http://   例如endpoint=http://oss-cn-beijing.aliyuncs.com  那么endpointNotWithHttp=oss-cn-beijing.aliyuncs.com   */
	@Value("${ossConfig.endpointNotWithHttp}")
	private String endpointNotWithHttp ;
	
	/** 存储空间名称  */
	@Value("${ossConfig.bucketName}")
	private String bucketName;
	
	/** 相当于存储空间下的文件夹  */
	@Value("${ossConfig.objectName}")
	private String objectName;
	
	
	/** nginx代理ip */
	@Value("${nginx.serverip}")
	private String nginxServerip;
	
	/** nginx代理端口 */
	@Value("${nginx.serverport}")
	private String nginxServerport;
 
	
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
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		logger.debug("进入文件ossupLoad方法");
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
					if(aliossEnabled) {//阿里oss上传true
						//走阿里oss上传
						OssUtils instance = OssUtils.getInstance();
						OSSClient ossClient = instance.getOssClient(endpoint, accessKeyId, accessKeySecret);
						instance.createBucket(ossClient, bucketName);
						String  ossFilePath = objectName+"/"+newFileName;//上传到oss的文件全路径
						InputStream inputStream = file.getInputStream();
						String ossUrl = instance.putObjectInputStream(ossClient, bucketName, ossFilePath, inputStream, endpointNotWithHttp);
						uploadVo.setFileServerPath("");
						uploadVo.setViewFilePathAndHttpOss(ossUrl);//访问文件路径 http:///
					}else {
						//普通上传
						UploadUtils.makeDirs(localdir+files);	// 例如 localTempdir :c:/upload/tmp   dirPath：/files
						//"localTempdir+dirPath" 为c:/upload/tmp/files ,本地加上files，目的是nginx转发配置一致
						FileUtils.copyInputStreamToFile(file.getInputStream(), new File(localdir+files +"/"+ newFileName));// 复制临时文件到指定目录下
						uploadVo.setFileServerPath(localdir+files +"/"+ newFileName);
						uploadVo.setViewFilePathAndHttp(nginxServerip+":"+nginxServerport+files+"/" + newFileName);//访问服务器的路径
					}
					logger.info("文件上传成功,上传服务器路径为 ："+ uploadVo.getFileServerPath());
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
		Object json = JSONObject.toJSON(r);
		return json.toString();
	}
}