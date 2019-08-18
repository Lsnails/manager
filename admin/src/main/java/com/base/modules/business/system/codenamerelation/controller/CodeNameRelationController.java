package com.base.modules.business.system.codenamerelation.controller;

import java.util.Arrays;
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
import com.base.modules.business.system.codenamerelation.entity.CodeNameRelationEntity;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 12:17:43
 */
@RestController
@RequestMapping("codenamerelation")
@Api(tags="code和name关系管理")
public class CodeNameRelationController extends AbstractController{
    @Autowired
    private CodeNameRelationService codeNameRelationService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("XX列表")
    public R list(@RequestParam Map<String, Object> params,CodeNameRelationEntity codeNameRelation){
        Page<CodeNameRelationEntity> page = codeNameRelationService.queryPage(params);

        return PageUtils.convertFrom(page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("XX信息")
    public R info(@PathVariable("id") String id){
        CodeNameRelationEntity codeNameRelation = codeNameRelationService.selectById(id);

        return R.ok().put("codeNameRelation", codeNameRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增XX")
    public R save(@RequestBody CodeNameRelationEntity codeNameRelation){
    	String uuId = UUIDUtils.getRandomUUID();
    	codeNameRelation.setId(uuId);
        codeNameRelationService.insert(codeNameRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改XX")
    public R update(@RequestBody CodeNameRelationEntity codeNameRelation){
        ValidatorUtils.validateEntity(codeNameRelation);
        codeNameRelationService.updateAllColumnById(codeNameRelation);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除XX")
    public R delete(@RequestBody String[] ids){
        codeNameRelationService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
