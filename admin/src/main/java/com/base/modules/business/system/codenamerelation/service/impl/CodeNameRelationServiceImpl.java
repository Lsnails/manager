package com.base.modules.business.system.codenamerelation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;
import com.base.modules.business.system.codenamerelation.dao.CodeNameRelationDao;
import com.base.modules.business.system.codenamerelation.entity.CodeNameRelationEntity;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("codeNameRelationService")
public class CodeNameRelationServiceImpl extends ServiceImpl<CodeNameRelationDao, CodeNameRelationEntity> implements CodeNameRelationService {

    @Override
    public Page<CodeNameRelationEntity> queryPage(Map<String, Object> params) {
    	String code = (String)params.get("code");
    	String name = (String) params.get("name");
    	EntityWrapper<CodeNameRelationEntity> entityWrapper = new EntityWrapper<CodeNameRelationEntity>();
    	if(StringUtils.isNotBlank(code)) {
    		entityWrapper.like("code", code);
    	}
    	if(StringUtils.isNotBlank(name)){
    	    entityWrapper.like("name",name);
        }
        entityWrapper.orderBy("code asc");
        Page<CodeNameRelationEntity> page = this.selectPage(
                new Query<CodeNameRelationEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

    @Override
    public List<String> getRelationNameAndCode(String name) {
        List<String> list = new ArrayList<>();
        Map<String,Object> map  = new HashMap<>();
        map.put("name",name);
        List<CodeNameRelationEntity> records = getList(map);
        if(null != records && records.size() >0){
            for (CodeNameRelationEntity record : records) {
                list.add(record.getCode());
                list.add(record.getName());
            }
        }
        return list;
    }

	@Override
	public List<CodeNameRelationEntity> getList(Map<String, Object> params) {
		String code = (String)params.get("code");
    	String name = (String) params.get("name");
    	EntityWrapper<CodeNameRelationEntity> entityWrapper = new EntityWrapper<CodeNameRelationEntity>();
    	if(StringUtils.isNotBlank(code)) {
    		entityWrapper.like("code", code);
    	}
    	if(StringUtils.isNotBlank(name)){
    	    entityWrapper.like("name",name);
        }
        entityWrapper.orderBy("code asc");
		return this.selectList(entityWrapper);
	}

}
