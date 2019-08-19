package com.base.modules.business.system.storagea.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import com.base.modules.business.system.config.entity.CodeEntity;
import com.base.modules.business.system.config.service.CodeService;
import com.base.modules.business.system.storagea.BuyType;
import com.base.modules.business.system.storagea.UnitType;
import com.base.modules.business.system.storagea.entity.StorageaEntity;
import com.base.modules.business.system.storagea.service.StorageaService;
import com.base.modules.business.system.storageb.entity.StoragebEntity;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * 入库A表存储
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 20:43:16
 */
@RestController
@RequestMapping("cms/storagea")
@Api(tags="a入库A表存储管理")
public class StorageaController extends AbstractController{
    @Autowired
    private StorageaService storageaService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeNameRelationService codeNameRelationService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("入库导入A信息列表")
    public R list(@RequestParam Map<String, Object> params,StorageaEntity storagea){
        Page<StorageaEntity> page = storageaService.queryPage(params);

        return PageUtils.convertFrom(page);
    }

//
//    /**
//     * 信息
//     */
//    @GetMapping("/info/{id}")
//    @ApiOperation("XX信息")
//    public R info(@PathVariable("id") String id){
//        StorageaEntity storagea = storageaService.selectById(id);
//
//        return R.ok().put("storagea", storagea);
//    }

//    /**
//     * 保存
//     */
    @PostMapping("/save")
    @ApiOperation("(没调试，调试参数总是报错)保存入库A表导入信息 和 入库B表信息")
    public R save(@RequestBody StorageaEntity storagea,@RequestBody List<StoragebEntity>  storagebVoList,@RequestBody String applyDate){
    	storagea.setApplyDate(ContentUtils.getStringToDate("2019-08-19"));
        storageaService.insertStorageaVoAndStoragebList(storagea, storagebVoList, applyDate);
        return R.ok();
    }
    
    @RequestMapping("/uploadFile")
	@ApiOperation("上传方法")
	public R uploadFile(@RequestParam("file") MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		long size = file.getSize();
		if(size>0){
            try {
                List<List<String>> readList = ExcelReaderUtil.readExcel(file.getInputStream());
                String date = ExcelReaderUtil.getStorageDate(readList);
                List<List<String>> storageBData = ExcelReaderUtil.getStorageData(readList);
                List<StoragebEntity> list = getList(storageBData, date);
                StorageaEntity storageaEntity = new StorageaEntity();
                storageaEntity.setApplyDate(ContentUtils.getStringToDate(date));
                storageaEntity.setApplyName(originalFilename);
                storageaEntity.setOutCode(list.get(0).getNumber());
                storageaService.insertStorageaVoAndStoragebList(storageaEntity,list,date);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
		return R.ok();
    }

    private List<StoragebEntity> getList(List<List<String>> list,String date){
        List<StoragebEntity> rList = new ArrayList<>();
        CodeEntity codeEntity = codeService.queryCodeEntityByDate(date);
        for (List<String> strings : list) {
            StoragebEntity entity = new StoragebEntity();
            entity.setDate(ContentUtils.getStringToDate(date));
            entity.setSupplier("新飞制冷器具有限公司");
            if (codeEntity.getCode() >= 100) {
                entity.setNumber("WIN000" + codeEntity.getCode());
            } else if (codeEntity.getCode() >= 10 && codeEntity.getCode() < 100) {
                entity.setNumber("WIN0000" + codeEntity.getCode());
            } else {
                entity.setNumber("WIN00000" + codeEntity.getCode());
            }
            entity.setAccept("XX");
            entity.setStorage("XX");
            entity.setBuyType(BuyType.T1.getCode());
            List<String> relationNameAndCode = codeNameRelationService.getRelationNameAndCode(strings.get(1));
            if (null != relationNameAndCode && relationNameAndCode.size() > 0) {
                entity.setMaterNumber(relationNameAndCode.get(0));
                entity.setMaterName(relationNameAndCode.get(1));
            } else {
                entity.setMaterNumber("未知");
                entity.setMaterName("未知");
            }
            entity.setUnit(UnitType.T1.getCode());
            Integer number = Integer.valueOf(strings.get(2)).intValue();
            Double unitPrice = Double.valueOf(strings.get(3)).doubleValue();
            BigDecimal price = new BigDecimal(number * unitPrice);
            entity.setAmount(Integer.valueOf(strings.get(2)));
            entity.setUnitPrice(new BigDecimal(unitPrice));
            entity.setPrice(price);
            entity.setWarehouse("新飞一等品");
            rList.add(entity);
        }
        return rList;
    }


//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改XX")
//    public R update(@RequestBody StorageaEntity storagea){
//        ValidatorUtils.validateEntity(storagea);
//        storageaService.updateAllColumnById(storagea);//全部更新
//        
//        return R.ok();
//    }

//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    @ApiOperation("删除XX")
//    public R delete(@RequestBody String[] ids){
//        storageaService.deleteBatchIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
