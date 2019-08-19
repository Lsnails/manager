package com.base.modules.business.system.shipmentc.controller;

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
import com.base.modules.business.system.shipmentc.entity.ShipmentcEntity;
import com.base.modules.business.system.shipmentc.service.ShipmentcService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 出库C表数据
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:13:57
 */
@RestController
@RequestMapping("shipmentc/shipmentc")
@Api(tags="出库C表数据管理")
public class ShipmentcController extends AbstractController{
    @Autowired
    private ShipmentcService shipmentcService;
//
//    /**
//     * 列表
//     */
//    @GetMapping("/list")
//    @ApiOperation("XX列表")
//    public R list(@RequestParam Map<String, Object> params,ShipmentcEntity shipmentc){
//        Page<ShipmentcEntity> page = shipmentcService.queryPage(params);
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
//        ShipmentcEntity shipmentc = shipmentcService.selectById(id);
//
//        return R.ok().put("shipmentc", shipmentc);
//    }
//
//    /**
//     * 保存
//     */
//    @PostMapping("/save")
//    @ApiOperation("新增XX")
//    public R save(@RequestBody ShipmentcEntity shipmentc){
//    	String uuId = UUIDUtils.getRandomUUID();
//    	//设置uuid shipmentc.setCourseId(uuId);
//    	shipmentc.setCreatetime(new Date());
//    	shipmentc.setCreateby(this.getUsername());
//    	shipmentc.setLastmodifyby(this.getUsername());
//    	shipmentc.setLastmodifytime(new Date());
//        shipmentcService.insert(shipmentc);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody ShipmentcEntity shipmentc){
//        ValidatorUtils.validateEntity(shipmentc);
//        shipmentcService.updateAllColumnById(shipmentc);//全部更新
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
//        shipmentcService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
