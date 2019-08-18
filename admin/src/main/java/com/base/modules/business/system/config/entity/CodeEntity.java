package com.base.modules.business.system.config.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 日期对应code生成表
 * 
 * @author huanw
 * @email 
 * @date 2019-08-18 19:14:31
 */
@TableName("biz_code")
public class CodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;
	/**
	 * 对应编号
	 */
	private int code;
	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;
	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateDate;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：日期
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * 获取：日期
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * 设置：对应编号
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * 获取：对应编号
	 */
	public int getCode() {
		return code;
	}
	/**
	 * 设置：
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}
