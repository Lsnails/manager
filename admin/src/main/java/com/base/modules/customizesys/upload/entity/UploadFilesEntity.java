package com.base.modules.customizesys.upload.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 上传文件管理实体
 * @ClassName:  UploadVo   
 * @Description:上传文件管理实体   
 * @author:  huanw
 * @date:   2019年1月23日 上午11:01:04
 */
@TableName("biz_upload_files")
public class UploadFilesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String fileId;
	/**
	 * 返回状态 0 成功 ，1 失败
	 */
	//注明非数据库字段属性
	@TableField(exist = false)
	private Integer returnStatus = 0;
	/**
	 * 原文件名称
	 */
	private String oldName;
	/**
	 * 文件大小
	 */
	private String fileSize;  
	/**
	 * 文件类型
	 */
	private String fileType; 
	/**
	 * 文件服务器路径
	 */
	private String fileServerPath;
	/**
	 * 新文件名称
	 */
	private String newFileName;
	/**
	 * nginx转发后的文件 http://127.0.0.1:8080/jdjdjd.jpg 路径
	 */
	private String viewFilePath;
	/**
	 * 无http ip加端口号的文件路径
	 */
	private String wuHttpFilePath;
	/**
	 * 状态（0：新增 1：使用 2：临时）
	 */
    private String fileStatus;
    /**
	 * 最近维护时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastmodifytime;
	/**
	 * 文件相关内容：例如：id为XXX的新闻引用此图片
	 */
    private String contentId;
	
	public Integer getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(Integer returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	/**
	 * 设置：
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取：
	 */
	public String getFileId() {
		return fileId;
	}
	/**
	 * 设置：原文件名称
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	/**
	 * 获取：原文件名称
	 */
	public String getOldName() {
		return oldName;
	}
	/**
	 * 设置：文件大小
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * 获取：文件大小
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * 设置：文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * 获取：文件类型
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * 设置：文件服务器路径
	 */
	public void setFileServerPath(String fileServerPath) {
		this.fileServerPath = fileServerPath;
	}
	/**
	 * 获取：文件服务器路径
	 */
	public String getFileServerPath() {
		return fileServerPath;
	}
	/**
	 * 设置：新文件名称
	 */
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	/**
	 * 获取：新文件名称
	 */
	public String getNewFileName() {
		return newFileName;
	}
	/**
	 * 设置：图片访问路径
	 */
//	public void setViewFilePath(String viewFilePath) {
//		this.viewFilePath = viewFilePath;
//	}
	/**
	 * 设置：图片访问路径前面加http://
	 */
	public void setViewFilePathAndHttp(String viewFilePath) {
		this.viewFilePath = "http://"+viewFilePath;;
	}
	/**
	 * 设置：适用于oss 图片访问路径前面
	 */
	public void setViewFilePathAndHttpOss(String viewFilePath) {
		this.viewFilePath ="https://"+ viewFilePath;;
	}
	/**
	 * 获取：图片访问路径
	 */
	public String getViewFilePath() {
		return viewFilePath;
	}
	/**
	 * 设置：状态（状态（0：新增 1：使用 2：临时）
	 */
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	/**
	 * 获取：状态（状态（0：新增 1：使用 2：临时）
	 */
	public String getFileStatus() {
		return fileStatus;
	}
	/**
	 * 设置：关联模块内容id
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	/**
	 * 获取：关联模块内容id
	 */
	public String getContentId() {
		return contentId;
	}
	/**
	 * 设置：最新修改时间
	 */
	public void setLastmodifytime(Date lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}
	/**
	 * 获取：最新修改时间
	 */
	public Date getLastmodifytime() {
		return lastmodifytime;
	}

	public String getWuHttpFilePath() {
		return wuHttpFilePath;
	}

	public void setWuHttpFilePath(String wuHttpFilePath) {
		this.wuHttpFilePath = wuHttpFilePath;
	}
	
	
}
