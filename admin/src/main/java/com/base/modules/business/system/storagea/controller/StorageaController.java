package com.base.modules.business.system.storagea.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 入库A表存储
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 20:43:16
 */
@RestController
@RequestMapping("cms/storagea")
@Api(tags="a入库A表存储管理")
public class StorageaController extends AbstractController{
    @Autowired
    private StorageaService storageaService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("入库导入A信息列表")
    public R list(@RequestParam Map<String, Object> params,StorageaEntity storagea){
        Page<StorageaEntity> page = storageaService.queryPage(params);

        return PageUtils.convertFrom(page);
    }

//
//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    public R info(@PathVariable("id") String id){
//        StorageaEntity storagea = storageaService.selectById(id);
//
//        return R.ok().put("storagea", storagea);
//    }

//    /**
//     * 保存
//     */
    @PostMapping("/save")
    @ApiOperation("保存入库A表导入信息")
    public R save(@RequestBody StorageaEntity storagea){
    	String uuId = UUIDUtils.getRandomUUID();
    	storagea.setId(uuId);
        storageaService.insertStorageaEntity(storagea);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody StorageaEntity storagea){
//        ValidatorUtils.validateEntity(storagea);
//        storageaService.updateAllColumnById(storagea);//全部更新
//        
//        return R.ok();
//    }

//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    @ApiOperation("删除XX")
//    public R delete(@RequestBody String[] ids){
//        storageaService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
