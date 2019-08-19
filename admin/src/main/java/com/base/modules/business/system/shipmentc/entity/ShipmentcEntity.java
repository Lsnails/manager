package com.base.modules.business.system.shipmentc.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 出库C表数据
 * 
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:57
 */
@TableName("biz_shipmentc")
public class ShipmentcEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 
	 */
	private String shipmentId;
	/**
	 * 序号
	 */
	private String serialNumber;
	/**
	 * 订单编号
	 */
	private String orderId;
	/**
	 * 物流单号
	 */
	private String shipmentOrderId;
	/**
	 * 收件人姓名
	 */
	private String orderName;
	/**
	 * 收件人手机
	 */
	private String orderPhone;
	/**
	 * 收货地址
	 */
	private String orderAddress;
	/**
	 * 运输性质
	 */
	private String ysNature;
	/**
	 * 付款方式
	 */
	private String payType;
	/**
	 * 送货方式
	 */
	private String deliverType;
	/**
	 * 货物名称
	 */
	private String productName;
	/**
	 * 数量
	 */
	private Integer number;
	/**
	 * 金额
	 */
	private BigDecimal price;
	/**
	 * 类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	private Integer type;
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
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;
	/**
	 * 修改时间
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
	 * 设置：
	 */
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	/**
	 * 获取：
	 */
	public String getShipmentId() {
		return shipmentId;
	}
	/**
	 * 设置：序号
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * 获取：序号
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * 设置：物流单号
	 */
	public void setShipmentOrderId(String shipmentOrderId) {
		this.shipmentOrderId = shipmentOrderId;
	}
	/**
	 * 获取：物流单号
	 */
	public String getShipmentOrderId() {
		return shipmentOrderId;
	}
	/**
	 * 设置：收件人姓名
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	/**
	 * 获取：收件人姓名
	 */
	public String getOrderName() {
		return orderName;
	}
	/**
	 * 设置：收件人手机
	 */
	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}
	/**
	 * 获取：收件人手机
	 */
	public String getOrderPhone() {
		return orderPhone;
	}
	/**
	 * 设置：收货地址
	 */
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	/**
	 * 获取：收货地址
	 */
	public String getOrderAddress() {
		return orderAddress;
	}
	/**
	 * 设置：运输性质
	 */
	public void setYsNature(String ysNature) {
		this.ysNature = ysNature;
	}
	/**
	 * 获取：运输性质
	 */
	public String getYsNature() {
		return ysNature;
	}
	/**
	 * 设置：付款方式
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	/**
	 * 获取：付款方式
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * 设置：送货方式
	 */
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	/**
	 * 获取：送货方式
	 */
	public String getDeliverType() {
		return deliverType;
	}
	/**
	 * 设置：货物名称
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 获取：货物名称
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 设置：数量
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * 获取：数量
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * 设置：金额
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public Integer getType() {
		return type;
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
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}
