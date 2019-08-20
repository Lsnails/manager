package com.base.modules.business.system.shipmentb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmentb.dao.ShipmentbDao;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.utils.UUIDUtils;


@Service("shipmentbService")
public class ShipmentbServiceImpl extends ServiceImpl<ShipmentbDao, ShipmentbEntity> implements ShipmentbService {

    @Override
    public Page<ShipmentbEntity> queryPage(Map<String, Object> params) {
        Page<ShipmentbEntity> page = this.selectPage(
                new Query<ShipmentbEntity>(params).getPage(),
                new EntityWrapper<ShipmentbEntity>()
        );

        return page;
    }

	@Override
	public void insertBatchShipmentBVo(List<ShipmentbEntity> shipmentBVoList,String shipmentAId) {
		for (ShipmentbEntity shipmentbEntity : shipmentBVoList) {
			String uuId = UUIDUtils.getRandomUUID();
			shipmentbEntity.setId(uuId);
			shipmentbEntity.setCreateDate(new Date());
			shipmentbEntity.setShipmentId(shipmentAId);
			shipmentbEntity.setUpdateDate(shipmentbEntity.getCreateDate());
		}
		this.insertBatch(shipmentBVoList);
	}

	@Override
	public void deleteShipmentbVoByShipmentAId(String shipmentAId) {
		if(StringUtils.isBlank(shipmentAId)) {
			return ;
		}
		EntityWrapper<ShipmentbEntity> entityWrapper = new EntityWrapper<ShipmentbEntity>();
		entityWrapper.eq("shipment_id", shipmentAId);
		this.delete(entityWrapper);
	}

	@Override
	public void deleteBatchShipmentbVoByShipmentAIds(List<String> shipmentAIdList) {
		if(shipmentAIdList == null || shipmentAIdList.isEmpty()) {
			return ;
		}
		EntityWrapper<ShipmentbEntity> entityWrapper = new EntityWrapper<ShipmentbEntity>();
		entityWrapper.in("shipment_id", shipmentAIdList);
		this.delete(entityWrapper);
	}

}
