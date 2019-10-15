package com.base.modules.business.system.activityinfo.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动管理表
 * 
 * @author huanw
 * @email 
 * @date 2019-10-15 15:48:42
 */
@TableName("biz_activityinfo")
public class ActivityinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String activityinfoId;
	/**
	 * 活动名称
	 */
	private String name;
	/**
	 * 是否启用状态（0：关闭 1：启用）
	 */
	private String status;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private String remark2;
	/**
	 * 
	 */
	private String remark3;
	/**
	 * 
	 */
	private String remark4;
	/**
	 * 
	 */
	private String remark5;
	/**
	 * 创建人
	 */
	private String createby;
	/**
	 * 创建时间
	 */
	private Date createtime;
	/**
	 * 最新修改人
	 */
	private String lastmodifyby;
	/**
	 * 最新修改时间
	 */
	private Date lastmodifytime;

	/**
	 * 设置：
	 */
	public void setActivityinfoId(String activityinfoId) {
		this.activityinfoId = activityinfoId;
	}
	/**
	 * 获取：
	 */
	public String getActivityinfoId() {
		return activityinfoId;
	}
	/**
	 * 设置：活动名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：活动名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：是否启用状态（0：关闭 1：启用）
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：是否启用状态（0：关闭 1：启用）
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	/**
	 * 获取：
	 */
	public String getRemark2() {
		return remark2;
	}
	/**
	 * 设置：
	 */
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	/**
	 * 获取：
	 */
	public String getRemark3() {
		return remark3;
	}
	/**
	 * 设置：
	 */
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
	/**
	 * 获取：
	 */
	public String getRemark4() {
		return remark4;
	}
	/**
	 * 设置：
	 */
	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}
	/**
	 * 获取：
	 */
	public String getRemark5() {
		return remark5;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateby() {
		return createby;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 设置：最新修改人
	 */
	public void setLastmodifyby(String lastmodifyby) {
		this.lastmodifyby = lastmodifyby;
	}
	/**
	 * 获取：最新修改人
	 */
	public String getLastmodifyby() {
		return lastmodifyby;
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
}
