package com.base.modules.business.system.shipmenta.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmenta.dao.ShipmentaDao;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.utils.UUIDUtils;


@Service("shipmentaService")
public class ShipmentaServiceImpl extends ServiceImpl<ShipmentaDao, ShipmentaEntity> implements ShipmentaService {

	@Autowired
	private ShipmentbService shipmentbService;
	@Autowired
	private ShipmentcService shipmentcService;
	
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
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
	public void insertShipmentAandBAndC(ShipmentaEntity shipmentAVo, List<ShipmentbEntity> shipmentBVoList,
			List<ShipmentcEntity> shipmentCVoList) {
		Date impDate = shipmentAVo.getImpDate();
		Integer impType = shipmentAVo.getImpType();
		//第一步删除 相同日期和相同类型已经导入过的数据  
		this.deleteShipmentAandBAndCInfo(ContentUtils.getDateToString(impDate, "yyyy-MM-dd"), impType);
		//第二步 插入出库A表数据 、出库B表数据 、出库C表数据
		//1.插入出库A表数据
		String uuId=UUIDUtils.getRandomUUID();
		shipmentAVo.setId(uuId);
		this.insertShipmentaEntity(shipmentAVo);
		
		//2.插入出库B表数据
		String shipmentAId=uuId;
		shipmentbService.insertBatchShipmentBVo(shipmentBVoList, shipmentAId);
		//3.插入出库C表数据
		shipmentcService.insertBatchShipmentCvo(shipmentCVoList, shipmentAId);
	}


	@Override
	public void insertShipmentaEntity(ShipmentaEntity shipmentAVo) {
//		String uuId = UUIDUtils.getRandomUUID();
//		shipmentAVo.setId(uuId);
		shipmentAVo.setCreateDate(new Date());
        this.insert(shipmentAVo);
	}

	@Override
	public void deleteShipmentaVo(String impDate, Integer impType) {
		EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();
		if(StringUtils.isBlank(impDate) ||  impType ==null ){
			return;
		}
		entityWrapper.eq("imp_date", impDate);
		entityWrapper.eq("imp_type", impType);
		this.delete(entityWrapper);
	}

	@Override
	public void deleteShipmentAandBAndCInfo(String impDate, Integer impType) {
		//第一步：查询此数据是否已经导入过
		ShipmentaEntity queryShipmentaVo = this.queryShipmentaVo(impDate, impType);
		if(queryShipmentaVo == null) {
			return ;
		}
		//第二步  删除出库A表 、B表、C表信息
		//1，删除出库A表信息
		this.deleteShipmentaVo(impDate, impType);
		//2，删除出库B表信息
		String shipmentAId = queryShipmentaVo.getId();
		shipmentbService.deleteShipmentbVoByShipmentAId(shipmentAId);
		//3，删除出库C表信息
		shipmentcService.deleteShipmentCVoByShipmentAId(shipmentAId);
	}

	@Override
	public ShipmentaEntity queryShipmentaVo(String impDate, Integer impType) {
		EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();
		if(StringUtils.isBlank(impDate) ||  impType ==null ){
			return null;
		}
		entityWrapper.eq("imp_date", impDate);
		entityWrapper.eq("imp_type", impType);
		return this.selectOne(entityWrapper);
	}

	@Override
	public void deleteBatchShipmentAandBAndCInfo(List<String> shipmentAIdList) {
		//第一步  批量删除出库A表信息
		this.deleteBatchIds(shipmentAIdList);
		//第二步 批量删除出库B表信息
		shipmentbService.deleteBatchShipmentbVoByShipmentAIds(shipmentAIdList);
		//第三步 批量删除出库C表信息
		shipmentcService.deleteBatchShipmentCVoByShipmentAIds(shipmentAIdList);
	}

}
