package com.base.modules.business.system.storagea.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.Query;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import com.base.modules.business.system.config.entity.CodeEntity;
import com.base.modules.business.system.config.service.CodeService;
import com.base.modules.business.system.storagea.BuyType;
import com.base.modules.business.system.storagea.UnitType;
import com.base.modules.business.system.storagea.dao.StorageaDao;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.business.system.storageb.service.StoragebService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("storageaService")
public class StorageaServiceImpl extends ServiceImpl<StorageaDao, StorageaEntity> implements StorageaService {
    @Autowired
    private StoragebService storagebService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeNameRelationService codeNameRelationService;

    @Override
    public Page<StorageaEntity> queryPage(Map<String, Object> params) {
        EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();

        if (params != null) {
            String searchDate = (String) params.get("searchDate");
            if (StringUtils.isNotBlank(searchDate)) {
                entityWrapper.eq("apply_date", searchDate);
            }
        }
        entityWrapper.orderBy(" apply_date desc ");
        Page<StorageaEntity> page = this.selectPage(
                new Query<StorageaEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void insertStorageaVoAndStoragebList(StorageaEntity storageaEntity, List<StoragebEntity> storagebVoList, String applyDate) {

        //第一步删除  --- 先删除  此日期已经导入过的数据
        this.deleteStorageAandBInfo(applyDate);

        //第二新增 入库A表数据
        String uuId = UUIDUtils.getRandomUUID();
        storageaEntity.setId(uuId);
        this.insertStorageaEntity(storageaEntity);

        //第三步 批量新增 入库B表数据
        storagebService.insertBatchStoragebEntity(storagebVoList, storageaEntity.getId());
    }

    @Override
    public String getCode(String date, int type) {
        String code = "";
        CodeEntity codeEntity = codeService.queryCodeEntityByDate(date);
        if (codeEntity.getCode() >= 100) {
            if (type == 0) {
                code = "WIN000" + codeEntity.getCode();
            } else {
                code = "XOUT000" + codeEntity.getCode();
            }
        } else if (codeEntity.getCode() >= 10 && codeEntity.getCode() < 100) {
            if (type == 0) {
                code = "WIN0000" + codeEntity.getCode();
            } else {
                code = "XOUT0000" + codeEntity.getCode();
            }
        } else {
            if (type == 0) {
                code = "WIN00000" + codeEntity.getCode();
            } else {
                code = "XOUT00000" + codeEntity.getCode();
            }
        }
        return code;
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            List<List<String>> readList = ExcelReaderUtil.readExcel(file.getInputStream());
            String date = ExcelReaderUtil.getStorageDate(readList);
            List<List<String>> storageBData = ExcelReaderUtil.getStorageData(readList);
            List<StoragebEntity> list = getList(storageBData, date);
            StorageaEntity storageaEntity = new StorageaEntity();
            storageaEntity.setApplyDate(ContentUtils.getStringToDate(date));
            storageaEntity.setApplyName(file.getOriginalFilename());
            storageaEntity.setOutCode(list.get(0).getNumber());
            this.insertStorageaVoAndStoragebList(storageaEntity, list, date);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<StoragebEntity> getList(List<List<String>> list, String date) {
        List<StoragebEntity> rList = new ArrayList<>();
        CodeEntity codeEntity = codeService.queryCodeEntityByDate(date);
        for (List<String> strings : list) {
            StoragebEntity entity = new StoragebEntity();
            entity.setDate(ContentUtils.getStringToDate(date));
            entity.setSupplier("新飞制冷器具有限公司");
            if (codeEntity.getCode() >= 100) {
                entity.setNumber("WIN000" + codeEntity.getCode());
            } else if (codeEntity.getCode() >= 10 && codeEntity.getCode() < 100) {
                entity.setNumber("WIN0000" + codeEntity.getCode());
            } else {
                entity.setNumber("WIN00000" + codeEntity.getCode());
            }
            entity.setAccept("XX");
            entity.setStorage("XX");
            entity.setBuyType(BuyType.T1.getCode());
            List<String> relationNameAndCode = codeNameRelationService.getRelationNameAndCode(strings.get(1));
            if (null != relationNameAndCode && relationNameAndCode.size() > 0) {
                entity.setMaterNumber(relationNameAndCode.get(0));
                entity.setMaterName(relationNameAndCode.get(1));
            } else {
                entity.setMaterNumber("未知");
                entity.setMaterName("未知");
            }
            entity.setUnit(UnitType.T1.getCode());
            Integer number = Integer.valueOf(strings.get(2)).intValue();
            Double unitPrice = Double.valueOf(strings.get(3)).doubleValue();
            BigDecimal price = new BigDecimal(number * unitPrice);
            entity.setAmount(Integer.valueOf(strings.get(2)));
            entity.setUnitPrice(new BigDecimal(unitPrice));
            entity.setPrice(price);
            entity.setWarehouse("新飞一等品");
            rList.add(entity);
        }
        return rList;
    }

    @Override
    public void insertStorageaEntity(StorageaEntity storageaEntity) {
        //第二步  新增入库A表数据
        storageaEntity.setCreateDate(new Date());
        this.insert(storageaEntity);
    }

    @Override
    public StorageaEntity queryStorageaEntityByapplyDate(String applyDate) {
        if (applyDate == null || "".equals(applyDate)) {
            return null;
        }
        EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
        entityWrapper.eq("apply_date", applyDate);
        return this.selectOne(entityWrapper);
    }

    @Override
    public StorageaEntity queryStorageaById(String id) {
        if (StringUtils.isNotBlank(id)) {
            //EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
            //entityWrapper.eq("id", id);
            return this.selectById(id);
        }
        return null;
    }

    @Override
    public void deleteStorageAandBInfo(String applyDate) {
        //第一步  查询该日期的数据是否已经导入过
        StorageaEntity storageaEntity = this.queryStorageaEntityByapplyDate(applyDate);
        if (storageaEntity == null) {
            return;
        }
        //第二步 删除 入库A表信息
        this.deleteStorageA(applyDate);
        //第三删除 入库B表信息
        storagebService.deleteStoragebByStorageAId(storageaEntity.getId());
    }

    @Override
    public void deleteStorageA(String applyDate) {
        if (StringUtils.isBlank(applyDate)) {
            return;
        }
        EntityWrapper<StorageaEntity> entityWrapper = new EntityWrapper<StorageaEntity>();
        entityWrapper.eq("apply_date", applyDate);
        this.delete(entityWrapper);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStorageAandBInfo(List<String> idList) {
        //第一 批量删除入库A表
        this.deleteBatchIds(idList);
        //第二 批量删除入库B表信息
        storagebService.deleteStorageAandBInfoBystoragaIds(idList);
    }


}
