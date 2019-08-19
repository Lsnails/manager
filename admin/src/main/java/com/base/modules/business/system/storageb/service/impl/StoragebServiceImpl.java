package com.base.modules.business.system.storageb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.storageb.dao.StoragebDao;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.business.system.storageb.service.StoragebService;
import com.base.utils.UUIDUtils;


@Service("storagebService")
public class StoragebServiceImpl extends ServiceImpl<StoragebDao, StoragebEntity> implements StoragebService {

    @Override
    public Page<StoragebEntity> queryPage(Map<String, Object> params) {
        Page<StoragebEntity> page = this.selectPage(
                new Query<StoragebEntity>(params).getPage(),
                new EntityWrapper<StoragebEntity>()
        );

        return page;
    }

	@Override
	public void deleteStoragebByStorageAId(String storageaId) {
		if(StringUtils.isBlank(storageaId)){
			return ;
		}
		EntityWrapper<StoragebEntity> entityWrapper = new EntityWrapper<StoragebEntity>();
		entityWrapper.eq("storage_id", storageaId);
		this.delete(entityWrapper);
	}

	@Override
	public void insertBatchStoragebEntity(List<StoragebEntity> storagebVoList,String storagaId) {
		for (StoragebEntity storagebEntity : storagebVoList) {
			String uuId = UUIDUtils.getRandomUUID();
			storagebEntity.setId(uuId);
			storagebEntity.setStorageId(storagaId);
			storagebEntity.setCreateDate(new Date());
			storagebEntity.setUpdateDate(storagebEntity.getCreateDate());
		}
		
		this.insertBatch(storagebVoList);
	}

}
