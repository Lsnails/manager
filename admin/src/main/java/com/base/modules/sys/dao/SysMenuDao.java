package com.base.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.base.modules.sys.entity.SysMenuEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 菜单管理
 * @date 2016年9月18日 上午9:33:01
 */
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList(@Param("sysMenuEntity") SysMenuEntity sysMenuEntity);
	
	/**
	 * 获取不包含前端网站的菜单列表
	 */
	List<SysMenuEntity> queryNotFrontMenuList();

	/**
	 * 获取前端网站的菜单列表
	 */
	List<SysMenuEntity> queryFrontMenuList(String language);
	
}
