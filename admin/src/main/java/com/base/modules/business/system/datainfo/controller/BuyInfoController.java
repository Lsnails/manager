package com.base.modules.business.system.datainfo.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.ValidatorUtils;
import com.base.modules.business.system.datainfo.entity.BuyInfoEntity;
import com.base.modules.business.system.datainfo.service.BuyInfoService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 
 *
 * @author huanw
 * @email 
 * @date 2020-03-02 13:48:17
 */
@RestController
@RequestMapping("buyinfo")
@Api(tags="后台数据管理管理")
public class BuyInfoController extends AbstractController{
    @Autowired
    private BuyInfoService buyInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("数据管理列表")
    @RequiresPermissions("datainfo:buyinfo:list")
    public R list(@RequestParam Map<String, Object> params,BuyInfoEntity buyInfo){
        Page<BuyInfoEntity> page = buyInfoService.queryPage(params,buyInfo);

        return PageUtils.convertFrom(page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("数据管理信息")
    @RequiresPermissions("datainfo:buyinfo:info")
    public R info(@PathVariable("id") String id){
        BuyInfoEntity buyInfo = buyInfoService.selectById(id);

        return R.ok().put("buyInfo", buyInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增数据管理")
    @RequiresPermissions("datainfo:buyinfo:save")
    public R save(@RequestBody BuyInfoEntity buyInfo){
    	String uuId = UUIDUtils.getRandomUUID();
    	buyInfo.setId(uuId);
    	buyInfo.setCreateDate(new Date());
        buyInfoService.insert(buyInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改数据管理")
    @RequiresPermissions("datainfo:buyinfo:update")
    public R update(@RequestBody BuyInfoEntity buyInfo){
        ValidatorUtils.validateEntity(buyInfo);
        buyInfo.setUpdateDate(new Date());
        buyInfoService.updateAllColumnById(buyInfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除数据管理")
    @RequiresPermissions("datainfo:buyinfo:delete")
    public R delete(@RequestBody String[] ids){
        buyInfoService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }
    
    @PostMapping("/uploadFile")
    @ApiOperation("上传方法")
    public R uploadFile(@RequestParam("file") MultipartFile file) {
        long size = file.getSize();
        if (size > 0) {
            try {
            	buyInfoService.importData(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return R.error("上传文件为空");
        }
        return R.ok();
    }

}
