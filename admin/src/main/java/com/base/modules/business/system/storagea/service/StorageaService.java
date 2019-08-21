package com.base.modules.business.system.storagea.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
     *
     * @param storageaEntity
     */
    void insertStorageaEntity(StorageaEntity storageaEntity);

    /**
     * 通过applyDate 日期查询 是否已经导入过，
     *
     * @param applyDate
     * @return
     */
    StorageaEntity queryStorageaEntityByapplyDate(String applyDate);

    StorageaEntity queryStorageaById(String id);
//    /**
//     * 通过applyDate日期查询导入A表信息，是否 第一次导入
//     * 返回  true 表示  
//     * @param applyDate
//     * @return
//     */
//    boolean isFristImport(String applyDate);

    /**
     * 删除StorageA 和 StorageB
     *
     * @param applyDate
     */
    void deleteStorageAandBInfo(String applyDate);

    /**
     * 删除StorageA，通过applyDate
     *
     * @param applyDate
     */
    void deleteStorageA(String applyDate);

    /**
     * 插入入库A表信息 和入库B表信息（插入之前先删除 当天已经导入过的数据）
     *
     * @param storageaEntity
     * @param storagebVoList
     * @param applyDate      格式yyyy-MM-dd
     */
    void insertStorageaVoAndStoragebList(StorageaEntity storageaEntity, List<StoragebEntity> storagebVoList, String applyDate);

    /**
     * 通过入库A表Ids删除 入库A and 入库B
     *
     * @param asList
     */
    void deleteStorageAandBInfo(List<String> asList);

    /**
     * 获取编号,根据不同的类型获取不同的编号
     * 入库和出库
     *
     * @param date
     * @param type
     * @return
     */
    String getCode(String date, int type);

    /**
     * 入库导入数据
     */
    void importData(MultipartFile file);
}

