package com.base.modules.customizesys.dictionary.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.customizesys.dictionary.dao.DictionaryDao;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;
import com.base.modules.customizesys.dictionary.service.DictionaryService;
import com.base.modules.customizesys.helper.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dictionaryService")
public class DictionaryServiceImpl extends ServiceImpl<DictionaryDao, DictionaryEntity> implements DictionaryService {

    @Override
    public Page<DictionaryEntity> queryPage(Map<String, Object> params) {
        Page<DictionaryEntity> page = this.selectPage(
                new Query<DictionaryEntity>(params).getPage(),
                new EntityWrapper<DictionaryEntity>().eq("enabled", "1")
                .eq("parent_id", "").or().isNull("parent_id").orderBy("order_num")
        );

        return page;
    }

	@Override
	public List<DictionaryEntity> queryDictionaryEntityList(List<DictionaryEntity> dictionaryList) {
		List<DictionaryEntity> newDictionaryList=new ArrayList<>();
		getDictionaryTreeList(dictionaryList,null,newDictionaryList);
		return newDictionaryList;
	}

	/**
	 * 通过父节点，递归查询子节点，组织数据字典TreeTable数据
	 * @Title: getDictionaryTreeList   
	 * @Description: 组织数据字典TreeTable数据
	 * @author: huanw
	 * @param: @param dictionaryList
	 * @param: @param parentName
	 * @param: @param newDictionaryList      
	 * @return: void
	 * @date:   2019年2月12日 下午4:58:09       
	 * @throws
	 */
	private void getDictionaryTreeList(List<DictionaryEntity> dictionaryList,String parentName,List<DictionaryEntity> newDictionaryList) {
		for (DictionaryEntity dictionary : dictionaryList) {
			String dictId = dictionary.getDictId();
			if(StringUtils.isNotBlank(dictionary.getParentId())) {
				dictionary.setParentName(parentName);
			}
			newDictionaryList.add(dictionary);
			//子分类集合
			List<DictionaryEntity> subclassDictionaryList = this.selectList(new EntityWrapper<DictionaryEntity>().eq("enabled", "1").eq("parent_id", dictId).orderBy("order_num"));
			if(!subclassDictionaryList.isEmpty()) {
				getDictionaryTreeList(subclassDictionaryList,dictionary.getName(),newDictionaryList);
			}
		}
	}

