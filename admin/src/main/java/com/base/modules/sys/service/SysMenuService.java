package com.base.modules.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.base.modules.sys.entity.SysMenuEntity;
import com.base.modules.sys.entity.SysMenuTwoEntity;

import java.util.List;


/**
 * 菜单管理
 * @date 2016年9月18日 上午9:42:16
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单，除前端网站菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList(SysMenuEntity sysMenuEntity);
	
	/**
	 * 获取不包含前端网站的菜单列表
	 */
	List<SysMenuEntity> queryNotFrontMenuList();

	/**
	 * 根据用户 获取不包含前端网站的菜单列表
	 */
	List<SysMenuEntity> queryNotFrontMenuList(Long userId);
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);
	
	/**
	 * 获取前端网站的菜单列表
	 */
	List<SysMenuEntity> queryFrontMenuList(String language);
	/**
	 * 获取网站菜单，外加课程的分类
	 * @Title: queryFrontMenuAndDictionaryList   
	 * @Description: 获取网站菜单，外加课程的分类
	 * @author: huanw
	 * @param: @param language
	 * @param: @return      
	 * @return: List<T>
	 * @date:   2019年3月26日 下午2:21:55       
	 * @throws
	 */
	List<SysMenuTwoEntity> queryFrontMenuAndDictionaryList(String language);
	
	/**
	 * 查询菜单，会按照名称、语种、 类目  0:后台菜单 1：网站菜单 进行查询
	 * @Title: querySysMenuEntityList   
	 * @Description: 查询菜单，会按照名称、语种、 类目  0:后台菜单 1：网站菜单 进行查询
	 * @author: huanw
	 * @param: @param sysMenuEntity
	 * @param: @return      
	 * @return: List<SysMenuEntity>
	 * @date:   2019年6月17日 下午2:08:14       
	 * @throws
	 */
	List<SysMenuEntity> querySysMenuEntityList(SysMenuEntity sysMenuEntity);

	/**
	 * 根据菜单ids获取 后台菜单
	 * @param menuIds
	 * @return
	 */
	public List<SysMenuEntity> queryMenuByIds(List<Long> menuIds);

	/**
	 * 按用户的权限查询用户显示菜单
	 * @param sysMenuEntity
	 * @param userId
	 * @return
	 */
	public List<SysMenuEntity> querySysMenuEntityListByUserId(SysMenuEntity sysMenuEntity,Long userId);
}
