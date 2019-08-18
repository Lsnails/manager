package com.base.modules.sys.redis;


import com.base.common.utils.RedisUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.common.utils.RedisKeys;
import com.base.modules.sys.entity.SysConfigEntity;

/**
 * 系统配置Redis
 * @date 2017/7/18 21:08
 */
@Component
public class SysConfigRedis {
    @Autowired
    private RedisUtils redisUtils;

    public void saveOrUpdate(SysConfigEntity config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        redisUtils.setObjToJson(key, config,RedisUtils.DEFAULT_EXPIRE);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisUtils.del(key);
    }

    public SysConfigEntity get(String configKey){
        String key = RedisKeys.getSysConfigKey(configKey);
        return redisUtils.getToObj(key, SysConfigEntity.class);
    }
}
