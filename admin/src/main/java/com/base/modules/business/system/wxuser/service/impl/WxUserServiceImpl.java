package com.base.modules.business.system.wxuser.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.PageUtils;
import com.base.common.utils.Query;

import com.base.modules.business.system.wxuser.dao.WxUserDao;
import com.base.modules.business.system.wxuser.entity.WxUserEntity;
import com.base.modules.business.system.wxuser.service.WxUserService;


@Service("wxUserService")
public class WxUserServiceImpl extends ServiceImpl<WxUserDao, WxUserEntity> implements WxUserService {

    @Override
    public Page<WxUserEntity> queryPage(Map<String, Object> params) {
        Page<WxUserEntity> page = this.selectPage(
                new Query<WxUserEntity>(params).getPage(),
                new EntityWrapper<WxUserEntity>()
        );

        return page;
    }

}
