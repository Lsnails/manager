package com.base.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.base.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * @date 2017-06-20 15:23:47
 */
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);
    
    Long queryNumber(Map<String,Object> map);
    
    void updateCnt(Map<String,Object> map);

}
