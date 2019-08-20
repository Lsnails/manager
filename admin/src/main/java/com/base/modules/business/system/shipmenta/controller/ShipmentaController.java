package com.base.modules.business.system.shipmenta.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import com.base.modules.business.system.shipmenta.ShipmentShopType;
import com.base.modules.business.system.shipmenta.ShipmentType;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.business.system.shipmentb.SaleType;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * 出库A表存储
 *
 * @author huanw
 * @email
 * @date 2019-08-19 15:11:58
 */
@RestController
@RequestMapping("cms/shipmenta")
@Api(tags = "出库A表存储管理")
public class ShipmentaController extends AbstractController {
    @Autowired
    private ShipmentaService shipmentaService;
    @Autowired
    private StorageaService storageaService;
    @Autowired
    private CodeNameRelationService codeNameRelationService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("出库A表存储列表")
    public R list(@RequestParam Map<String, Object> params, ShipmentaEntity shipmenta) {
        Page<ShipmentaEntity> page = shipmentaService.queryPage(params);

        return PageUtils.convertFrom(page);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增出库A表存储")
    public R save(@RequestBody ShipmentaEntity shipmenta) {
        String uuId = UUIDUtils.getRandomUUID();
        //设置uuid shipmenta.setCourseId(uuId);
        shipmenta.setId(uuId);
        shipmenta.setCreateDate(new Date());
        shipmentaService.insert(shipmenta);

        return R.ok();
    }

    @RequestMapping("/uploadFile")
    @ApiOperation("上传方法")
    public R uploadFile(@RequestParam("file") MultipartFile file, String impType, String shopUnit) {
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        Integer su = Integer.valueOf(shopUnit);
        if (size > 0) {
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
                a.setImpName(originalFilename);
                a.setOutCode(shipaList.get(0).getNumber());
                a.setImpType(shipmentType.getCode());
                shipmentaService.insertShipmentAandBAndC(a, shipaList, shipbList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return R.error("上传文件为空");
        }
        return R.ok();
    }

    @RequestMapping("/exportB")
    @ApiOperation("下载出库B表信息")
    public void exportB(String shipmentAId) {

        System.out.println(shipmentAId);
    }

    @RequestMapping("/exportC")
    @ApiOperation("下载出库C表信息")
    public void exportC(String shipmentAId) {

        System.out.println(shipmentAId);
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
            b.setAmount(Integer.valueOf(list.get(3)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(8)); // 结算金额
            b.setPrice(price);
            b.setType(ShipmentType.JD.getCode());
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue()/b.getAmount());
            b.setUnitPrice(unitPrice);
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(16));
            c.setPayType("月结");
            c.setProductName(list.get(2));
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
            b.setAmount(Integer.valueOf(list.get(26)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10)); // 实付金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue()/b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.TM.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(18));
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
            b.setAmount(Integer.valueOf(list.get(26)));
            b.setRemark(list.get(0) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10)); // 实付金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue()/b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.TB.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(15));
            c.setOrderPhone(list.get(18));
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
            b.setAmount(Integer.valueOf(list.get(11).toString().trim()));
            b.setRemark(list.get(1) + "_" + list.get(14));
            BigDecimal price = new BigDecimal(list.get(10).replace("\t", "")); //实收金额
            b.setPrice(price);
            BigDecimal unitPrice = new BigDecimal(b.getPrice().doubleValue()/b.getAmount());
            b.setUnitPrice(unitPrice);
            b.setType(ShipmentType.PDD.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(1));
            c.setOrderName(list.get(14));
            c.setOrderAddress(list.get(17) + list.get(18) + list.get(19) + list.get(20));
            c.setOrderPhone(list.get(15));
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

    public static void main(String[] args) {
        String s1 = "新飞（Frestec）276升 家用商用一级能效冷柜 节能单温柜 BC/BD-276HJ1EW";
        String s2 = "BC/BD-276HJ1EW";
        System.out.println(s1.contains(s2));
        System.out.println(s1.contentEquals(s2));
    }


//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    @RequiresPermissions("shipmenta:shipmenta:info")
//    public R info(@PathVariable("id") String id){
//        ShipmentaEntity shipmenta = shipmentaService.selectById(id);
//
//        return R.ok().put("shipmenta", shipmenta);
//    }


//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody ShipmentaEntity shipmenta){
//        ValidatorUtils.validateEntity(shipmenta);
//        shipmentaService.updateAllColumnById(shipmenta);//全部更新
//        
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    @ApiOperation("删除XX")
//    public R delete(@RequestBody String[] ids){
//        shipmentaService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除导出A表信息")
    public R delete(@RequestBody String[] ids) {
        shipmentaService.deleteBatchShipmentAandBAndCInfo(Arrays.asList(ids));
        return R.ok();
    }

}
