package com.base.modules.customizesys.dictionary.dao;



import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;

/**
 * 业务数据字典包含多层次数据
 * 
 * @author huanw
 * @email 
 * @date 2019-02-11 17:09:10
 */
public interface DictionaryDao extends BaseMapper<DictionaryEntity> {
	/**
	 * 查询被删除的体系是否被占用，如果占用不能删除
	 * @Title: queryDictionaryIsOccupy   
	 * @Description: 查询被删除的体系是否被占用，如果占用不能删除
	 * @author: huanw
	 * @param: @param dictIdList
	 * @param: @return      
	 * @return: List<DictionaryEntity>
	 * @date:   2019年2月14日 下午3:19:52       
	 * @throws
	 */
	 List<DictionaryEntity> queryDictionaryIsOccupy(@Param("list") List<String> dictIdList);
}
