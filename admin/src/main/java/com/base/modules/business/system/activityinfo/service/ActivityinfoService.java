package com.base.modules.business.system.activityinfo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 活动管理表
 *
 * @author huanw
 * @email 
 * @date 2019-10-15 15:48:42
 */
public interface ActivityinfoService extends IService<ActivityinfoEntity> {

    Page<ActivityinfoEntity> queryPage(Map<String, Object> params,ActivityinfoEntity activityinfo);

    List<ActivityinfoEntity> getListByStates();
}

