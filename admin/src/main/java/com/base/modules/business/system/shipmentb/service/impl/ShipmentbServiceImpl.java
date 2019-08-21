package com.base.modules.business.system.shipmentb.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmentb.SaleType;
import com.base.modules.business.system.shipmentb.dao.ShipmentbDao;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    public void insertBatchShipmentBVo(List<ShipmentbEntity> shipmentBVoList, String shipmentAId) {
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
        if (StringUtils.isBlank(shipmentAId)) {
            return;
        }
        EntityWrapper<ShipmentbEntity> entityWrapper = new EntityWrapper<ShipmentbEntity>();
        entityWrapper.eq("shipment_id", shipmentAId);
        this.delete(entityWrapper);
    }

    @Override
    public void deleteBatchShipmentbVoByShipmentAIds(List<String> shipmentAIdList) {
        if (shipmentAIdList == null || shipmentAIdList.isEmpty()) {
            return;
        }
        EntityWrapper<ShipmentbEntity> entityWrapper = new EntityWrapper<ShipmentbEntity>();
        entityWrapper.in("shipment_id", shipmentAIdList);
        this.delete(entityWrapper);
    }

    @Override
    public List<ShipmentbEntity> exportShipmentBList(String shipmentAId) {
        if (StringUtils.isBlank(shipmentAId)) {
            return null;
        }
        EntityWrapper<ShipmentbEntity> entityWrapper = new EntityWrapper<ShipmentbEntity>();
        entityWrapper.eq("shipment_id", shipmentAId);
        return this.selectList(entityWrapper);
    }

    @Override
    public List<Object[]> exportListB(String id) {
        List<ShipmentbEntity> shipmentbEntities = this.exportShipmentBList(id);
        List<Object[]> data = getData(shipmentbEntities);
        return data;
    }

    /**
     * 拼装导出数据
     * @param listEntitys
     * @return
     */
    private List<Object[]> getData(List<ShipmentbEntity> listEntitys) {
        List<Object[]> list = new ArrayList<>();
        for (ShipmentbEntity entity : listEntitys) {
            Object[] b = new Object[18];
            b[0] = ContentUtils.getDateToString(entity.getDate(), "yyyy-MM-dd");
            b[1] = entity.getShopUnit();
            b[2] = entity.getNumber();
            b[3] = SaleType.getDesc(entity.getSaleType());
            b[4] = entity.getShip();
            b[5] = entity.getStorage();
            b[6] = entity.getSaleBussinessType();
            b[7] = entity.getProductCode();
            b[8] = entity.getProductName();
            b[9] = entity.getUnit();
            b[10] = entity.getAmount();
            b[11] = entity.getUnitCost();
            b[12] = entity.getCost();
            b[13] = entity.getRemark();
            b[14] = entity.getShipWarehouse();
            b[15] = entity.getWarehouse();
            b[16] = entity.getUnitPrice();
            b[17] = entity.getPrice();
            list.add(b);
        }
        return list;
    }

}
