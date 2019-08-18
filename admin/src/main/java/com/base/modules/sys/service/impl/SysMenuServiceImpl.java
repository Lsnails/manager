package com.base.modules.sys.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Constant;
import com.base.common.utils.MapUtils;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;
import com.base.modules.customizesys.dictionary.service.DictionaryService;
import com.base.modules.sys.dao.SysMenuDao;
import com.base.modules.sys.entity.SysMenuEntity;
import com.base.modules.sys.entity.SysMenuTwoEntity;
import com.base.modules.sys.service.SysMenuService;
import com.base.modules.sys.service.SysRoleMenuService;
import com.base.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public List<SysMenuEntity> queryNotFrontMenuList(Long userId) {
		//系统管理员，拥有最高权限 查询全部后台菜单
		if(userId == Constant.SUPER_ADMIN){
			return queryNotFrontMenuList();
		}
		//查询普通用户功能角色
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		List<SysMenuEntity> sysMenuList = this.queryMenuByIds(menuIdList);
		return sysMenuList;
	}

	@Override
	public List<SysMenuEntity> queryMenuByIds(List<Long> menuIds){
		EntityWrapper<SysMenuEntity> wrapper = new EntityWrapper<SysMenuEntity>();
		wrapper.eq("isenable", "Y");// 是否启用  N：停用 Y：启用
		wrapper.eq("kind", 0); //类目  0:后台菜单 1：网站菜单
		if(menuIds!=null && !menuIds.isEmpty()){
			wrapper.in("menu_id",menuIds);
		}
		wrapper.orderBy("order_num");
		return this.selectList(wrapper);
	}
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList(SysMenuEntity sysMenuEntity) {
		return baseMapper.queryNotButtonList(sysMenuEntity);
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.deleteById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.deleteByMap(new MapUtils().put("menu_id", menuId));
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表，除前端网站菜单
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}

	@Override
	public List<SysMenuEntity> queryNotFrontMenuList() {
		return baseMapper.queryNotFrontMenuList();
	}

	@Override
	public List<SysMenuEntity> queryFrontMenuList(String language) {
		return baseMapper.queryFrontMenuList(language);
	}
	
	@Cacheable(value="queryFrontMenuAndDictionaryList",key="'menu_'+#language")
	@Override
	public List<SysMenuTwoEntity> queryFrontMenuAndDictionaryList(String language) {
		//1.从数据库查出来的菜单
		List<SysMenuEntity> menuList = this.queryFrontMenuList(language);
		//2.查询字典的分类
		List<SysMenuTwoEntity> menuDictionaryList = new ArrayList<>();
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = this.selectById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}else {
				// 菜单名称为“学院课程”时候，课程分类，将数据挂在此菜单下
				if(sysMenuEntity.getName().equals(com.base.modules.customizesys.helper.Constant.MENU_COURSENAME) || com.base.modules.customizesys.helper.Constant.MENU_COURSENAME_ENGLISH.equals(sysMenuEntity.getName())) {
					//课程 英文的分类父节点也必须是"学院课程"字典树分类，课程一级菜单必须叫“学院课程”
					this.getMenuDictionary(sysMenuEntity, language, menuDictionaryList,com.base.modules.customizesys.helper.Constant.COURSE_CLASSIFICATION);
				}
				// 菜单名称为“学院师资”时候，导师分类，将数据挂在此菜单下
				if(sysMenuEntity.getName().equals(com.base.modules.customizesys.helper.Constant.MENU_PROFESSORNAME) || com.base.modules.customizesys.helper.Constant.MENU_PROFESSORNAME_ENGLISH.equals(sysMenuEntity.getName())) {
					//课程 英文的分类父节点也必须是"学院师资"字典树分类，导师一级菜单必须叫“学院师资”
					this.getMenuDictionary(sysMenuEntity, language, menuDictionaryList,com.base.modules.customizesys.helper.Constant.PROFESSOR_CLASSIFICATION);
				}
				
			}
		}
		//3.allMenuList数据菜单与字典分类组装成新的菜单
		List<SysMenuTwoEntity> allMenuList =new ArrayList<>();
		//统一用SysMenuTwoEntity，因为这个实体字段是字符串,将menuList的数据对象转成SysMenuTwoEntity
		for (SysMenuEntity sysMenu : menuList) {
			SysMenuTwoEntity sysMenuTwo =new SysMenuTwoEntity();
			sysMenuTwo.setMenuId(String.valueOf(sysMenu.getMenuId()));
			sysMenuTwo.setParentId(String.valueOf(sysMenu.getParentId()));
			sysMenuTwo.setParentName(sysMenu.getParentName());
			sysMenuTwo.setName(sysMenu.getName());
			sysMenuTwo.setUrl(sysMenu.getUrl());
			sysMenuTwo.setOpen(sysMenu.getOpen());
			sysMenuTwo.setOrderNum(sysMenu.getOrderNum());
			sysMenuTwo.setLanguage(language);
			allMenuList.add(sysMenuTwo);
		}
		allMenuList.addAll(menuDictionaryList);
		//4.递归处理数据，为前端服务
		List<SysMenuTwoEntity> newAllMenuList =new ArrayList<>();
	    this.getSysMenuTwoTreeList(allMenuList, newAllMenuList);
		return newAllMenuList;
	}
	/**
	 * 先查询一级分类，然后再调递归查询子分类方法
	 */
	private void getSysMenuTwoTreeList(List<SysMenuTwoEntity> allMenuList,List<SysMenuTwoEntity> newAllMenuList) {
		for (SysMenuTwoEntity sysMenuTwo : allMenuList) {
			String parentId = sysMenuTwo.getParentId();
			if("0".equals(parentId)) {
				//需要递归查询pId为当前菜单menuId的，即是，此菜单的子菜单
				List<SysMenuTwoEntity> childrenSysMenuTwoList = this.recursiveGetchildrenSysMenuList(sysMenuTwo,allMenuList);
				sysMenuTwo.setChildren(childrenSysMenuTwoList);
				//一级菜单
				newAllMenuList.add(sysMenuTwo);
			}
		}
	}
	/**
	 * 递归查询一级分类下其他分类，
	 */
	private List<SysMenuTwoEntity> recursiveGetchildrenSysMenuList(SysMenuTwoEntity sysMenuTwo,List<SysMenuTwoEntity> allMenuList) {
		List<SysMenuTwoEntity> childrenSysMenuTwoList =new ArrayList<>();
		String menuId = sysMenuTwo.getMenuId();
		for (SysMenuTwoEntity sysMenuTwo2 : allMenuList) {
			String parentId = sysMenuTwo2.getParentId();
			if(StringUtils.isNotBlank(parentId) && parentId.equals(menuId)) {
				//递归查询子节点
				sysMenuTwo2.setChildren(this.recursiveGetchildrenSysMenuList(sysMenuTwo2,allMenuList));
				childrenSysMenuTwoList.add(sysMenuTwo2);
			}
		}
		return childrenSysMenuTwoList;
	}
	/**
	 * 通过菜单名称，获取此菜单对应的字典分类，并挂在原菜单上，形成新的树结构
	 * @Title: getMenuDictionary   
	 * @Description: 通过菜单名称，获取此菜单对应的字典分类，并挂在原菜单上，形成新的树结构
	 * @author: huanw
	 * @param: @param sysMenuEntity
	 * @param: @param language 
	 * @param: @param menuDictionaryList
	 * @param: @param classificationName       通过分类名称，查询此分类下的其他分类（不包含此分类）
	 * @return: void
	 * @date:   2019年4月1日 下午4:03:05       
	 * @throws
	 */
	private void getMenuDictionary(SysMenuEntity sysMenuEntity,String language,List<SysMenuTwoEntity> menuDictionaryList,String classificationName) {
		List<DictionaryEntity> queryFirstLevelDictionaryList = dictionaryService.queryFirstLevelDictionaryByName(classificationName, language);
		for (DictionaryEntity dictionary : queryFirstLevelDictionaryList) {
			SysMenuTwoEntity sysMenu =new SysMenuTwoEntity();
			sysMenu.setMenuId(dictionary.getDictId());
			sysMenu.setParentId(String.valueOf(sysMenuEntity.getMenuId()));
			sysMenu.setParentName(sysMenuEntity.getName());
			sysMenu.setName(dictionary.getName());
			sysMenu.setUrl(dictionary.getUrl());
			sysMenu.setOpen(dictionary.getOpen());
			sysMenu.setOrderNum(dictionary.getOrderNum());
			sysMenu.setLanguage(language);
			menuDictionaryList.add(sysMenu);
		}
	}
