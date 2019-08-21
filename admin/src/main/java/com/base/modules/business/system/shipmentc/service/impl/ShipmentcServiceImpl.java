package com.base.modules.business.system.shipmentc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.shipmentc.dao.ShipmentcDao;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
        if (StringUtils.isBlank(shipmentAId)) {
            return;
        }
        EntityWrapper<ShipmentcEntity> entityWrapper = new EntityWrapper<ShipmentcEntity>();
        entityWrapper.eq("shipment_id", shipmentAId);
        this.delete(entityWrapper);
    }

    @Override
    public void deleteBatchShipmentCVoByShipmentAIds(List<String> shipmentAIdList) {
        if (shipmentAIdList == null || shipmentAIdList.isEmpty()) {
            return;
        }
        EntityWrapper<ShipmentcEntity> entityWrapper = new EntityWrapper<ShipmentcEntity>();
        entityWrapper.in("shipment_id", shipmentAIdList);
        this.delete(entityWrapper);
    }

    @Override
    public List<ShipmentcEntity> exportShipmentCList(String shipmentAId) {
        if (StringUtils.isBlank(shipmentAId)) {
            return null;
        }
        EntityWrapper<ShipmentcEntity> entityWrapper = new EntityWrapper<ShipmentcEntity>();
        entityWrapper.eq("shipment_id", shipmentAId);
        return this.selectList(entityWrapper);
    }

    @Override
    public List<Object[]> exportListC(String id) {
        List<ShipmentcEntity> shipmentcEntities = this.exportShipmentCList(id);
        List<Object[]> data = getData(shipmentcEntities);
        return data;
    }

    /**
     * 拼装出库C表数据
     *
     * @param listEntitys
     * @return
     */
    private List<Object[]> getData(List<ShipmentcEntity> listEntitys) {
        List<Object[]> list = new ArrayList<>();
        for (ShipmentcEntity entity : listEntitys) {
            Object[] b = new Object[18];
            b[0] = entity.getSerialNumber();
            b[1] = entity.getOrderId();
            b[2] = entity.getShipmentOrderId();
            b[3] = entity.getOrderName();
            b[4] = entity.getOrderPhone();
            b[5] = entity.getOrderAddress();
            b[6] = entity.getYsNature();
            b[7] = entity.getPayType();
            b[8] = "";
            b[9] = entity.getProductName();
            b[10] = entity.getNumber();
            b[11] = entity.getPrice();
            b[12] = "";
            b[13] = "";
            b[14] = "";
            b[15] = "";
            b[16] ="";
            list.add(b);
        }
        return list;
    }

}