	@Override
	public List<DictionaryEntity> queryDictionaryEntityList(Map<String, Object> params,DictionaryEntity dictionary) {
		List<DictionaryEntity> dictionaryList=new ArrayList<>();
		Wrapper<DictionaryEntity> wrapperDictionary = new EntityWrapper<DictionaryEntity>().eq("enabled", "1");
		String tempName="";//标识是否有查询条件
		if(dictionary!=null) {
			String name = dictionary.getName();
			tempName=name;
			wrapperDictionary.like(StringUtils.isNotBlank(name), "name", name);
			wrapperDictionary.eq(StringUtils.isNotBlank(dictionary.getLanguage()), "language",dictionary.getLanguage());
		}
		wrapperDictionary.orderBy("order_num");
		dictionaryList = this.selectList(wrapperDictionary);
		if(StringUtils.isNotBlank(tempName)) {
			//有查询条件，将查询出来的结果，需要反向将父节点查询出来，重新组织树数据
			List<DictionaryEntity> newDictionaryList = new ArrayList<>();
			for(DictionaryEntity dictionaryEntity : dictionaryList){
				getParentDictionaryTreeList(dictionaryEntity,newDictionaryList);
			}
			//去除重复数据开始
			Map<String,DictionaryEntity> map=new HashMap<>();
			for (int i = 0; i < newDictionaryList.size(); i++) {
				String dictId = newDictionaryList.get(i).getDictId();
				map.put(dictId, newDictionaryList.get(i));
			}
			//去除重复数据结束
			dictionaryList = new ArrayList<>();
			for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
				DictionaryEntity dictionaryEntity = map.get(key);
				dictionaryList.add(dictionaryEntity);
			}
		}else {
			//无查询条件
	        for(DictionaryEntity dictionaryEntity : dictionaryList){
	        	DictionaryEntity parentMenuEntity = this.selectById(dictionaryEntity.getParentId());
				if(parentMenuEntity != null){
					dictionaryEntity.setParentName(parentMenuEntity.getName());
				}
			}
		}
		return dictionaryList;
	}
	/**
	 * 通过界面查询条件，递归查询满足条件的父节点，组织数据字典TreeTable数据
	 * @Title: getParentDictionaryTreeList   
	 * @Description: 通过界面查询条件，递归查询满足条件的父节点，组织数据字典TreeTable数据 
	 * @author: huanw
	 * @param: @param dictionary
	 * @param: @param newDictionaryList      空集合，用于返回数据
	 * @return: void
	 * @date:   2019年2月13日 上午10:54:31       
	 * @throws
	 */
	private void getParentDictionaryTreeList(DictionaryEntity dictionary,List<DictionaryEntity> newDictionaryList) {
		String parentId = dictionary.getParentId();
		//父分类集合
		if(StringUtils.isNotBlank(parentId)) {
			DictionaryEntity parentDictionary = this.selectOne(new EntityWrapper<DictionaryEntity>().eq("enabled", "1").eq("dict_id", parentId).orderBy("order_num"));
			if(parentDictionary!=null) {
				dictionary.setParentName(parentDictionary.getName());
				newDictionaryList.add(parentDictionary);
				getParentDictionaryTreeList(parentDictionary, newDictionaryList);
			}
		}
		newDictionaryList.add(dictionary);
	}

	@Override
	public List<DictionaryEntity> queryParentDictionaryTreeList(DictionaryEntity dictionary) {
		List<DictionaryEntity>  newDictionaryList = new ArrayList<>();
		this.getParentDictionaryTreeList(dictionary, newDictionaryList);
		//去除重复数据开始
		Map<String,DictionaryEntity> map=new HashMap<>();
		for (int i = 0; i < newDictionaryList.size(); i++) {
			String dictId = newDictionaryList.get(i).getDictId();
			map.put(dictId, newDictionaryList.get(i));
		}
		//去除重复数据结束
		List<DictionaryEntity> dictionaryList = new ArrayList<>();
		for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
			DictionaryEntity dictionaryEntity = map.get(key);
			dictionaryList.add(dictionaryEntity);
		}
		return dictionaryList;
	}

	@Override
	public List<DictionaryEntity> queryDictionaryIsOccupy(List<String> dictIds) {
		List<DictionaryEntity> dictionaryList = baseMapper.queryDictionaryIsOccupy(dictIds);
		return dictionaryList;
	}

	@Override
	public boolean checkIsLeafnode(List<String> dictIds) {
		List<DictionaryEntity> dictionaryList = this.selectBatchIds(dictIds);
		List<DictionaryEntity> newDictionaryList = new ArrayList<>();
		getDictionaryTreeList(dictionaryList, null, newDictionaryList);
		boolean flag=true;//叶子节点
		if(newDictionaryList!=null & newDictionaryList.size()>1) {
			flag =false;//中间节点
		}
		return flag;
	}

	@Override
	public Map<String, List<String>> getDictionaryOccupyData(List<DictionaryEntity> dictionaryList) {
		Map<String,List<String>> map=new HashMap<>();
    	for (int i = 0; i <dictionaryList.size(); i++) {
    		DictionaryEntity dictionaryEntity = dictionaryList.get(i);
    		String description = dictionaryEntity.getDescription();//存放是的课程、教授、领域等分类
    		String name = dictionaryEntity.getName();//存放的是使用分类的课程名称、教授名称等
    		List<String> nameList =new ArrayList<>();
    		if(map.containsKey(description)) {
    			nameList= map.get(description);
    		}
    		nameList.add(name);
    		map.put(description, nameList);
		}
    	return map;
	}

	@Override
	public String getDictionaryNameByDictId(String dictId) {
		DictionaryEntity dictionaryEntity = baseMapper.selectById(dictId);
		return dictionaryEntity==null?"":dictionaryEntity.getName();
	}

	@Override
	public List<DictionaryEntity> queryFirstLevelDictionaryByName(String name, String language) {
		//1.根据名称、语种查询出该分类，由于在菜单中，不显示改分类，所有仅取该分类下的一级分类
		EntityWrapper<DictionaryEntity> entityWrapper = new EntityWrapper<DictionaryEntity>();
		entityWrapper.eq("enabled", "1");
		entityWrapper.eq(StringUtils.isNotBlank(language), "language", language);
		// 课程 英文的分类父节点也必须是"学院课程"字典树分类，课程一级菜单必须叫“学院课程”
		entityWrapper.eq("name", name);
		DictionaryEntity dictionaryEntity = this.selectOne(entityWrapper);
		
		//2.查询，该分类下的第一层分类
		EntityWrapper<DictionaryEntity> entityWrapper1 = new EntityWrapper<DictionaryEntity>();
		entityWrapper1.eq("enabled", "1");
		entityWrapper1.eq(StringUtils.isNotBlank(language), "language", language);
		// 课程 英文的分类父节点也必须是"学院课程"字典树分类，课程一级菜单必须叫“学院课程”
		entityWrapper1.eq("parent_id", dictionaryEntity.getDictId());
		entityWrapper1.orderBy("order_num");
		//查询学院课程 下的第一层分类
		List<DictionaryEntity> queryDictionaryList = this.selectList(entityWrapper1);
		return queryDictionaryList;
	}

	@Cacheable(value="courseClsList",key="'courseCls_'+#language")
	@Override
	public List<DictionaryEntity> courseClsList(String language) {
		if(language == null || "".equals(language)) {
			return null;
		}
		//1.查询学院课程
		DictionaryEntity     dictionaryEntity = this.selectOneCourse(language, Constant.COURSE_CLASSIFICATION);
		if(dictionaryEntity == null) {
			return null;
		}
		List<DictionaryEntity> dictionaryList = new ArrayList<>();
		dictionaryList.add(dictionaryEntity);
		//2.递归查询学院课程下的分类
		List<DictionaryEntity> queryDictionaryEntityList = this.queryDictionaryEntityList(dictionaryList);
		//3.封装数据为前端服务
		List<DictionaryEntity> recursiveGetchildrenSysMenuList = this.recursiveGetchildrenSysMenuList(dictionaryEntity, queryDictionaryEntityList);
		return recursiveGetchildrenSysMenuList;
	}
