package com.base.modules.business.system.storagea.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.utils.UUIDUtils;

/**
 * 入库A表存储
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 20:43:16
 */
public interface StorageaService extends IService<StorageaEntity> {

    Page<StorageaEntity> queryPage(Map<String, Object> params);
    
    
    /**
     * 新增入库A表导入信息
     * @param storageaEntity
     */
    void insertStorageaEntity (StorageaEntity storageaEntity);
    /**
     * 通过applyDate 日期查询 是否已经导入过，
     * @param applyDate
     * @return
     */
    StorageaEntity quweyStorageaEntityByapplyDate(String applyDate);
//    /**
//     * 通过applyDate日期查询导入A表信息，是否 第一次导入
//     * 返回  true 表示  
//     * @param applyDate
//     * @return
//     */
//    boolean isFristImport(String applyDate);
    /**
     * 删除StorageA 和 StorageB
     * @param applyDate
     */
     void  deleteStorageAandBInfo(String applyDate);
     /**
      * 删除StorageA，通过applyDate
      * @param applyDate
      */
     void deleteStorageA(String applyDate);
}

