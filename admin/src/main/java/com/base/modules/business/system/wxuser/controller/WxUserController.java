package com.base.modules.business.system.wxuser.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.ValidatorUtils;
import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;
import com.base.modules.business.system.activityinfo.service.ActivityinfoService;
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
    @Autowired
    private ActivityinfoService activityinfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("微信user列表")
//    @RequiresPermissions("wxuser:wxuser:list")
    public R list(@RequestParam Map<String, Object> params,WxUserEntity wxUser){
        Page<WxUserEntity> page = wxUserService.queryPage(params,wxUser);

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
    
    /**
     * 核销
     */
    @PostMapping("/hexiao")
    @ApiOperation("核销")
    public R hexiao(@RequestBody String[] ids){
    	R r = new R();
    	List<WxUserEntity> list =new ArrayList<>();
    	for (int i = 0; i < ids.length; i++) {
    		WxUserEntity entity =new WxUserEntity();
    		entity = wxUserService.selectById(ids[i]);
    		EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper<ActivityinfoEntity>();
    		entityWrapper.eq("activityinfo_id", entity.getActivityId());
    		ActivityinfoEntity selectOne = activityinfoService.selectOne(entityWrapper);
    		if(selectOne != null) {
    			if("0".equals(selectOne.getStatus())) {
    				r.put("code", 1);
    				r.put("msg", "用户编码为："+entity.getUserCode()+",活动已经过期，请取消选择！");
    				return r;
    			}
    		}
    		entity.setId(ids[i]);
    		//0 未核销 1已核销
    		entity.setState(1);
    		entity.setUpdateDate(new Date());
    		list.add(entity);
		}
    	wxUserService.updateBatchById(list);
        return R.ok();
    }

}
