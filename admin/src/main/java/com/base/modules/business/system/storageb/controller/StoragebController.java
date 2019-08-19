package com.base.modules.business.system.storageb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.business.system.storageb.service.StoragebService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 入库生成B表
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 10:50:46
 */
@RestController
@RequestMapping("cms/storageb")
@Api(tags="入库生成B表管理")
public class StoragebController extends AbstractController{
    @Autowired
    private StoragebService storagebService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("入库生成B表列表")
    public R list(@RequestParam Map<String, Object> params,StoragebEntity storageb){
        Page<StoragebEntity> page = storagebService.queryPage(params);

        return PageUtils.convertFrom(page);
    }


//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    public R info(@PathVariable("id") String id){
//        StoragebEntity storageb = storagebService.selectById(id);
//
//        return R.ok().put("storageb", storageb);
//    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增XX")
    public R save(@RequestBody StoragebEntity storageb){
    	String uuId = UUIDUtils.getRandomUUID();
    	//设置uuid storageb.setCourseId(uuId);
    	storageb.setId(uuId);
        storagebService.insert(storageb);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody StoragebEntity storageb){
//        ValidatorUtils.validateEntity(storageb);
//        storagebService.updateAllColumnById(storageb);//全部更新
//        
//        return R.ok();
//    }

//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    @ApiOperation("删除XX")
//    public R delete(@RequestBody String[] ids){
//        storagebService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
