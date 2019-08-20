package com.base.modules.business.system.storageb.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.storageb.entity.StoragebEntity;

/**
 * 入库生成B表
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 10:50:46
 */
public interface StoragebService extends IService<StoragebEntity> {

    Page<StoragebEntity> queryPage(Map<String, Object> params);
    
    /**
     * 删除入库B表数据，通过与入库A表关联的storageId删除
     * @param storageaId
     */
    void deleteStoragebByStorageAId(String storageaId);
    
    /**
     * 批量新增入库B表数据（B表的uuid方法里面已经生成）
     * storagebVoList: 表示对象集合
     * storagaId：表示与入库A表的关系
     * @param storagebVoList
     */
    void insertBatchStoragebEntity(List<StoragebEntity> storagebVoList,String storagaId);
    /**
     * 通过入库A表的id集合删除 入库B表信息
     * @param storagaIdList
     */
    void deleteStorageAandBInfoBystoragaIds(List<String> storagaIdList);
    
    /**
     * 导出入库B表数据
     * @param storageAId
     * @return
     */
    List<StoragebEntity> exportStorageBList(String storageAId);
}

