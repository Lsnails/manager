package com.base.modules.business.system.wxuser.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author huanw
 * @email 
 * @date 2019-10-15 10:12:07
 */
@TableName("biz_wx_user")
public class WxUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * wx openId唯一标识
	 */
	private String openId;
	/**
	 * 活动id
	 */
	private String activityId;
	/**
	 * 网点id
	 */
	private String networkId;
	/**
	 * 网点名称
	 */
	private String networkName;
	/**
	 * 是否核销
	 */
	private Integer state;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 用户唯一编码,自动生成
	 */
	private String userCode;
	/**
	 * 
	 */
	private Date createDate;
	/**
	 * 
	 */
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
	 * 设置：wx openId唯一标识
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * 获取：wx openId唯一标识
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * 设置：活动id
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	/**
	 * 获取：活动id
	 */
	public String getActivityId() {
		return activityId;
	}
	/**
	 * 设置：网点id
	 */
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	/**
	 * 获取：网点id
	 */
	public String getNetworkId() {
		return networkId;
	}
	/**
	 * 设置：网点名称
	 */
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	/**
	 * 获取：网点名称
	 */
	public String getNetworkName() {
		return networkName;
	}
	/**
	 * 设置：是否核销
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：是否核销
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：用户唯一编码,自动生成
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * 获取：用户唯一编码,自动生成
	 */
	public String getUserCode() {
		return userCode;
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