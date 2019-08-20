package com.base.modules.business.system.shipmenta.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
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
import org.apache.poi.ss.formula.functions.T;
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
@Api(tags="出库A表存储管理")
public class ShipmentaController extends AbstractController{
    @Autowired
    private ShipmentaService shipmentaService;
    @Autowired
    private StorageaService storageaService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("出库A表存储列表")
    public R list(@RequestParam Map<String, Object> params,ShipmentaEntity shipmenta){
        Page<ShipmentaEntity> page = shipmentaService.queryPage(params);

        return PageUtils.convertFrom(page);
    }
    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增出库A表存储")
    public R save(@RequestBody ShipmentaEntity shipmenta){
    	String uuId = UUIDUtils.getRandomUUID();
    	//设置uuid shipmenta.setCourseId(uuId);
    	shipmenta.setId(uuId);
    	shipmenta.setCreateDate(new Date());
        shipmentaService.insert(shipmenta);

        return R.ok();
    }
    
    @RequestMapping("/uploadFile")
	@ApiOperation("上传方法")
	public R uploadFile(@RequestParam("file") MultipartFile file,String impType) {
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        if (size > 0) {
            try {
                List list = new ArrayList<>();
                List<List<String>> lists = ExcelReaderUtil.readCsv(file.getInputStream());
                ShipmentType shipmentType = ShipmentType.getShipmentType(Integer.valueOf(impType));
                Date date = getDate(shipmentType.getCode(),lists);
                //根据不同类型 得到不同数据
                switch (shipmentType) {
                    case JD:
                        list = getJDList(lists,date);
                        break;
                    case TM:
                        list = getTMList(lists);
                        break;
                    case TB:
                        list = getTBList(lists);
                        break;
                    case PDD:
                        list = getPDDList(lists);
                        break;
                }
                //处理数据
                System.out.println(list);
                ShipmentaEntity a = new ShipmentaEntity();
                a.setImpDate(date);
                a.setImpName(originalFilename);
                List<ShipmentbEntity> shipaList = (List<ShipmentbEntity>)list.get(0);
                List<ShipmentcEntity> shipbList = (List<ShipmentcEntity>)list.get(1);
                shipmentaService.insertShipmentAandBAndC(a,shipaList,shipbList);
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
     * @param lists
     * @param date
     * @return
     */
    public List<List> getJDList(List<List<String>> lists,Date date){
        List<List> reList = new ArrayList<>();
        List<ShipmentbEntity> entityB = new ArrayList<>();
        List<ShipmentcEntity> entityC = new ArrayList<>();
        for (List<String> list : lists) {
            if(list.get(2).toString().contains("评价")){
                continue;
            }
            ShipmentbEntity b = new ShipmentbEntity();
            b.setDate(date);
            b.setShopUnit(ShipmentShopType.JD.getDesc());
            b.setSaleType(SaleType.T1.getCode());
            b.setShip("XX");
            b.setStorage("XX");
            b.setSaleBussinessType("XX");
            b.setNumber(storageaService.getCode(ContentUtils.getDateToString(date,"yyyy-MM-dd"),1));
            b.setProductCode("001");
            b.setProductName("001");
            b.setUnit("台");
            b.setAmount(Integer.valueOf(list.get(3)));
            BigDecimal unitCost = new BigDecimal(0.00000);
            b.setUnitCost(unitCost);
            BigDecimal cost = new BigDecimal(0.00);
            b.setCost(cost);
            b.setRemark(list.get(0)+"_"+list.get(14));
            b.setShipWarehouse("新飞一等品");
            BigDecimal unitPrice = new BigDecimal(list.get(7));
            b.setPrice(unitPrice);
            b.setType(ShipmentType.JD.getCode());
            entityB.add(b);
            ShipmentcEntity c = new ShipmentcEntity();
            c.setOrderId(list.get(0));
            c.setShipmentOrderId("xxx1111");
            c.setOrderName(list.get(14));
            c.setOrderPhone(list.get(16));
            c.setPayType("月结");
            c.setProductName(list.get(2));
            c.setPrice(unitPrice);
            entityC.add(c);
        }
        reList.add(entityB);
        reList.add(entityC);
        return reList;
    }

    /**
     * 获取日期
     * @return
     */
    public static Date getDate(int type,List<List<String>> lists){
        Date date = null;
        ShipmentType shipmentType = ShipmentType.getShipmentType(type);
        switch (shipmentType){
            case JD:
                date = ContentUtils.getStringToDate(lists.get(0).get(24));
                break;
            case TM:
                date =  ContentUtils.getStringToDate(lists.get(0).get(20).replace("/","-"));
              break;
            case TB:
                date =  ContentUtils.getStringToDate(lists.get(0).get(20).replace("/","-"));
                break;
            case PDD:
                date =  ContentUtils.getStringToDate(lists.get(0).get(22).replace("/","-"));
                break;
        }
        return date;
    }

    public static List<T> getTMList(List<List<String>> lists){
        return null;
    }

    public static List<T> getTBList(List<List<String>> lists){
        return null;
    }

    public static List<T> getPDDList(List<List<String>> lists){
        return null;
    }

    public static void main(String[] args) {
        String s1="新飞（Frestec）276升 家用商用一级能效冷柜 节能单温柜 BC/BD-276HJ1EW";
        String s2="BC/BD-276HJ1EW";
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
    public R delete(@RequestBody String[] ids){
        shipmentaService.deleteBatchShipmentAandBAndCInfo(Arrays.asList(ids));
        return R.ok();
    }

}
