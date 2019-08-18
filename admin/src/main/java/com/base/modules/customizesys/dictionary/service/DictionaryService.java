package com.base.modules.customizesys.dictionary.service;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;

/**
 * 业务数据字典包含多层次数据
 *
 * @author huanw
 * @email 
 * @date 2019-02-11 17:09:10
 */
public interface DictionaryService extends IService<DictionaryEntity> {

    Page<DictionaryEntity> queryPage(Map<String, Object> params);
    /**
     * 通过分页查询的一级分类信息，查询一级分类下的所有子分类
     * @Title: queryDictionaryEntityList   
     * @Description: 通过分页查询的一级分类信息，查询一级分类下的所有子分类
     * @author: huanw
     * @param: @param dictionaryList  一级分类集合
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年2月12日 下午2:15:09       
     * @throws
     */
    List<DictionaryEntity> queryDictionaryEntityList(List<DictionaryEntity> dictionaryList);
    /**
     * 查询数据字典list，包含界面搜索条件
     * @Title: queryDictionaryEntityList   
     * @Description: 查询数据字典list，包含界面搜索条件
     * @author: huanw
     * @param: @param params
     * @param: @param dictionary
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年2月13日 下午3:11:35       
     * @throws
     */
    List<DictionaryEntity> queryDictionaryEntityList(Map<String, Object> params,DictionaryEntity dictionary);
    /**
     * 通过当前节点，获取当前节点+父节点节集合
     * @Title: queryParentDictionaryTreeList   
     * @Description: 通过当前节点，获取当前节点+父节点节集合
     * @author: huanw
     * @param: @param dictionary
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年2月13日 下午4:51:32       
     * @throws
     */
    List<DictionaryEntity> queryParentDictionaryTreeList(DictionaryEntity dictionary);
    /**
     * 查询被删除的体系是否被占用，如果占用不能删除
     * @Title: queryDictionaryIsOccupy   
     * @Description: TODO(这里用一句话描述这个方法的作用)   
     * @author: huanw
     * @param: @param idList
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年2月14日 下午2:31:45       
     * @throws
     */
    List<DictionaryEntity> queryDictionaryIsOccupy(@Param("list") List<String> dictIds);
    /**
     * 检查删除的节点是否是叶子节点
     * @Title: checkIsLeafnode   
     * @Description: 检查删除的节点是否是叶子节点
     * @author: huanw
     * @param: @param dictIds
     * @param: @return      
     * @return: boolean
     * @date:   2019年2月14日 下午2:49:12       
     * @throws
     */
    boolean checkIsLeafnode(@Param("list") List<String> dictIds);
    /**
     * 将字典占用的数据，封装给前端显示
     * @Title: getDictionaryOccupyData   
     * @Description: 将字典占用的数据，封装给前端显示
     * @author: huanw
     * @param: @param dictionaryList
     * @param: @return      
     * @return: Map<String,List<String>>
     * @date:   2019年2月15日 下午12:56:00       
     * @throws
     */
    Map<String,List<String>> getDictionaryOccupyData(List<DictionaryEntity> dictionaryList);
    /**
     * 通过字典id获取字典名称
     * @Title: getDictionaryNameByDictId   
     * @Description: TODO(这里用一句话描述这个方法的作用)   
     * @author: huanw
     * @param: @param dictId
     * @param: @return      
     * @return: String
     * @date:   2019年2月20日 下午3:17:00       
     * @throws
     */
    String getDictionaryNameByDictId(String dictId);
    /**
     * 根据名称查询该名称下的第一层分类，仅仅查一层
     * @Title: queryFirstLevelDictionaryByName   
     * @Description: 根据名称查询该名称下的第一层分类，仅仅查一层
     * @author: huanw
     * @param: @param name
     * @param: @param language
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年3月26日 上午11:26:07       
     * @throws
     */
    List<DictionaryEntity> queryFirstLevelDictionaryByName(String name,String language);
    /**
     * 获取课程分类
     * @Title: courseClsList   
     * @Description: 获取课程分类 
     * @author: huanw
     * @param: @param language
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年4月4日 上午10:29:18       
     * @throws
     */
    List<DictionaryEntity> courseClsList(String language);
    /**
     * 获取导师分类
     * @Title: professorClsList   
     * @Description: 获取导师分类   
     * @author: huanw
     * @param: @param language
     * @param: @return      
     * @return: List<DictionaryEntity>
     * @date:   2019年4月9日 上午10:25:59       
     * @throws
     */
    List<DictionaryEntity> professorClsList(String language);
}

