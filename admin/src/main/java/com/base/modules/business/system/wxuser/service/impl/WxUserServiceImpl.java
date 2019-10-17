package com.base.modules.business.system.wxuser.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.wxuser.dao.WxUserDao;
import com.base.modules.business.system.wxuser.entity.WxUserEntity;
import com.base.modules.business.system.wxuser.service.WxUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wxUserService")
public class WxUserServiceImpl extends ServiceImpl<WxUserDao, WxUserEntity> implements WxUserService {

    @Override
    public Page<WxUserEntity> queryPage(Map<String, Object> params,WxUserEntity wxUser) {
    	EntityWrapper<WxUserEntity> entityWrapper = new EntityWrapper<WxUserEntity>();
    	if(wxUser!=null) {
    		if(StringUtils.isNotBlank(wxUser.getActivityId())) {
    			entityWrapper.like("activity_id", wxUser.getActivityId());
    		}
    		if(StringUtils.isNotBlank(wxUser.getPhone())) {
    			entityWrapper.like("phone", wxUser.getPhone());
    		}
    		if(StringUtils.isNotBlank(wxUser.getNetworkName())) {
    			entityWrapper.like("network_name", wxUser.getNetworkName());
    		}
    		if(StringUtils.isNotBlank(wxUser.getUserCode())) {
    			entityWrapper.like("user_code", wxUser.getUserCode());
    		}
    	}
        Page<WxUserEntity> page = this.selectPage(
                new Query<WxUserEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

	@Override
	public WxUserEntity getUserInfo(String openId, String activityId) {
		EntityWrapper<WxUserEntity> entityWrapper = new EntityWrapper();
		entityWrapper.eq("open_id", openId);
		entityWrapper.eq("activity_id", activityId);
		WxUserEntity wxUserEntity = this.selectOne(entityWrapper);
		return wxUserEntity;
	}

	@Override
	public WxUserEntity selectOne(Wrapper<WxUserEntity> wrapper) {
		return super.selectOne(wrapper);
	}
}
