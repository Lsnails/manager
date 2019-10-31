package com.base.modules.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.base.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * @date 2017-06-20 15:23:47
 */
public interface SysDeptService extends IService<SysDeptEntity> {

	List<SysDeptEntity> queryList(Map<String, Object> map);

	List<SysDeptEntity> getList();

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 获取子部门ID，用于数据过滤
	 */
	List<Long> getSubDeptIdList(Long deptId);

	/**
	 * 获取网点信息 根据网点id
	 * @param id
	 * @return
	 */
	SysDeptEntity getWdInfo(String id);

	/**
	 * 1.删除部门 
	 * 2.删除部门和角色关系
	 * 3.解除用户和部门的关系
	 * 4.删除用户和此部门下角色的关系
	 * @param deptId
	 */
	void deleteDeptAndRelatedInfo(Long deptId);

}