//	@Override
//	public List<SysMenuTwoEntity> queryFrontMenuOnlyAboutUsList(String language) {
//		//1.从数据库查出来的菜单
//		List<SysMenuEntity> menuList = this.queryFrontMenuList(language);
//		List<SysMenuEntity> menuList11 = new ArrayList<>();
//		for(SysMenuEntity sysMenuEntity : menuList){
//			SysMenuEntity parentMenuEntity = this.selectById(sysMenuEntity.getParentId());
//			if(parentMenuEntity == null){
//				// 菜单名称为“关于我们”时候，查询关于我们下面的菜单
//				if(sysMenuEntity.getName().equals(com.base.modules.customizesys.helper.Constant.MENU_ABOUTUS) || com.base.modules.customizesys.helper.Constant.MENU_ABOUTUS_ENGLISH.equals(sysMenuEntity.getName())) {
//					//递归查询该菜单下其他菜单
//					//递归获取子菜单
//					menuList11.add(sysMenuEntity);
//					getMenuTreeList(menuList11, null);
//					System.out.println(menuList11);
//				}
//			}
//		}
//		//3.allMenuList数据菜单与字典分类组装成新的菜单
//		List<SysMenuTwoEntity> allMenuList =new ArrayList<>();
//		//统一用SysMenuTwoEntity，因为这个实体字段是字符串,将menuList的数据对象转成SysMenuTwoEntity
//		for (SysMenuEntity sysMenu : menuList11) {
//			SysMenuTwoEntity sysMenuTwo =new SysMenuTwoEntity();
//			sysMenuTwo.setMenuId(String.valueOf(sysMenu.getMenuId()));
//			sysMenuTwo.setParentId(String.valueOf(sysMenu.getParentId()));
//			sysMenuTwo.setParentName(sysMenu.getParentName());
//			sysMenuTwo.setName(sysMenu.getName());
//			sysMenuTwo.setUrl(sysMenu.getUrl());
//			sysMenuTwo.setOpen(sysMenu.getOpen());
//			sysMenuTwo.setOrderNum(sysMenu.getOrderNum());
//			sysMenuTwo.setLanguage(language);
//			allMenuList.add(sysMenuTwo);
//		}
//		//4.递归处理数据，为前端服务
//		List<SysMenuTwoEntity> newAllMenuList =new ArrayList<>();
//	    this.getSysMenuTwoTreeList(allMenuList, newAllMenuList);
//		return newAllMenuList;
//	}
	

	@Override
	public List<SysMenuEntity> querySysMenuEntityList(SysMenuEntity sysMenuEntity) {
		//按条件查询数据
		EntityWrapper<SysMenuEntity> wrapper = new EntityWrapper<SysMenuEntity>();
		wrapper.eq("isenable", "Y");// 是否启用  N：停用 Y：启用
		String tempName=null;
		if(sysMenuEntity!=null) {
			String language = sysMenuEntity.getLanguage();//语种 1：中文  2：英文
			Integer kind = sysMenuEntity.getKind();//类目  0:后台菜单 1：网站菜单
			String name = sysMenuEntity.getName();//菜单名称
			tempName = name;
			if(kind!=null && !"".equals(kind.toString())) {
				wrapper.eq("kind", kind);
			}
			if(StringUtils.isNotBlank(language)) {
				wrapper.eq("language", language);
			}
			if(StringUtils.isNotBlank(name)) {
				wrapper.like("name", name);
			}
		}
		wrapper.orderBy("order_num");
		List<SysMenuEntity> menuList = this.selectList(wrapper);
		//如果查询条件有名称，需要重新组装数据树
		if(StringUtils.isNotBlank(tempName)) {
			List<SysMenuEntity> newSysMenuList=new ArrayList<>();
			//1.有查询条件，将查询出来的结果，需要反向将父节点查询出来，重新组织树数据
			for(SysMenuEntity sysMenu : menuList){
				//递归查询父节点
				this.getParentMenuTreeList(sysMenu, newSysMenuList);
			}
			//2.去除重复数据开始
			Map<String,SysMenuEntity> map=new HashMap<>();
			for(SysMenuEntity sysMenu : newSysMenuList){
				Long menuId = sysMenu.getMenuId();
				map.put(menuId.toString(), sysMenu);
			}
			//3.去除重复数据结束
			menuList = new ArrayList<>();
			for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
				SysMenuEntity sysMenu = map.get(key);
				menuList.add(sysMenu);
			}
		}else {
			for(SysMenuEntity sysMenu : menuList){
				SysMenuEntity parentMenuEntity = this.selectById(sysMenu.getParentId());
				if(parentMenuEntity != null){
					sysMenu.setParentName(parentMenuEntity.getName());
				}
			}
		}
		return menuList;
	}
	@Override
	public List<SysMenuEntity> querySysMenuEntityListByUserId(SysMenuEntity sysMenuEntity,Long userId) {
		//按条件查询数据
		EntityWrapper<SysMenuEntity> wrapper = new EntityWrapper<SysMenuEntity>();
		wrapper.eq("isenable", "Y");// 是否启用  N：停用 Y：启用
		//系统管理员，拥有最高权限 查询全部后台菜单
		if(userId == Constant.SUPER_ADMIN){

		}else{
			//用户菜单列表
			List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
			//普通用户需要，按用户权限显示菜单
			wrapper.in("menu_id",menuIdList);
		}


		String tempName=null;
		if(sysMenuEntity!=null) {
			String language = sysMenuEntity.getLanguage();//语种 1：中文  2：英文
			Integer kind = sysMenuEntity.getKind();//类目  0:后台菜单 1：网站菜单
			String name = sysMenuEntity.getName();//菜单名称
			tempName = name;
			if(kind!=null && !"".equals(kind.toString())) {
				wrapper.eq("kind", kind);
			}
			if(StringUtils.isNotBlank(language)) {
				wrapper.eq("language", language);
			}
			if(StringUtils.isNotBlank(name)) {
				wrapper.like("name", name);
			}
		}
		wrapper.orderBy("menu_id,order_num");
		List<SysMenuEntity> menuList = this.selectList(wrapper);
		//如果查询条件有名称，需要重新组装数据树
		if(StringUtils.isNotBlank(tempName)) {
			List<SysMenuEntity> newSysMenuList=new ArrayList<>();
			//1.有查询条件，将查询出来的结果，需要反向将父节点查询出来，重新组织树数据
			for(SysMenuEntity sysMenu : menuList){
				//递归查询父节点
				this.getParentMenuTreeList(sysMenu, newSysMenuList);
			}
			//2.去除重复数据开始
			Map<String,SysMenuEntity> map=new HashMap<>();
			for(SysMenuEntity sysMenu : newSysMenuList){
				Long menuId = sysMenu.getMenuId();
				map.put(menuId.toString(), sysMenu);
			}
			//3.去除重复数据结束
			menuList = new ArrayList<>();
			for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
				SysMenuEntity sysMenu = map.get(key);
				menuList.add(sysMenu);
			}
		}else {
			for(SysMenuEntity sysMenu : menuList){
				SysMenuEntity parentMenuEntity = this.selectById(sysMenu.getParentId());
				if(parentMenuEntity != null){
					sysMenu.setParentName(parentMenuEntity.getName());
				}
			}
		}
		return menuList;
	}
	
	public void getParentMenuTreeList(SysMenuEntity sysMenu,List<SysMenuEntity> newSysMenuList) {
		Long parentId = sysMenu.getParentId();
		if(!parentId.equals("0")) {
			EntityWrapper<SysMenuEntity> wrapper = new EntityWrapper<SysMenuEntity>();
			wrapper.eq("isenable", "Y");// 是否启用  N：停用 Y：启用
			wrapper.eq("menu_id",parentId);
			SysMenuEntity parentSysMenu = this.selectOne(wrapper);
			if(parentSysMenu!=null) {
				sysMenu.setParentName(parentSysMenu.getName());
				newSysMenuList.add(parentSysMenu);
				getParentMenuTreeList(parentSysMenu, newSysMenuList);
			}
			
		}
		newSysMenuList.add(sysMenu);
	}
}
