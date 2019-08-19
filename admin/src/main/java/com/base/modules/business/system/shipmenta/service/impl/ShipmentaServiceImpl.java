package com.base.modules.business.system.shipmenta.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmenta.dao.ShipmentaDao;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;


@Service("shipmentaService")
public class ShipmentaServiceImpl extends ServiceImpl<ShipmentaDao, ShipmentaEntity> implements ShipmentaService {

    @Override
    public Page<ShipmentaEntity> queryPage(Map<String, Object> params) {
    	EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();

    	if(params!=null) {
    		String searchDate = (String)params.get("searchDate");
    		if(StringUtils.isNotBlank(searchDate)) {
    			entityWrapper.eq("imp_date", searchDate);
    		}
    	}
    	entityWrapper.orderBy(" imp_date desc ");
        Page<ShipmentaEntity> page = this.selectPage(
                new Query<ShipmentaEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

}
