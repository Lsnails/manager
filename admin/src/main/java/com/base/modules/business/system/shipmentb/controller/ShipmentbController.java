package com.base.modules.business.system.shipmentb.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.ValidatorUtils;
import com.base.modules.business.system.shipmentb.entity.ShipmentbEntity;
import com.base.modules.business.system.shipmentb.service.ShipmentbService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 出库B表数据
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:17
 */
@RestController
@RequestMapping("cms/shipmentb")
@Api(tags="出库B表数据管理")
public class ShipmentbController extends AbstractController{
    @Autowired
    private ShipmentbService shipmentbService;

//    /**
//     * 列表
//     */
//    @PostMapping("/list")
//    @ApiOperation("XX列表")
//    public R list(@RequestParam Map<String, Object> params,ShipmentbEntity shipmentb){
//        Page<ShipmentbEntity> page = shipmentbService.queryPage(params);
//
//        return PageUtils.convertFrom(page);
//    }
//
//
//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    public R info(@PathVariable("id") String id){
//        ShipmentbEntity shipmentb = shipmentbService.selectById(id);
//
//        return R.ok().put("shipmentb", shipmentb);
//    }
//
    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增XX")
    public R save(@RequestBody ShipmentbEntity shipmentb){
    	String uuId = UUIDUtils.getRandomUUID();
    	//设置uuid shipmentb.setCourseId(uuId);
        shipmentbService.insert(shipmentb);

        return R.ok();
    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody ShipmentbEntity shipmentb){
//        ValidatorUtils.validateEntity(shipmentb);
//        shipmentbService.updateAllColumnById(shipmentb);//全部更新
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
//        shipmentbService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
