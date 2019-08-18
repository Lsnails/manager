package com.base.modules.business.system.codenamerelation.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.codenamerelation.entity.CodeNameRelationEntity;

/**
 * 
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 12:17:43
 */
public interface CodeNameRelationService extends IService<CodeNameRelationEntity> {

    Page<CodeNameRelationEntity> queryPage(Map<String, Object> params);
}

