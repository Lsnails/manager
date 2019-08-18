package com.base.modules.sys.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.base.modules.sys.entity.SysConfigEntity;

import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息
 * 
 * @date 2016年12月4日 下午6:46:16
 */
public interface SysConfigDao extends BaseMapper<SysConfigEntity> {

	/**
	 * 根据key，查询value
	 */
	SysConfigEntity queryByKey(String paramKey);

	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);
	
}
