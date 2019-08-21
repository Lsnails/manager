package com.base.modules.business.system.storageb.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.storagea.BuyType;
import com.base.modules.business.system.storagea.UnitType;
import com.base.modules.business.system.storageb.dao.StoragebDao;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.business.system.storageb.service.StoragebService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("storagebService")
public class StoragebServiceImpl extends ServiceImpl<StoragebDao, StoragebEntity> implements StoragebService {

    @Override
    public Page<StoragebEntity> queryPage(Map<String, Object> params) {
        Page<StoragebEntity> page = this.selectPage(
                new Query<StoragebEntity>(params).getPage(),
                new EntityWrapper<StoragebEntity>()
        );

        return page;
    }

    @Override
    public void deleteStoragebByStorageAId(String storageaId) {
        if (StringUtils.isBlank(storageaId)) {
            return;
        }
        EntityWrapper<StoragebEntity> entityWrapper = new EntityWrapper<StoragebEntity>();
        entityWrapper.eq("storage_id", storageaId);
        this.delete(entityWrapper);
    }

    @Override
    public void insertBatchStoragebEntity(List<StoragebEntity> storagebVoList, String storagaId) {
        for (StoragebEntity storagebEntity : storagebVoList) {
            String uuId = UUIDUtils.getRandomUUID();
            storagebEntity.setId(uuId);
            storagebEntity.setStorageId(storagaId);
            storagebEntity.setCreateDate(new Date());
            storagebEntity.setUpdateDate(storagebEntity.getCreateDate());
        }

        this.insertBatch(storagebVoList);
    }

    @Override
    public void deleteStorageAandBInfoBystoragaIds(List<String> storagaIdList) {
        if (storagaIdList == null || storagaIdList.isEmpty()) {
            return;
        }
        EntityWrapper<StoragebEntity> entityWrapper = new EntityWrapper<StoragebEntity>();
        entityWrapper.in("storage_id", storagaIdList);
        this.delete(entityWrapper);
    }

    @Override
    public List<StoragebEntity> exportStorageBList(String storageAId) {
        if (StringUtils.isBlank(storageAId)) {
            return null;
        }
        EntityWrapper<StoragebEntity> entityWrapper = new EntityWrapper<StoragebEntity>();
        entityWrapper.eq("storage_id", storageAId);
        return this.selectList(entityWrapper);
    }

    @Override
    public List<Object[]> exportListB(String storageAId) {
        List<StoragebEntity> storagebEntities = this.exportStorageBList(storageAId);
        List<Object[]> data = getData(storagebEntities);
        return data;
    }

    /**
     * 拼装入库导出B表
     *
     * @param storagebEntities
     * @return
     */
    private List<Object[]> getData(List<StoragebEntity> storagebEntities) {
        List<Object[]> list = new ArrayList<>();
        for (StoragebEntity storagebEntity : storagebEntities) {
            Object[] b = new Object[15];
            b[0] = ContentUtils.getDateToString(storagebEntity.getDate(), "yyyy-MM-dd");
            b[1] = storagebEntity.getSupplier();
            b[2] = storagebEntity.getNumber();
            b[3] = storagebEntity.getAccept();
            b[4] = storagebEntity.getStorage();
            b[5] = BuyType.getDesc(storagebEntity.getBuyType());
            b[6] = storagebEntity.getMaterNumber();
            b[7] = storagebEntity.getMaterName();
            b[8] = UnitType.getDesc(storagebEntity.getUnit());
            b[9] = storagebEntity.getAmount();
            b[10] = storagebEntity.getUnitPrice();
            b[11] = storagebEntity.getPrice();
            b[12] = storagebEntity.getWarehouse();
            b[13] = "";
            list.add(b);
        }
        return list;
    }

}
