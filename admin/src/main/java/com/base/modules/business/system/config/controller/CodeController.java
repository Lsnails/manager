package com.base.modules.business.system.config.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.common.utils.R;
import com.base.modules.business.system.config.entity.CodeEntity;
import com.base.modules.business.system.config.service.CodeService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 日期对应code生成表
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 19:14:31
 */
@RestController
@RequestMapping("cms/code")
@Api(tags="a日期对应code生成管理")
public class CodeController extends AbstractController{
    @Autowired
    private CodeService codeService;

//    /**
//     * 列表
//     */
//    @GetMapping("/list")
//    @ApiOperation("XX列表")
//    public R list(@RequestParam Map<String, Object> params,CodeEntity code){
//        Page<CodeEntity> page = codeService.queryPage(params);
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
//        CodeEntity code = codeService.selectById(id);
//
//        return R.ok().put("code", code);
//    }

    @GetMapping("/info/{date}")
    @ApiOperation("按日期获取code信息")
    public R queryCodeEntityByDate(@PathVariable String date) {
    	CodeEntity codeEntity = codeService.queryCodeEntityByDate(date);
    	return R.ok().put("data", codeEntity);
    }
    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增日期对应的code")
    public R save(@RequestBody CodeEntity code){
    	String uuId = UUIDUtils.getRandomUUID();
    	//设置uuid code.setCourseId(uuId);
    	code.setId(uuId);
    	code.setCreateDate(new Date());
    	code.setUpdateDate(code.getCreateDate());
        codeService.insert(code);
        
        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody CodeEntity code){
//        ValidatorUtils.validateEntity(code);
//        codeService.updateAllColumnById(code);//全部更新
//        
//        return R.ok();
//    }

//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    @ApiOperation("删除XX")
//    public R delete(@RequestBody String[] ids){
//        codeService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
