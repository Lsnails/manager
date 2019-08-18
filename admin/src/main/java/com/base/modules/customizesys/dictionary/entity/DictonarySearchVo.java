package com.base.modules.customizesys.dictionary.entity;


/**
 * 数据字典查询条件参数对象类
 * @ClassName:  DictonarySearchVo   
 * @Description:数据字典查询条件参数对象类
 * @author:  huanw
 * @date:   2019年2月19日 下午1:13:56
 */
public class DictonarySearchVo {
	private String[] dictIds;
	private String[] moduleIds;
	public String[] getDictIds() {
		return dictIds;
	}
	public void setDictIds(String[] dictIds) {
		this.dictIds = dictIds;
	}
	public String[] getModuleIds() {
		return moduleIds;
	}
	public void setModuleIds(String[] moduleIds) {
		this.moduleIds = moduleIds;
	}
	
}
