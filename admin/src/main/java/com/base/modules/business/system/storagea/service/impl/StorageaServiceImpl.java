package com.base.modules.business.system.storagea.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;

import com.base.modules.business.system.storagea.dao.StorageaDao;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.customizesys.helper.ContentUtils;


@Service("storageaService")
public class StorageaServiceImpl extends ServiceImpl<StorageaDao, StorageaEntity> implements StorageaService {

    @Override
    public Page<StorageaEntity> queryPage(Map<String, Object> params) {
    	EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
    	entityWrapper.orderBy(" apply_date desc ");
        Page<StorageaEntity> page = this.selectPage(
                new Query<StorageaEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

	@Override
	public void insertStorageaEntity(StorageaEntity storageaEntity) {
		//第一步  先删除  此日期已经导入过的数据
		Date applyDate = storageaEntity.getApplyDate();
		this.deleteStorageAandBInfo(ContentUtils.getDateToString(applyDate,"yyyy-MM-dd"));
		//第二步  新增入库A表数据
		storageaEntity.setCreateDate(new Date());
		this.insert(storageaEntity);
	}

	@Override
	public StorageaEntity quweyStorageaEntityByapplyDate(String applyDate) {
		 if(applyDate == null || "".equals(applyDate)) {
			 return null;
		 }
		 EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
		 entityWrapper.eq("apply_date", applyDate);
		 return  this.selectOne(entityWrapper);
	}

	@Override
	public void deleteStorageAandBInfo(String applyDate) {
		//第一步  查询该日期的数据是否已经导入过
		StorageaEntity storageaEntity = this.quweyStorageaEntityByapplyDate(applyDate);
		if(storageaEntity == null) {
			return;
		}
		//第二步 删除 入库A表信息
		this.deleteStorageA(applyDate);
		//第三删除 入库B表信息
		//TODO
	}

	@Override
	public void deleteStorageA(String applyDate) {
		if(StringUtils.isBlank(applyDate)){
			return ;
		}
		EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
		entityWrapper.eq("apply_date", applyDate);
		this.delete(entityWrapper);
	}

}
