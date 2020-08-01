package com.base.modules.business.system.activityinfo.dao;

import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 活动管理表
 * 
 * @author huanw
 * @email 
 * @date 2019-10-15 15:48:42
 */
public interface ActivityinfoDao extends BaseMapper<ActivityinfoEntity> {
	int countNum(@Param("createBy") String createBy);
}
