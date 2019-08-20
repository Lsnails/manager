package com.base.modules.business.system.shipmentc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmentc.dao.ShipmentcDao;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.utils.UUIDUtils;


@Service("shipmentcService")
public class ShipmentcServiceImpl extends ServiceImpl<ShipmentcDao, ShipmentcEntity> implements ShipmentcService {

    @Override
    public Page<ShipmentcEntity> queryPage(Map<String, Object> params) {
        Page<ShipmentcEntity> page = this.selectPage(
                new Query<ShipmentcEntity>(params).getPage(),
                new EntityWrapper<ShipmentcEntity>()
        );

        return page;
    }

	@Override
	public void insertBatchShipmentCvo(List<ShipmentcEntity> shipmentCVoList, String shipmentAId) {
		for (ShipmentcEntity shipmentcEntity : shipmentCVoList) {
			String uuId = UUIDUtils.getRandomUUID();
			shipmentcEntity.setId(uuId);
			shipmentcEntity.setShipmentId(shipmentAId);
			shipmentcEntity.setCreateDate(new Date());
			shipmentcEntity.setUpdateDate(shipmentcEntity.getCreateDate());
		}
		this.insertBatch(shipmentCVoList);
	}

	@Override
	public void deleteShipmentCVoByShipmentAId(String shipmentAId) {
		if(StringUtils.isBlank(shipmentAId)){
			return;
		}
		EntityWrapper<ShipmentcEntity> entityWrapper = new EntityWrapper<ShipmentcEntity>();
		entityWrapper.eq("shipment_id", shipmentAId);
		this.delete(entityWrapper);
	}

	@Override
	public void deleteBatchShipmentCVoByShipmentAIds(List<String> shipmentAIdList) {
		if(shipmentAIdList == null || shipmentAIdList.isEmpty()){
			return;
		}
		EntityWrapper<ShipmentcEntity> entityWrapper = new EntityWrapper<ShipmentcEntity>();
		entityWrapper.in("shipment_id", shipmentAIdList);
		this.delete(entityWrapper);
	}

}
