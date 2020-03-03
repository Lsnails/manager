package com.base.modules.business.system.datainfo.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 
 * 
 * @author huanw
 * @email 
 * @date 2020-03-02 13:48:17
 */
@TableName("biz_buy_info")
public class BuyInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 电话号码
	 */
	private String phone;
	/**
	 * 产品型号
	 */
	private String proType;
	/**
	 * 购买时间
	 */
	private String buyTime;
	/**
	 * 购买价格
	 */
	private Double buyPrice;
	/**
	 * 购买价格范围 小区间  -非数据库字段
	 */
	@TableField(exist=false)
	private String buyPriceMin;
	/**
	 * 购买价格范围 大区间  -非数据库字段
	 */
	@TableField(exist=false)
	private String buyPriceMax;
	/**
	 * 购买数量
	 */
	private Integer buyNumber;
	/**
	 * 购买渠道
	 */
	private String buyChannel;
	/**
	 * 收货地址（购买地址）
	 */
	private String buyAddress;
	/**
	 * 省市缩写
	 */
	private String address;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 星标
	 */
	private String star;
	/**
	 * 回访记录
	 */
	private String returnInfo;
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
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：电话号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：电话号码
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：产品型号
	 */
	public void setProType(String proType) {
		this.proType = proType;
	}
	/**
	 * 获取：产品型号
	 */
	public String getProType() {
		return proType;
	}
	/**
	 * 设置：购买时间
	 */
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	/**
	 * 获取：购买时间
	 */
	public String getBuyTime() {
		return buyTime;
	}
	/**
	 * 设置：购买价格
	 */
	public Double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}
	/**
	 * 设置：购买数量
	 */
	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}
	/**
	 * 获取：购买数量
	 */
	public Integer getBuyNumber() {
		return buyNumber;
	}
	/**
	 * 设置：购买渠道
	 */
	public void setBuyChannel(String buyChannel) {
		this.buyChannel = buyChannel;
	}
	/**
	 * 获取：购买渠道
	 */
	public String getBuyChannel() {
		return buyChannel;
	}
	/**
	 * 设置：收货地址（购买地址）
	 */
	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}
	/**
	 * 获取：收货地址（购买地址）
	 */
	public String getBuyAddress() {
		return buyAddress;
	}
	/**
	 * 设置：省市缩写
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：省市缩写
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：星标
	 */
	public void setStar(String star) {
		this.star = star;
	}
	/**
	 * 获取：星标
	 */
	public String getStar() {
		return star;
	}
	/**
	 * 设置：回访记录
	 */
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	/**
	 * 获取：回访记录
	 */
	public String getReturnInfo() {
		return returnInfo;
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
	/**
	 * 购买价格范围 小区间  -非数据库字段
	 */
	public String getBuyPriceMin() {
		return buyPriceMin;
	}
	/**
	 * 购买价格范围 小区间  -非数据库字段
	 */
	public void setBuyPriceMin(String buyPriceMin) {
		this.buyPriceMin = buyPriceMin;
	}
	/**
	 * 购买价格范围 大区间  -非数据库字段
	 */
	public String getBuyPriceMax() {
		return buyPriceMax;
	}
	/**
	 * 购买价格范围 大区间  -非数据库字段
	 */
	public void setBuyPriceMax(String buyPriceMax) {
		this.buyPriceMax = buyPriceMax;
	}
	
	
}
