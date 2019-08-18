package com.base.modules.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.ValidatorUtils;
import com.base.modules.sys.entity.SysDictEntity;
import com.base.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 数据字典
 *
 * @since 3.1.0 2018-01-27
 */
@RestController
@RequestMapping("sys/dict")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dict:list")
    public R list(@RequestParam Map<String, Object> params) {
//    	Map<String, Object> paramsNew =new HashMap<String, Object>();
//    	paramsNew.put("_search", "false");
//    	paramsNew.put("nd", "123456");
//    	paramsNew.put("limit", "10");
//    	paramsNew.put("page", "1");
//    	paramsNew.put("sidx", "");
//    	paramsNew.put("order", "asc");
//    	paramsNew.put("name", "");
//    	paramsNew.put("_", "1546483669804");
        Page<SysDictEntity> page = sysDictService.queryPage(params);
        return PageUtils.convertFrom(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dict:info")
    public R info(@PathVariable("id") Long id) {
        SysDictEntity dict = sysDictService.selectById(id);

        return R.ok().put("dict", dict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public R save(@RequestBody SysDictEntity dict) {
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.insert(dict);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dict:update")
    public R update(@RequestBody SysDictEntity dict) {
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.updateById(dict);
        //清除缓存
        sysDictService.clearCache();
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dict:delete")
    public R delete(@RequestBody Long[] ids) {
        sysDictService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
