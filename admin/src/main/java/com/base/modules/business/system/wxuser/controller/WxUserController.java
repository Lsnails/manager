package com.base.modules.business.system.wxuser.controller;

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
import com.base.modules.business.system.wxuser.entity.WxUserEntity;
import com.base.modules.business.system.wxuser.service.WxUserService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 
 *微信user
 * @author huanw
 * @email 
 * @date 2019-10-15 10:12:07
 */
@RestController
@RequestMapping("cms/wxuser")
@Api(tags="微信user管理")
public class WxUserController extends AbstractController{
    @Autowired
    private WxUserService wxUserService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("微信user列表")
//    @RequiresPermissions("wxuser:wxuser:list")
    public R list(@RequestParam Map<String, Object> params,WxUserEntity wxUser){
        Page<WxUserEntity> page = wxUserService.queryPage(params);

        return PageUtils.convertFrom(page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("微信user信息")
//    @RequiresPermissions("wxuser:wxuser:info")
    public R info(@PathVariable("id") String id){
        WxUserEntity wxUser = wxUserService.selectById(id);

        return R.ok().put("wxUser", wxUser);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增微信user")
//    @RequiresPermissions("wxuser:wxuser:save")
    public R save(@RequestBody WxUserEntity wxUser){
    	String uuId = UUIDUtils.getRandomUUID();
    	wxUser.setId(uuId);
    	wxUser.setCreateDate(new Date());
        wxUserService.insert(wxUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改微信user")
//    @RequiresPermissions("wxuser:wxuser:update")
    public R update(@RequestBody WxUserEntity wxUser){
        ValidatorUtils.validateEntity(wxUser);
        wxUser.setUpdateDate(new Date());
        wxUserService.updateAllColumnById(wxUser);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除微信user")
//    @RequiresPermissions("wxuser:wxuser:delete")
    public R delete(@RequestBody String[] ids){
        wxUserService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
