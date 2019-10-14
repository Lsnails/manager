package com.base.modules.customizesys.upload.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.customizesys.upload.entity.UeditorImage;
import com.base.modules.customizesys.upload.entity.UploadFilesEntity;
import com.base.modules.customizesys.upload.utils.OssUtils;
import com.base.modules.customizesys.upload.utils.UploadUtils;
import com.base.utils.UUIDUtils;

/**
 * @ClassName:  UeditorController   
 * @Description:Ueditor上传文件实现   
 * @author:  huanw
 * @date:   2019年1月10日 上午10:26:40
 */
@Controller
@RequestMapping("/upload")
public class OssUeditorController {
	
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
		try {
			logger.debug("file------->:" + file);
			if (file != null && file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())) {
				String uuid ="";// 获取到唯一的名称
				UploadFilesEntity uploadVo = new UploadFilesEntity();
				String OriginalFileName = file.getOriginalFilename().trim();
				uploadVo.setOldName(OriginalFileName);
				uploadVo.setFileSize(String.valueOf(file.getSize()));
				logger.info("接收到上传数据，原文件名OriginalFileName:" + OriginalFileName);
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
					if(aliossEnabled) {//阿里oss上传true
						//走阿里oss上传
						OssUtils instance = OssUtils.getInstance();
						OSSClient ossClient = instance.getOssClient(endpoint, accessKeyId, accessKeySecret);
//						instance.createBucket(ossClient, bucketName);
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
					image.setUrl(uploadVo.getViewFilePath());
					logger.info("文件上传成功,上传服务器路径为 ："+ uploadVo.getFileServerPath());
				}
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