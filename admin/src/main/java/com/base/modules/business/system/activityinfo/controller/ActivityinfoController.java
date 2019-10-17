package com.base.modules.business.system.activityinfo.controller;

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
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 活动管理表
 *
 * @author huanw
 * @email 
 * @date 2019-10-15 15:48:42
 */
@RestController
@RequestMapping("cms/activityinfo")
@Api(tags="活动管理")
public class ActivityinfoController extends AbstractController{
    @Autowired
    private ActivityinfoService activityinfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("活动列表")
    public R list(@RequestParam Map<String, Object> params,ActivityinfoEntity activityinfo){
        Page<ActivityinfoEntity> page = activityinfoService.queryPage(params,activityinfo);

        return PageUtils.convertFrom(page);
    }

    @PostMapping("/allList")
    @ApiOperation("活动列表")
    public R allList(ActivityinfoEntity activityinfo){
    	List<ActivityinfoEntity> selectActivityList = activityinfoService.selectList(new EntityWrapper<ActivityinfoEntity>());
        return R.ok().put("data", selectActivityList);
    }
    /**
     * 信息
     */
    @GetMapping("/info/{activityinfoId}")
    @ApiOperation("活动信息")
    public R info(@PathVariable("activityinfoId") String activityinfoId){
        ActivityinfoEntity activityinfo = activityinfoService.selectById(activityinfoId);

        return R.ok().put("activityinfo", activityinfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增活动")
    public R save(@RequestBody ActivityinfoEntity activityinfo){
    	String uuId = UUIDUtils.getRandomUUID();
    	activityinfo.setActivityinfoId(uuId);
    	activityinfo.setCreatetime(new Date());
    	activityinfo.setCreateby(this.getUsername());
    	activityinfo.setLastmodifyby(this.getUsername());
    	activityinfo.setLastmodifytime(new Date());
        activityinfoService.insert(activityinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改活动")
    public R update(@RequestBody ActivityinfoEntity activityinfo){
        ValidatorUtils.validateEntity(activityinfo);
        activityinfo.setLastmodifyby(this.getUsername());
    	activityinfo.setLastmodifytime(new Date());
        String status = activityinfo.getStatus();
        //修改状态为1启动时候，将其他的活动置为0（关闭）
        if("1".equals(status)) {
        	EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper<ActivityinfoEntity>();
        	ActivityinfoEntity entity =new ActivityinfoEntity();
        	entity.setStatus("0");
        	activityinfoService.update(entity, entityWrapper);
        }
        activityinfoService.updateAllColumnById(activityinfo);//全部更新
        return R.ok();
    }
    /**
     * 修改
     */
    @PutMapping("/updateStatus/{activityinfoId}/{status}")
    @ApiOperation("修改活动状态")
    public R updateStatus(@PathVariable("activityinfoId") String activityinfoId,@PathVariable("status") String status){
    	ActivityinfoEntity activityinfo = activityinfoService.selectById(activityinfoId);
        activityinfo.setLastmodifyby(this.getUsername());
        activityinfo.setStatus(status);
    	activityinfo.setLastmodifytime(new Date());
        //修改状态为1启动时候，将其他的活动置为0（关闭）
        if("1".equals(status)) {
        	EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper<ActivityinfoEntity>();
        	ActivityinfoEntity entity =new ActivityinfoEntity();
        	entity.setStatus("0");
        	activityinfoService.update(entity, entityWrapper);
        }
        activityinfoService.updateAllColumnById(activityinfo);//全部更新
        return R.ok();
    }
    
    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除活动")
    public R delete(@RequestBody String[] activityinfoIds){
        activityinfoService.deleteBatchIds(Arrays.asList(activityinfoIds));

        return R.ok();
    }

}
