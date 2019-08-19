package com.base.modules.business.system.storageb.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 入库生成B表
 * 
 * @author huanw
 * @email 
 * @date 2019-08-19 10:50:46
 */
@TableName("biz_storageb")
public class StoragebEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 入库A表的唯一id
	 */
	private String storageId;
	/**
	 * 日期
	 */
	private Date date;
	/**
	 * 供应商
	 */
	private String supplier;
	/**
	 * 编号
	 */
	private String number;
	/**
	 * 验收
	 */
	private String accept;
	/**
	 * 保管
	 */
	private String storage;
	/**
	 * 采购方式  1 : 赊购
	 */
	private Integer buyType;
	/**
	 * 物料编码
	 */
	private String materNumber;
	/**
	 * 物料名称
	 */
	private String materName;
	/**
	 * 单位 1：台
	 */
	private Integer unit;
	/**
	 * 数量
	 */
	private Integer amount;
	/**
	 * 单价
	 */
	private BigDecimal unitPrice;
	/**
	 * 金额
	 */
	private BigDecimal price;
	/**
	 * 收料仓库
	 */
	private String warehouse;
	/**
	 * 仓位
	 */
	private String position;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
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
	 * 设置：
	 */
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	/**
	 * 获取：
	 */
	public String getStorageId() {
		return storageId;
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
	 * 设置：供应商
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	/**
	 * 获取：供应商
	 */
	public String getSupplier() {
		return supplier;
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
	 * 设置：验收
	 */
	public void setAccept(String accept) {
		this.accept = accept;
	}
	/**
	 * 获取：验收
	 */
	public String getAccept() {
		return accept;
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
	 * 设置：采购方式  1 : 赊购
	 */
	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}
	/**
	 * 获取：采购方式  1 : 赊购
	 */
	public Integer getBuyType() {
		return buyType;
	}
	/**
	 * 设置：物料编码
	 */
	public void setMaterNumber(String materNumber) {
		this.materNumber = materNumber;
	}
	/**
	 * 获取：物料编码
	 */
	public String getMaterNumber() {
		return materNumber;
	}
	/**
	 * 设置：物料名称
	 */
	public void setMaterName(String materName) {
		this.materName = materName;
	}
	/**
	 * 获取：物料名称
	 */
	public String getMaterName() {
		return materName;
	}
	/**
	 * 设置：单位 1：台
	 */
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	/**
	 * 获取：单位 1：台
	 */
	public Integer getUnit() {
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
	 * 设置：单价
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	 * 获取：单价
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
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
	 * 设置：收料仓库
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	/**
	 * 获取：收料仓库
	 */
	public String getWarehouse() {
		return warehouse;
	}
	/**
	 * 设置：仓位
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * 获取：仓位
	 */
	public String getPosition() {
		return position;
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
