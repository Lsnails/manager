package com.base.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;


/**
 * 部门管理
 * @date 2017-06-20 15:23:47
 */
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//部门ID
	@TableId
	private Long deptId;
	//上级部门ID，一级部门为0
	private Long parentId;
	//部门名称
	@NotBlank(message="部门名称不能为空")
	private String name;
	//上级部门名称
	@TableField(exist=false)
	private String parentName;
	//排序
	private Integer orderNum;
	//网点二维码图片路径
	private String qrcodeurl;
	//网点二维码图片名称
	private String qrcodetitle;
	//二维码展示时间
	private String showTime;
	/**
	 * 图片服务器地址
	 */
	@TableField(exist=false)
	private String imagesHttp;
	@TableLogic
	private Integer delFlag;
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;


	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
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

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getParentName() {
		return parentName;
	}

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
	 * 网点二维码图片路径
	 * @return
	 */
	public String getQrcodeurl() {
		return qrcodeurl;
	}
	/**
	 * 网点二维码图片路径
	 * @param qrcodeurl
	 */
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}

	/**
	 * 网点二维码图片名称
	 * @return
	 */
	public String getQrcodetitle() {
		return qrcodetitle;
	}
	/**
	 * 网点二维码图片名称
	 * @param qrcodetitle
	 */
	public void setQrcodetitle(String qrcodetitle) {
		this.qrcodetitle = qrcodetitle;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public String getImagesHttp() {
		return imagesHttp;
	}

	public void setImagesHttp(String imagesHttp) {
		this.imagesHttp ="http://"+imagesHttp;
	}

	/**
	 * 二维码展示时间
	 * @return
	 */
	public String getShowTime() {
		return showTime;
	}
	/**
	 * 二维码展示时间
	 * @param showTime
	 */
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	
	
}
