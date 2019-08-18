package com.base.modules.customizesys.dictionary.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.List;

/**
 * 业务数据字典包含多层次数据
 * 
 * @author huanw
 * @email 
 * @date 2019-02-11 17:09:10
 */
@TableName("biz_dictionary")
public class DictionaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private String dictId;
	/**
	 * 父字典ID，一级菜单为0
	 */
	private String parentId;
	/**
	 * 字典code
	 */
	private String code;
	/**
	 * 字典名称
	 */
	private String name;
	/**
	 * url
	 */
	private String url;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 是否启用  0：停用 1：启用
	 */
	private String enabled;
	/**
	 * 语种：1：中文  2：英文
	 */
	private String language;
	
	/**
	 * 父字典名称
	 */
	@TableField(exist=false)
	private String parentName;
	
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	/**
	 * 网站组装数据使用
	 */
	@TableField(exist=false)
	private List<?> children;

	/**
	 * 设置：
	 */
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
	/**
	 * 获取：
	 */
	public String getDictId() {
		return dictId;
	}
	/**
	 * 设置：父字典ID，一级菜单为0
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父字典ID，一级菜单为0
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * 设置：字典code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：字典code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：字典名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：字典名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：描述
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 获取：图标
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：是否启用  0：停用 1：启用
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	/**
	 * 获取：是否启用  0：停用 1：启用
	 */
	public String getEnabled() {
		return enabled;
	}
	/**
	 * 获取：父字典名称
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * 设置：父字典名称
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	/**
	 * 获取：语种1：中文  2：英文
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * 设置：语种1：中文  2：英文
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * 获取：url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	public List<?> getChildren() {
		return children;
	}
	public void setChildren(List<?> children) {
		this.children = children;
	}
}
