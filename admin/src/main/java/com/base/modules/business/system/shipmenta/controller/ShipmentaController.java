package com.base.modules.business.system.shipmenta.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    private ShipmentbService shipmentbService;
    @Autowired
    private ShipmentcService shipmentcService;

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
        long size = file.getSize();
        Integer su = Integer.valueOf(shopUnit);
        if (size > 0) {
            try {
                shipmentaService.importData(file, impType, shopUnit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return R.error("上传文件为空");
        }
        return R.ok();
    }

    @RequestMapping("/exportB")
    @ApiOperation("下载出库B表信息")
    public void exportB(String shipmentAId, HttpServletResponse response) {
        String[] title = new String[]{"日期", "购货单位", "编号", "销售方式", "发货", "保管", "销售业务类型", "产品代码", "产品名称",
                "单位", "实发数量", "单位成本", "成本", "备注", "发货仓位", "仓位", "销售单价", "销售金额"};
        ShipmentaEntity shipmentaEntity = shipmentaService.queryEntityById(shipmentAId);
        String date = ContentUtils.getDateToString(shipmentaEntity.getImpDate(), "yyyy-MM-dd");
        String fileName = shipmentaEntity.getImpName().replace(".csv", "") + "_B_" + date + ".xlsx";
        List<Object[]> list = shipmentbService.exportListB(shipmentAId);
        ExcelReaderUtil.exportExcelObj(fileName, title, list, response);
        System.out.println(shipmentAId);
    }

    @RequestMapping("/exportC")
    @ApiOperation("下载出库C表信息")
    public void exportC(String shipmentAId, HttpServletResponse response) {
        String[] title = new String[]{"序号 ", "用户/平台订单号", "物流单号", "收件人姓名 *", "收件人手机", "收货详细地址 *", "运输性质 *", "运费付款方式 *", "送货方式",
                "货物名称 *", "数量", "金额", "货物重量", "货物体积", "其他费用", "保价金额", "备注信息"};
        ShipmentaEntity shipmentaEntity = shipmentaService.queryEntityById(shipmentAId);
        String date = ContentUtils.getDateToString(shipmentaEntity.getImpDate(), "yyyy-MM-dd");
        String fileName = shipmentaEntity.getImpName().replace(".csv", "") + "_C_" + date + ".xlsx";
        List<Object[]> list = shipmentcService.exportListC(shipmentAId);
        ExcelReaderUtil.exportExcelObj(fileName, title, list, response);
        System.out.println(shipmentAId);
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
