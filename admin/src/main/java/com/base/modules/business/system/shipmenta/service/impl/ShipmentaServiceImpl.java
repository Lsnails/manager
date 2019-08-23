package com.base.modules.business.system.shipmenta.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.Query;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import com.base.modules.business.system.shipmenta.ShipmentShopType;
import com.base.modules.business.system.shipmenta.ShipmentType;
import com.base.modules.business.system.shipmenta.dao.ShipmentaDao;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.business.system.shipmentb.SaleType;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.modules.business.system.storagea.service.StorageaService;
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


@Service("shipmentaService")
public class ShipmentaServiceImpl extends ServiceImpl<ShipmentaDao, ShipmentaEntity> implements ShipmentaService {

    @Autowired
    private ShipmentbService shipmentbService;
    @Autowired
    private ShipmentcService shipmentcService;
    @Autowired
    private StorageaService storageaService;
    @Autowired
    private CodeNameRelationService codeNameRelationService;

    @Override
    public Page<ShipmentaEntity> queryPage(Map<String, Object> params) {
        EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();

        if (params != null) {
            String searchDate = (String) params.get("searchDate");
            if (StringUtils.isNotBlank(searchDate)) {
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
        Integer shopType = shipmentAVo.getShopType();
        //第一步删除 相同日期和相同类型已经导入过的数据
        this.deleteShipmentAandBAndCInfo(ContentUtils.getDateToString(impDate, "yyyy-MM-dd"), impType,shopType);
        //第二步 插入出库A表数据 、出库B表数据 、出库C表数据
        //1.插入出库A表数据
        String uuId = UUIDUtils.getRandomUUID();
        shipmentAVo.setId(uuId);
        this.insertShipmentaEntity(shipmentAVo);

        //2.插入出库B表数据
        String shipmentAId = uuId;
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
    public void deleteShipmentaVo(String impDate, Integer impType,Integer shopType) {
        EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();
        if (StringUtils.isBlank(impDate) || impType == null || shopType==null) {
            return;
        }
        entityWrapper.eq("imp_date", impDate);
        entityWrapper.eq("imp_type", impType);
        entityWrapper.eq("shop_type", shopType);
        this.delete(entityWrapper);
    }

    @Override
    public void deleteShipmentAandBAndCInfo(String impDate, Integer impType,Integer shopType) {
        //第一步：查询此数据是否已经导入过
        ShipmentaEntity queryShipmentaVo = this.queryShipmentaVo(impDate, impType,shopType);
        if (queryShipmentaVo == null) {
            return;
        }
        //第二步  删除出库A表 、B表、C表信息
        //1，删除出库A表信息
        this.deleteShipmentaVo(impDate, impType,shopType);
        //2，删除出库B表信息
        String shipmentAId = queryShipmentaVo.getId();
        shipmentbService.deleteShipmentbVoByShipmentAId(shipmentAId);
        //3，删除出库C表信息
        shipmentcService.deleteShipmentCVoByShipmentAId(shipmentAId);
    }

    @Override
    public ShipmentaEntity queryShipmentaVo(String impDate, Integer impType,Integer shopType) {
        EntityWrapper<ShipmentaEntity> entityWrapper = new EntityWrapper<ShipmentaEntity>();
        if (StringUtils.isBlank(impDate) || impType == null || shopType ==null) {
            return null;
        }
        entityWrapper.eq("imp_date", impDate);
        entityWrapper.eq("imp_type", impType);
        entityWrapper.eq("shop_type", shopType);
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

    @Override
    public ShipmentaEntity queryEntityById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return this.selectById(id);
        }
        return null;
    }

    @Override
    public void importData(MultipartFile file, String impType, String shopUnit) {
        try {
            List list = new ArrayList<>();
            List<List<String>> lists = ExcelReaderUtil.readCsv(file.getInputStream());
            ShipmentType shipmentType = ShipmentType.getShipmentType(Integer.valueOf(impType));
            ShipmentShopType shipmentShopUnit = ShipmentShopType.getShopUnit(Integer.valueOf(shopUnit));
            Date date = getDate(shipmentType.getCode(), lists);
            //根据不同类型 得到不同数据
            switch (shipmentType) {
                case JD:
                    list = getJDList(lists, date, shipmentShopUnit);
                    break;
                case TM:
                    list = getTMList(lists, date, shipmentShopUnit);
                    break;
                case TB:
                    list = getTBList(lists, date, shipmentShopUnit);
                    break;
                case PDD:
                    list = getPDDList(lists, date, shipmentShopUnit);
                    break;
            }
            //处理数据
            List<ShipmentbEntity> shipaList = (List<ShipmentbEntity>) list.get(0);
            List<ShipmentcEntity> shipbList = (List<ShipmentcEntity>) list.get(1);
            ShipmentaEntity a = new ShipmentaEntity();
            a.setImpDate(date);
            a.setImpName(file.getOriginalFilename());
            a.setOutCode(shipaList.get(0).getNumber());
            a.setImpType(shipmentType.getCode());
            a.setShopType(Integer.valueOf(shopUnit));
            this.insertShipmentAandBAndC(a, shipaList, shipbList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取JD数据
     *
     * @param lists
     * @param date
     * @return
     */
    public List<List> getJDList(List<List<String>> lists, Date date, ShipmentShopType shopUnit) {
        List<List> reList = new ArrayList<>();
        List<ShipmentbEntity> entityB = new ArrayList<>();
        List<ShipmentcEntity> entityC = new ArrayList<>();
        String d = ContentUtils.getDateToString(date, "yyyy-MM-dd");
        for (List<String> list : lists) {
            if (list.get(2).contains("评价")) {
                continue;
            }
            if (null == list.get(5)) {
                continue;
            }
            if (null != list.get(5) && !list.get(5).contains(d)) {
                continue;
            }
            String codeAndName = getCodeAndName(list.get(2));
            ShipmentbEntity b = initData(date, shopUnit, codeAndName);
            if (StringUtils.isBlank(codeAndName)) {
                b.setProductName(list.get(2));
            }
            b.setAmount(Integer.valueOf(list.get(3)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(8)); // 结算金额
            b.setPrice(price);
            b.setType(ShipmentType.JD.getCode());
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue() / b.getAmount());
            b.setUnitPrice(unitPrice);
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(16).replace("'",""));
            c.setPayType("月结");
            c.setProductName(list.get(2));
            c.setNumber(b.getAmount());
            c.setPrice(price);
            entityC.add(c);
        }
        reList.add(entityB);
        reList.add(entityC);
        return reList;
    }

    public List<List> getTMList(List<List<String>> lists, Date date, ShipmentShopType shopUnit) {
        List<List> reList = new ArrayList<>();
        List<ShipmentbEntity> entityB = new ArrayList<>();
        List<ShipmentcEntity> entityC = new ArrayList<>();
        String d = ContentUtils.getDateToString(date, "yyyy-MM-dd");
        for (List<String> list : lists) {
            if (StringUtils.isBlank(list.get(20))) {
                continue;
            }
            Date stringToDate = ContentUtils.getStringToDate(list.get(20).replace("/", "-"));
            String dateToString = ContentUtils.getDateToString(stringToDate, "yyyy-MM-dd");
            if (!dateToString.contains(d)) {
                continue;
            }
            String codeAndName = getCodeAndName(list.get(21));
            ShipmentbEntity b = initData(date, shopUnit, codeAndName);
            if (StringUtils.isBlank(codeAndName)) {
                b.setProductName(list.get(21));
            }
            b.setAmount(Integer.valueOf(list.get(26)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10)); // 实付金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue() / b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.TM.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(18).replace("'",""));
            c.setPayType("月结");
            c.setProductName(list.get(21));
            c.setPrice(price);
            c.setNumber(Integer.valueOf(list.get(26)));
            entityC.add(c);
        }
        reList.add(entityB);
        reList.add(entityC);
        return reList;
    }

    public List<List> getTBList(List<List<String>> lists, Date date, ShipmentShopType shopUnit) {
        List<List> reList = new ArrayList<>();
        List<ShipmentbEntity> entityB = new ArrayList<>();
        List<ShipmentcEntity> entityC = new ArrayList<>();
        String d = ContentUtils.getDateToString(date, "yyyy-MM-dd");
        for (List<String> list : lists) {
            if (StringUtils.isBlank(list.get(20))) {
                continue;
            }
            Date stringToDate = ContentUtils.getStringToDate(list.get(20).replace("/", "-"));
            String dateToString = ContentUtils.getDateToString(stringToDate, "yyyy-MM-dd");
            if (!dateToString.contains(d)) {
                continue;
            }
            String codeAndName = getCodeAndName(list.get(21));
            ShipmentbEntity b = initData(date, shopUnit, codeAndName);
            if (StringUtils.isBlank(codeAndName)) {
                b.setProductName(list.get(21));
            }
            b.setAmount(Integer.valueOf(list.get(26)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10)); // 实付金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue() / b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.TB.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(18).replace("'",""));
            c.setPayType("月结");
            c.setProductName(list.get(21));
            c.setPrice(unitPrice);
            c.setNumber(Integer.valueOf(list.get(26)));
            entityC.add(c);
        }
        reList.add(entityB);
        reList.add(entityC);
        return reList;
    }

    public List<List> getPDDList(List<List<String>> lists, Date date, ShipmentShopType shopUnit) {
        List<List> reList = new ArrayList<>();
        List<ShipmentbEntity> entityB = new ArrayList<>();
        List<ShipmentcEntity> entityC = new ArrayList<>();
        String d = ContentUtils.getDateToString(date, "yyyy-MM-dd");
        for (List<String> list : lists) {
            if (StringUtils.isBlank(list.get(22))) {
                continue;
            }
            Date stringToDate = ContentUtils.getStringToDate(list.get(22).replace("/", "-"));
            String dateToString = ContentUtils.getDateToString(stringToDate, "yyyy-MM-dd");
            if (!dateToString.contains(d)) {
                continue;
            }
            String codeAndName = getCodeAndName(list.get(0));
            ShipmentbEntity b = initData(date, shopUnit, codeAndName);
            if (StringUtils.isBlank(codeAndName)) {
                b.setProductName(list.get(0));
            }
            b.setAmount(Integer.valueOf(list.get(11).toString().trim()));
            b.setRemark(list.get(1) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10).replace("\t", "")); //实收金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue() / b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.PDD.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(1));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(17) + list.get(18) + list.get(19) + list.get(20));
            c.setOrderPhone(list.get(15).replace("'",""));
            c.setProductName(list.get(0));
            c.setPayType("月结");
            c.setPrice(unitPrice);
            c.setNumber(Integer.valueOf(list.get(11).trim()));
            entityC.add(c);
        }
        reList.add(entityB);
        reList.add(entityC);
        return reList;
    }

    private ShipmentbEntity initData(Date date, ShipmentShopType shopUnit, String codeAndName) {
        String d = ContentUtils.getDateToString(date, "yyyy-MM-dd");
        ShipmentbEntity b = new ShipmentbEntity();
        b.setDate(date);
        b.setShopUnit(shopUnit.getDesc());
        b.setSaleType(SaleType.T1.getCode());
        b.setShip("XX");
        b.setStorage("XX");
        if (StringUtils.isNotBlank(codeAndName)) {
            b.setProductCode(codeAndName.split("#")[0]);
            b.setProductName(codeAndName.split("#")[1]);
        } else {
            b.setProductCode("未知");
            b.setProductName("未知");
        }
        b.setNumber(storageaService.getCode(d, 1));
        b.setSaleBussinessType("销售出库类型");
        b.setUnit("台");
        BigDecimal unitCost = new BigDecimal(0.00000);
        b.setUnitCost(unitCost);
        BigDecimal cost = new BigDecimal(0.00);
        b.setCost(cost);
        b.setShipWarehouse("新飞一等品");
        return b;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static Date getDate(int type, List<List<String>> lists) {
        Date date = null;
        ShipmentType shipmentType = ShipmentType.getShipmentType(type);
        switch (shipmentType) {
            case JD:
                date = ContentUtils.getStringToDate(lists.get(0).get(24));
                break;
            case TM:
                date = ContentUtils.getStringToDate(lists.get(0).get(20).replace("/", "-"));
                break;
            case TB:
                date = ContentUtils.getStringToDate(lists.get(0).get(20).replace("/", "-"));
                break;
            case PDD:
                date = ContentUtils.getStringToDate(lists.get(0).get(22).replace("/", "-"));
                break;
        }
        return date;
    }

    private String getCodeAndName(String name) {
        Map<String, Object> relationData = codeNameRelationService.getRalationData();// 初始化
        if (null == relationData) {
            return null;
        }
        for (String string : relationData.keySet()) {
            if (name.contains(string)) {
                return relationData.get(string).toString();
            }
        }
        return null;
    }

}
