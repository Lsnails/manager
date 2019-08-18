package com.base.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.base.modules.sys.entity.SysUserRoleEntity;

import java.util.List;


/**
 * 用户与角色对应关系
 * @date 2016年9月18日 上午9:43:24
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {
	
	void saveOrUpdate(Long userId, List<Long> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
	/**
	 * 根据用户ids，删除用户与角色的关系
	 */
	void deleteSysUserRoleByUserIds(List<Long> userIds);
}
