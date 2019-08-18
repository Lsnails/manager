package com.base.modules.business.system.config.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.config.entity.CodeEntity;

/**
 * 日期对应code生成表
 *
 * @author huanw
 * @email 
 * @date 2019-08-18 19:14:31
 */
public interface CodeService extends IService<CodeEntity> {

    Page<CodeEntity> queryPage(Map<String, Object> params);
    /**
     * 新增代码日期代码对应关系
     * @param codeEntity
     */
    void insertCodeEntity(CodeEntity codeEntity);
    /**
     * 通过日期查询code等信息,如果该日期目前还没有code，会实时生成并且返回
     * @param date
     * @return
     */
    CodeEntity queryCodeEntityByDate(String date);
}

