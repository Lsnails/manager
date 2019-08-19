package com.base.modules.business.system.shipmentb.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 出库B表数据
 * 
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:17
 */
@TableName("biz_shipmentb")
public class ShipmentbEntity implements Serializable {
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
	 * 日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;
	/**
	 * 购货单位
	 */
	private String shopUnit;
	/**
	 * 编号
	 */
	private String number;
	/**
	 * 销售方式 1：赊销
	 */
	private Integer saleType;
	/**
	 * 发货
	 */
	private String ship;
	/**
	 * 保管
	 */
	private String storage;
	/**
	 * 销售业务类型
	 */
	private String saleBussinessType;
	/**
	 * 产品代码
	 */
	private String productCode;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 数量
	 */
	private Integer amount;
	/**
	 * 单位成本
	 */
	private BigDecimal unitCost;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 发货仓位
	 */
	private String shipWarehouse;
	/**
	 * 仓位
	 */
	private String warehouse;
	/**
	 * 销售单价
	 */
	private BigDecimal unitPrice;
	/**
	 * 销售金额
	 */
	private BigDecimal price;
	/**
	 * 导入类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	private Integer type;
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
	 * 设置：购货单位
	 */
	public void setShopUnit(String shopUnit) {
		this.shopUnit = shopUnit;
	}
	/**
	 * 获取：购货单位
	 */
	public String getShopUnit() {
		return shopUnit;
	}
	/**
	 * 设置：编号
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * 获取：编号
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * 设置：销售方式 1：赊销
	 */
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	/**
	 * 获取：销售方式 1：赊销
	 */
	public Integer getSaleType() {
		return saleType;
	}
	/**
	 * 设置：发货
	 */
	public void setShip(String ship) {
		this.ship = ship;
	}
	/**
	 * 获取：发货
	 */
	public String getShip() {
		return ship;
	}
	/**
	 * 设置：保管
	 */
	public void setStorage(String storage) {
		this.storage = storage;
	}
	/**
	 * 获取：保管
	 */
	public String getStorage() {
		return storage;
	}
	/**
	 * 设置：销售业务类型
	 */
	public void setSaleBussinessType(String saleBussinessType) {
		this.saleBussinessType = saleBussinessType;
	}
	/**
	 * 获取：销售业务类型
	 */
	public String getSaleBussinessType() {
		return saleBussinessType;
	}
	/**
	 * 设置：产品代码
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * 获取：产品代码
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * 设置：产品名称
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 获取：产品名称
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 设置：单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * 获取：单位
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * 设置：数量
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：数量
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 设置：单位成本
	 */
	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}
	/**
	 * 获取：单位成本
	 */
	public BigDecimal getUnitCost() {
		return unitCost;
	}
	/**
	 * 设置：成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * 获取：成本
	 */
	public BigDecimal getCost() {
		return cost;
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
	 * 设置：发货仓位
	 */
	public void setShipWarehouse(String shipWarehouse) {
		this.shipWarehouse = shipWarehouse;
	}
	/**
	 * 获取：发货仓位
	 */
	public String getShipWarehouse() {
		return shipWarehouse;
	}
	/**
	 * 设置：仓位
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	/**
	 * 获取：仓位
	 */
	public String getWarehouse() {
		return warehouse;
	}
	/**
	 * 设置：销售单价
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	 * 获取：销售单价
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	/**
	 * 设置：销售金额
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：销售金额
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：导入类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：导入类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public Integer getType() {
		return type;
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
