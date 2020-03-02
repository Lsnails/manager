package com.base.modules.business.system.datainfo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.ExcelReaderUtil;
import com.base.common.utils.Query;
import com.base.modules.business.system.datainfo.dao.BuyInfoDao;
import com.base.modules.business.system.datainfo.entity.BuyInfoEntity;
import com.base.modules.business.system.datainfo.service.BuyInfoService;
import com.base.utils.AddressUtils;
import com.base.utils.UUIDUtils;


@Service("buyInfoService")
public class BuyInfoServiceImpl extends ServiceImpl<BuyInfoDao, BuyInfoEntity> implements BuyInfoService {

    @Override
    public Page<BuyInfoEntity> queryPage(Map<String, Object> params) {
        Page<BuyInfoEntity> page = this.selectPage(
                new Query<BuyInfoEntity>(params).getPage(),
                new EntityWrapper<BuyInfoEntity>()
        );

        return page;
    }

	@Override
	public void importData(MultipartFile file) throws IOException {
        List<List<String>> lists = ExcelReaderUtil.readCsv(file.getInputStream());
        if (null == lists) {
            return;
        }
        List<BuyInfoEntity> listEntity = new ArrayList<>();
        lists.stream().forEach(i->{
        	BuyInfoEntity buyInfoEntity =  new BuyInfoEntity();
        	buyInfoEntity.setId(UUIDUtils.getRandomUUID());
        	buyInfoEntity.setName(i.get(0).trim());
        	buyInfoEntity.setPhone(i.get(1).trim());
        	buyInfoEntity.setProType(i.get(2).trim());
        	buyInfoEntity.setBuyTime(i.get(3).trim());
        	buyInfoEntity.setBuyPrice(i.get(4).trim());
        	buyInfoEntity.setBuyNumber(Integer.valueOf(i.get(5).trim()));
        	buyInfoEntity.setBuyChannel(i.get(6).trim());
        	buyInfoEntity.setBuyAddress(i.get(7).trim());
        	buyInfoEntity.setAddress(setAddress(i.get(7).trim()));
        	buyInfoEntity.setRemark(i.get(8)==null?"":i.get(8).trim());
        	buyInfoEntity.setStar(i.get(9)==null?"":i.get(9).trim());
        	buyInfoEntity.setReturnInfo(i.get(10)==null?"":i.get(10).trim());
        	buyInfoEntity.setCreateDate(new Date());
        	listEntity.add(buyInfoEntity);
        });
        this.insertBatch(listEntity); //批量插入
	}
	
	public String setAddress(String str) {
		StringBuffer reStr= new StringBuffer();
		List<Map<String, String>> addressResolution = AddressUtils.addressResolution(str);
		addressResolution.stream().forEach(i->{
			if(null != i) {
				reStr.append(i.get("province")).append(i.get("city"));
			}
		});
		return reStr.toString();
	}
	
	
	

}