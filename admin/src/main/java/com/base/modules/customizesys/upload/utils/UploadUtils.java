package com.base.modules.customizesys.upload.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * 文件上传工具类
 * @ClassName:  UploadUtils   
 * @Description:文件上传工具类
 * @author:  huanw
 * @date:   2018年12月27日 上午10:26:16
 */
public class UploadUtils {
	private static final Logger logger = LoggerFactory.getLogger(UploadUtils.class);
	/**
	 * 
	 * @Description: 从ueditor的返回json中取出文件的文件名，然后ftp
	 * @param @param json
	 * @param @param rootPath
	 * @return void
	 * @throws
	 */
//	public static void uploadImage(String json, String rootPath,String remotePath,String ip,String port,String password,String username) {
//		logger.info("进入FTP方法,json=" + json);
//		JSONObject jsonObject =null;
//		try {
//			jsonObject = new JSONObject(json);
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		String state = "";
//		String fileName = "";
//		File rootpath = new File(rootPath);
//		String uploadpath = rootpath.getParent();
//		if (StringUtils.isEmpty(remotePath)) {
//			logger.info("缺少参数");
//			return;
//		}
//		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
//		String nowday = df1.format(new Date());
//		String remotedir = remotePath + "/image/" + nowday;
//		try {
//			jsonObject.get("state");
//			state = (String) jsonObject.get("state");
//			if ("SUCCESS".equals(state)) {
//				fileName = (String) jsonObject.get("title");
//				String sourceFile = uploadpath + File.separator + "upload"
//						+ File.separator + "image" + File.separator + nowday
//						+ File.separator + fileName;
//				File file = new File(sourceFile);
//				if (file.exists()) {
//					ftpFile(sourceFile, remotedir,ip,port,password,username);
//				}
//			}
//
//		} catch (Exception e) {
//		}
//	}

	/**
	 * @Description: FTP方法,参数为源文件（路径+文件名），目标目录
	 * @param @param sourceFile 源文件
	 * @param @param remotePath 服务器路径
	 * @return void
	 * @throws
	 */
	public synchronized static void ftpFile(String sourceFile, String remotePath,String ip,String port,String password,String username) {
		logger.info("进入FTP上传文件的方法");
		logger.info("源文件为" + sourceFile + "  目标目录为" + remotePath);
		if (StringUtils.isEmpty(ip) 
				|| StringUtils.isEmpty(port)
				|| StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(username)) {
			logger.info(ip + port + username + password);
			logger.error("缺少参数，不能进行FTP上传");
			return;
		}
		ChannelSftp sftp = null;
		sftp = SftpUtil.connect(ip, Integer.parseInt(port), username, password);
		if (sftp == null) {
			logger.error("连接FTP失败");
			return;
		}
		try {
			File file = new File(sourceFile);
			if (file.exists()) {
				//上传文件到ftp
				SftpUtil.sftpUploadFile(remotePath, sourceFile, sftp);
			}
		} catch (SftpException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			SftpUtil.disconnect();

		}
	}
	
	/**
	 * 检查文件类型是否符合规范
	 * @Title: checkFileType   
	 * @Description: 检查文件类型是否符合规范
	 * @author: huanw
	 * @param: @param fileName
	 * @param: @param uploadFileTypes
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: boolean
	 * @date:   2019年1月16日 下午1:32:20       
	 * @throws
	 */
	public static boolean checkFileType(String fileName, String uploadFileTypes) throws Exception {
		boolean flag = false;
		String[] uploadFileType = uploadFileTypes.split("[|]");
		// 检查上传的文件类型是否符合要求的文件
		for (int i = 0; i < uploadFileType.length; i++) {
			if (fileName.toLowerCase().contains(uploadFileType[i])) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 
	 * 描述：目录不存时，创建目录
	 * @version
	 */
	public static boolean makeDirs(String filePath) {
		boolean flag = false;
		String folderName = filePath;
		if (folderName == null || folderName.isEmpty()) {
			return false;
		}
		File folder = new File(folderName);
		if (folder.exists() && folder.isDirectory()) {
			flag = true;
		} else {
			boolean mkdirs = folder.mkdirs();
			if (mkdirs) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 删除服务器本地文件
	 * @Title: deleteFile   
	 * @Description: 删除服务器本地文件
	 * @author: huanw
	 * @param: @param filePath      
	 * @return: void
	 * @date:   2019年1月29日 下午3:09:06       
	 * @throws
	 */
//	public  static boolean deleteFile(String filePath) {
//		File dirFile = new File(filePath);
//		//如果dir对应的文件不存在，或者不是一个目录，则退出
//		if (!dirFile.exists()) {
//			return false;
//		}
//		boolean flag = true;
//		//删除文件夹下的所有文件(包括子目录)
//		if (dirFile.isFile()) {
//			flag = deleteFile(dirFile.getAbsolutePath());
//			System.out.println();
//		}
//		return flag;
//	}
	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	/**
	 *  根据路径删除指定的目录或文件，无论存在与否
	 *@param sPath  要删除的目录或文件
	 *@return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	private static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} //删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		//删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 将base64的数据写到指定文件中
	 * @Title: base64Decode   
	 * @Description: 将base64的数据写到指定文件中
	 * @author: huanw
	 * @param: @param imgData
	 * @param: @param path
	 * @param: @return      
	 * @return: boolean
	 * @date:   2019年4月15日 下午3:13:14       
	 * @throws
	 */
	public boolean base64Decode(String imgData,String path) {
		boolean flag=false;
		// 将dataURL开头的非base64字符删除
		String base64 = imgData.substring(imgData.indexOf(",") + 1);
//        FileOutputStream write = new FileOutputStream(new File(localTempdir+dirPath+"/"+newFileName));
		try {
			FileOutputStream write = new FileOutputStream(new File(path));
			byte[] decoderBytes = Base64.getDecoder().decode(base64);
	        write.write(decoderBytes);
	        write.close();
	        flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return flag;
	}
	
	public byte[] base64DecodeToByte(String imgData) {
	   byte[] decoderBytes = null;
	   // 将dataURL开头的非base64字符删除
	   String base64 = imgData.substring(imgData.indexOf(",") + 1);
	   decoderBytes = Base64.getDecoder().decode(base64);
       return decoderBytes;
	}
	
	public static void main(String args[]) {
		File path = new File("d:/data/backup");
		String parent = path.getParent();
		logger.info(parent);
	}

}
