package com.base.modules.business.system.shipmenta.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.shipmenta.ShipmentType;
import com.base.modules.business.system.shipmenta.entity.ShipmentaEntity;
import com.base.modules.business.system.shipmenta.service.ShipmentaService;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



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
        if (size > 0) {
            try {
                List<T> list = new ArrayList<>();
                List<List<String>> lists = ExcelReaderUtil.readCsv(file.getInputStream());
                ShipmentType shipmentType = ShipmentType.getShipmentType(Integer.valueOf(impType));
                //根据不同类型 得到不同数据
                switch (shipmentType) {
                    case JD:
                        list = getJDList(lists);
                        break;
                    case TM:
                        list = getTMList(lists);
                        break;
                    case TB:
                        list = getTBList(lists);
                        break;
                    case PDD:
                        list = getPDDList(lists);
                        break;
                }
                //处理数据
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return R.error("上传文件为空");
        }
        return R.ok();
    }
    
	@RequestMapping("/exportB")
	@ApiOperation("下载出库B表信息")
	public void exportB(String shipmentAId) {

		System.out.println(shipmentAId);
	}
	
	@RequestMapping("/exportC")
	@ApiOperation("下载出库C表信息")
	public void exportC(String shipmentAId) {

		System.out.println(shipmentAId);
	}

    public static List<T> getJDList(List<List<String>> lists){
        return null;
    }

    public static List<T> getTMList(List<List<String>> lists){
        return null;
    }

    public static List<T> getTBList(List<List<String>> lists){
        return null;
    }

    public static List<T> getPDDList(List<List<String>> lists){
        return null;
    }

    public static void main(String[] args) {
        String s1="新飞（Frestec）276升 家用商用一级能效冷柜 节能单温柜 BC/BD-276HJ1EW";
        String s2="BC/BD-276HJ1EW";
        System.out.println(s1.contains(s2));
        System.out.println(s1.contentEquals(s2));
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
