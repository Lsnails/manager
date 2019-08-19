package com.base.modules.business.system.shipmentb.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;

/**
 * 出库B表数据
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:17
 */
public interface ShipmentbService extends IService<ShipmentbEntity> {

    Page<ShipmentbEntity> queryPage(Map<String, Object> params);
    
    /**
     * 批量插入出库B表信息
     * @param shipmentBVoList  对象集合
     * @param shipmentAId	   与出库A表关联
     */
    void insertBatchShipmentBVo(List<ShipmentbEntity> shipmentBVoList,String shipmentAId);
    /**
     * 按照shipment_id 删除出库B表数据
     * @param shipmentAId
     */
    void  deleteShipmentbVoByShipmentAId(String shipmentAId);
}

