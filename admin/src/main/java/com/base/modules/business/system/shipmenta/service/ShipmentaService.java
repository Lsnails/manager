package com.base.modules.business.system.shipmenta.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 出库A表存储
 *
 * @author huanw
 * @email
 * @date 2019-08-19 15:11:58
 */
public interface ShipmentaService extends IService<ShipmentaEntity> {

    Page<ShipmentaEntity> queryPage(Map<String, Object> params);

    /**
     * 新增出库A and B and C的数据
     *
     * @param shipmentAVo     出库A表信息 需要包含imp_date 和 imp_type
     * @param shipmentBVoList 出库B的集合
     * @param shipmentCVoList 出库C的集合
     */
    void insertShipmentAandBAndC(ShipmentaEntity shipmentAVo, List<ShipmentbEntity> shipmentBVoList, List<ShipmentcEntity> shipmentCVoList);

    /**
     * 插入出库A表数据
     *
     * @param shipmentAVo
     */
    void insertShipmentaEntity(ShipmentaEntity shipmentAVo);

    /**
     * 删除出库A表信息
     *
     * @param impDate
     * @param impType
     */
    void deleteShipmentaVo(String impDate, Integer impType);

    /**
     * 删除出库AandBandC的数据
     *
     * @param impDate
     * @param impType
     */
    void deleteShipmentAandBAndCInfo(String impDate, Integer impType);

    /**
     * 按照导入日期和类型（天猫、淘宝等）查询出库A是否存在
     *
     * @param impDate
     * @param impType
     * @return
     */
    ShipmentaEntity queryShipmentaVo(String impDate, Integer impType);

    /**
     * 批量删除出库AandBandC的数据
     *
     * @param shipmentAIdList
     */
    void deleteBatchShipmentAandBAndCInfo(List<String> shipmentAIdList);

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    ShipmentaEntity queryEntityById(String id);

    void importData(MultipartFile file, String impType, String shopUnit);

}

