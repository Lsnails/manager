package com.base.modules.business.system.datainfo.dao;

import com.base.modules.business.system.datainfo.entity.BuyInfoEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * 
 * @author huanw
 * @email 
 * @date 2020-03-02 13:48:17
 */
public interface BuyInfoDao extends BaseMapper<BuyInfoEntity> {
	
	List<Map<String,Object>> queryBuyInfoByType(@Param("type") String  type);
}
