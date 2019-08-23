package com.base.modules.business.system.shipmenta.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 出库A表存储
 * 
 * @author huanw
 * @email 
 * @date 2019-08-19 15:11:58
 */
@TableName("biz_shipmenta")
public class ShipmentaEntity implements Serializable {
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
	private Date impDate;
	/**
	 * 导入名称
	 */
	private String impName;
	/**
	 * 类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	private Integer impType;
	/**
	 * 购货单位 11 新飞冰洗旗舰店   12 星诺电器专营店  21 新飞大豫云商专卖店  31 大豫电器商城   41 大豫电器商城
	 */
	private Integer shopType;
	/**
	 * 输出编码
	 */
	private String outCode;
	/**
	 * 
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
	public void setImpDate(Date impDate) {
		this.impDate = impDate;
	}
	/**
	 * 获取：导入日期
	 */
	public Date getImpDate() {
		return impDate;
	}
	/**
	 * 设置：导入名称
	 */
	public void setImpName(String impName) {
		this.impName = impName;
	}
	/**
	 * 获取：导入名称
	 */
	public String getImpName() {
		return impName;
	}
	/**
	 * 设置：类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public void setImpType(Integer impType) {
		this.impType = impType;
	}
	/**
	 * 获取：类型 1：京东 2：天猫 3：淘宝 4：拼多多
	 */
	public Integer getImpType() {
		return impType;
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
	 * 获取：购货单位 11 新飞冰洗旗舰店   12 星诺电器专营店  21 新飞大豫云商专卖店  31 大豫电器商城   41 大豫电器商城
	 * @return
	 */
	public Integer getShopType() {
		return shopType;
	}
	/**
	 * 设置： 购货单位 11 新飞冰洗旗舰店   12 星诺电器专营店  21 新飞大豫云商专卖店  31 大豫电器商城   41 大豫电器商城
	 * @param shopType
	 */
	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}
	
}
