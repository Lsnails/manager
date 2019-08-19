package com.base.modules.business.system.shipmenta.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 出库A表存储
 *
 * @author huanw
 * @email 
 * @date 2019-08-19 15:11:58
 */
@RestController
@RequestMapping("cms/shipmenta")
@Api(tags="出库A表存储管理")
public class ShipmentaController extends AbstractController{
    @Autowired
    private ShipmentaService shipmentaService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("出库A表存储列表")
    public R list(@RequestParam Map<String, Object> params,ShipmentaEntity shipmenta){
        Page<ShipmentaEntity> page = shipmentaService.queryPage(params);

        return PageUtils.convertFrom(page);
    }
    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增出库A表存储")
    public R save(@RequestBody ShipmentaEntity shipmenta){
    	String uuId = UUIDUtils.getRandomUUID();
    	//设置uuid shipmenta.setCourseId(uuId);
    	shipmenta.setId(uuId);
    	shipmenta.setCreateDate(new Date());
        shipmentaService.insert(shipmenta);

        return R.ok();
    }
    
    @RequestMapping("/uploadFile")
	@ApiOperation("上传方法")
	public R uploadFile(@RequestParam("file") MultipartFile file,String impType) {
		String originalFilename = file.getOriginalFilename();
		long size = file.getSize();
		return R.ok();
    }


//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    @RequiresPermissions("shipmenta:shipmenta:info")
//    public R info(@PathVariable("id") String id){
//        ShipmentaEntity shipmenta = shipmentaService.selectById(id);
//
//        return R.ok().put("shipmenta", shipmenta);
//    }


//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody ShipmentaEntity shipmenta){
//        ValidatorUtils.validateEntity(shipmenta);
//        shipmentaService.updateAllColumnById(shipmenta);//全部更新
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
//        shipmentaService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
