package com.base.modules.business.system.storagea.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.storagea.dao.StorageaDao;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.business.system.storageb.service.StoragebService;
import com.base.utils.UUIDUtils;


@Service("storageaService")
public class StorageaServiceImpl extends ServiceImpl<StorageaDao, StorageaEntity> implements StorageaService {

	
	@Autowired
	private StoragebService storagebService;
	
    @Override
    public Page<StorageaEntity> queryPage(Map<String, Object> params) {
    	EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
    	
    	if(params!=null) {
    		String searchDate = (String)params.get("searchDate");
    		if(StringUtils.isNotBlank(searchDate)) {
    			entityWrapper.eq("apply_date", searchDate);
    		}
    	}
    	entityWrapper.orderBy(" apply_date desc ");
        Page<StorageaEntity> page = this.selectPage(
                new Query<StorageaEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }
    
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
	public void insertStorageaVoAndStoragebList(StorageaEntity storageaEntity, List<StoragebEntity> storagebVoList,String applyDate) {
		
    	//第一步删除  --- 先删除  此日期已经导入过的数据
    	this.deleteStorageAandBInfo(applyDate);
    	
    	//第二新增 入库A表数据
    	String uuId = UUIDUtils.getRandomUUID();
    	storageaEntity.setId(uuId);
    	this.insertStorageaEntity(storageaEntity);
		
    	//第三步 批量新增 入库B表数据
    	storagebService.insertBatchStoragebEntity(storagebVoList, storageaEntity.getId());
	}

	@Override
	public void insertStorageaEntity(StorageaEntity storageaEntity) {
		//第二步  新增入库A表数据
		storageaEntity.setCreateDate(new Date());
		this.insert(storageaEntity);
	}

	@Override
	public StorageaEntity queryStorageaEntityByapplyDate(String applyDate) {
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
		StorageaEntity storageaEntity = this.queryStorageaEntityByapplyDate(applyDate);
		if(storageaEntity == null) {
			return;
		}
		//第二步 删除 入库A表信息
		this.deleteStorageA(applyDate);
		//第三删除 入库B表信息
		storagebService.deleteStoragebByStorageAId(storageaEntity.getId());
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
