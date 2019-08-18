package com.base.modules.business.system.storagea.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 入库A表存储
 * 
 * @author huanw
 * @email 
 * @date 2019-08-18 20:43:16
 */
@TableName("biz_storagea")
public class StorageaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 导入日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date applyDate;
	/**
	 * 导入名称
	 */
	private String applyName;
	/**
	 * 输出编码
	 */
	private String outCode;
	/**
	 * 创建日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

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
	 * 设置：导入日期
	 */
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	/**
	 * 获取：导入日期
	 */
	public Date getApplyDate() {
		return applyDate;
	}
	/**
	 * 设置：导入名称
	 */
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	/**
	 * 获取：导入名称
	 */
	public String getApplyName() {
		return applyName;
	}
	/**
	 * 设置：输出编码
	 */
	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}
	/**
	 * 获取：输出编码
	 */
	public String getOutCode() {
		return outCode;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}
}
