package com.base.modules.business.system.codenamerelation.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.Query;

import com.base.modules.business.system.codenamerelation.dao.CodeNameRelationDao;
import com.base.modules.business.system.codenamerelation.entity.CodeNameRelationEntity;
import com.base.modules.business.system.codenamerelation.service.CodeNameRelationService;


@Service("codeNameRelationService")
public class CodeNameRelationServiceImpl extends ServiceImpl<CodeNameRelationDao, CodeNameRelationEntity> implements CodeNameRelationService {

    @Override
    public Page<CodeNameRelationEntity> queryPage(Map<String, Object> params) {
    	String code = (String)params.get("code");
    	EntityWrapper<CodeNameRelationEntity> entityWrapper = new EntityWrapper<CodeNameRelationEntity>();
    	if(StringUtils.isNotBlank(code)) {
    		entityWrapper.like("code", code);
    	}
        Page<CodeNameRelationEntity> page = this.selectPage(
                new Query<CodeNameRelationEntity>(params).getPage(),
                entityWrapper
        );

        return page;
    }

}
