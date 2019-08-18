package com.base.modules.business.system.codenamerelation.entity;


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
 * @date 2019-08-18 12:17:43
 */
@TableName("biz_code_name_relation")
public class CodeNameRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**业务字段，即非数据库字段，使用此注释 @TableField(exist=false)*/
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * code
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 类型 1：冰箱 2：冰柜 3：洗衣机
	 */
	private Integer type;
	/**
	 * 
	 */
	private String fullName;
	/**
	 * 
	 */
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
	 * 设置：code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：类型 1：冰箱 2：冰柜 3：洗衣机
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 1：冰箱 2：冰柜 3：洗衣机
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * 获取：
	 */
	public String getFullName() {
		return fullName;
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
}
