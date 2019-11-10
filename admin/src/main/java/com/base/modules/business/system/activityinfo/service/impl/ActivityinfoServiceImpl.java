package com.base.modules.business.system.activityinfo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.activityinfo.dao.ActivityinfoDao;
import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;
import com.base.modules.business.system.activityinfo.service.ActivityinfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("activityinfoService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class ActivityinfoServiceImpl extends ServiceImpl<ActivityinfoDao, ActivityinfoEntity> implements ActivityinfoService {

    @Override
    public Page<ActivityinfoEntity> queryPage(Map<String, Object> params,ActivityinfoEntity activityinfo) {
    	EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper<ActivityinfoEntity>();
    	entityWrapper.orderBy("createtime desc");
    	if(activityinfo!=null) {
    		if(StringUtils.isNotBlank(activityinfo.getName())) {
    			entityWrapper.like("name", activityinfo.getName());
    		}
    	}
        Page<ActivityinfoEntity> page = this.selectPage(
                new Query<ActivityinfoEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

    /**
     * 只显示启用的活动
     */
    @Override
    public List<ActivityinfoEntity> getListByStates() {
        EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper<ActivityinfoEntity>();
        entityWrapper.eq("status",1);
        return this.selectList(entityWrapper);
    }
    
    @Override
    public ActivityinfoEntity selectOne(Wrapper<ActivityinfoEntity> wrapper) {
    	// TODO Auto-generated method stub
    	ActivityinfoEntity selectOne = super.selectOne(wrapper);
    	return selectOne;
    }

}
