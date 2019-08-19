package com.base.modules.business.system.shipmenta.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;

/**
 * 出库A表存储
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:11:58
 */
public interface ShipmentaService extends IService<ShipmentaEntity> {

    Page<ShipmentaEntity> queryPage(Map<String, Object> params);
}