//	/**
//	 * 先查询一级分类，然后再调递归查询子分类方法
//	 */
//	private void getSysMenuTwoTreeList(List<SysMenuTwoEntity> allMenuList,List<SysMenuTwoEntity> newAllMenuList) {
//		for (SysMenuTwoEntity sysMenuTwo : allMenuList) {
//			String parentId = sysMenuTwo.getParentId();
//			if("0".equals(parentId)) {
//				//需要递归查询pId为当前菜单menuId的，即是，此菜单的子菜单
//				List<SysMenuTwoEntity> childrenSysMenuTwoList = this.recursiveGetchildrenSysMenuList(sysMenuTwo,allMenuList);
//				sysMenuTwo.setChildren(childrenSysMenuTwoList);
//				//一级菜单
//				newAllMenuList.add(sysMenuTwo);
//			}
//		}
//	}
	/**
	 * 递归查询dictionary分类下其他分类，
	 */
	private List<DictionaryEntity> recursiveGetchildrenSysMenuList(DictionaryEntity dictionary,List<DictionaryEntity> allDictionaryList) {
		List<DictionaryEntity> childrenDictionaryList =new ArrayList<>();
		String dictId = dictionary.getDictId();
		for (DictionaryEntity dictionaryEntity : allDictionaryList) {
			String parentId = dictionaryEntity.getParentId();
			if(StringUtils.isNotBlank(parentId) && parentId.equals(dictId)) {
				//递归查询子节点
				dictionaryEntity.setChildren(this.recursiveGetchildrenSysMenuList(dictionaryEntity,allDictionaryList));
				childrenDictionaryList.add(dictionaryEntity);
			}
		}
		return childrenDictionaryList;
	}
	/**
	 * 按语种、字典分类名称查询字典信息
	 * @Title: selectOneCourse   
	 * @Description:  按语种、字典分类名称查询字典信息
	 * @author: huanw
	 * @param: @param language
	 * @param: @param dicName
	 * @param: @return      
	 * @return: DictionaryEntity
	 * @date:   2019年4月4日 上午11:35:54       
	 * @throws
	 */
	private DictionaryEntity selectOneCourse(String language,String dicName) {
		EntityWrapper<DictionaryEntity> entityWrapper = new EntityWrapper<DictionaryEntity>();
		entityWrapper.eq("enabled", "1");
		entityWrapper.eq(StringUtils.isNotBlank(language), "language", language);
		entityWrapper.eq("name", dicName);
		DictionaryEntity dictionaryEntity =new DictionaryEntity();
		dictionaryEntity = this.selectOne(entityWrapper);
		return dictionaryEntity;
	}

	@Cacheable(value="professorClsList",key="'professorCls_'+#language")
	@Override
	public List<DictionaryEntity> professorClsList(String language) {
		if(language == null || "".equals(language)) {
			return null;
		}
		//1.查询学院师资
		DictionaryEntity     dictionaryEntity = this.selectOneCourse(language, Constant.PROFESSOR_CLASSIFICATION);
		if(dictionaryEntity == null) {
			return null;
		}
		List<DictionaryEntity> dictionaryList = new ArrayList<>();
		dictionaryList.add(dictionaryEntity);
		//2.递归查询学院师资下的分类
		List<DictionaryEntity> queryDictionaryEntityList = this.queryDictionaryEntityList(dictionaryList);
		//3.封装数据为前端服务
		List<DictionaryEntity> recursiveGetchildrenSysMenuList = this.recursiveGetchildrenSysMenuList(dictionaryEntity, queryDictionaryEntityList);
		return recursiveGetchildrenSysMenuList;
	}

	@Override
	public DictionaryEntity getInfoByCode(String code) {
		EntityWrapper<DictionaryEntity> entityWrapper = new EntityWrapper<DictionaryEntity>();
		entityWrapper.eq("code",code);
		return this.selectOne(entityWrapper);
	}
}
