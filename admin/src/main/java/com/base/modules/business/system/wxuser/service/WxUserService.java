package com.base.modules.business.system.wxuser.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.wxuser.entity.WxUserEntity;

import java.util.Map;

/**
 * 
 *
 * @author huanw
 * @email 
 * @date 2019-10-15 10:12:07
 */
public interface WxUserService extends IService<WxUserEntity> {

    Page<WxUserEntity> queryPage(Map<String, Object> params,WxUserEntity wxUser);

    WxUserEntity getUserInfo(String openId,String activityId);

    WxUserEntity getUserByParam(String txt,String activityId);
}

