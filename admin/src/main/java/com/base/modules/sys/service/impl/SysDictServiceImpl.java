package com.base.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.sys.dao.SysDictDao;
import com.base.modules.sys.entity.SysDictEntity;
import com.base.modules.sys.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {
	
	@Cacheable(value="sysDictQuery",key="123") //ehcache缓存测试
    @Override
    public Page<SysDictEntity> queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<SysDictEntity> page = this.selectPage(
                new Query<SysDictEntity>(params).getPage(),
                new EntityWrapper<SysDictEntity>()
                    .like(StringUtils.isNotBlank(name),"name", name)
        );

        return page;
    }
	
	@CacheEvict(value="sysDictQuery",key="123")
	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		System.out.println("正在清空缓存");
	}

}
