package com.base.modules.business.system.shipmentc.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;

/**
 * 出库C表数据
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:57
 */
public interface ShipmentcService extends IService<ShipmentcEntity> {

    Page<ShipmentcEntity> queryPage(Map<String, Object> params);
    /**
     * 批量插入出库C表数据
     * @param ShipmentCVoList 对象集合
     * @param shipmentAId	与出库A表关联id
     */
    void insertBatchShipmentCvo(List<ShipmentcEntity> shipmentCVoList,String shipmentAId);
    
    /**
     * 按照shipment_id 删除出库B表数据
     * @param shipmentAId
     */
    void  deleteShipmentCVoByShipmentAId(String shipmentAId);
    /**
     * 按照shipmentAIdList 批量删除出库B表数据
     * @param shipmentAIdList
     */
    void  deleteBatchShipmentCVoByShipmentAIds(List<String> shipmentAIdList);
    /**
     * 导出出库C的数据
     * @param shipmentAId
     * @return
     */
    List<ShipmentcEntity> exportShipmentCList(String shipmentAId);

    List<Object[]> exportListC(String id);
}

