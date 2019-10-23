package com.base.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.annotation.DataFilter;
import com.base.common.utils.Constant;
import com.base.common.utils.Query;
import com.base.modules.sys.dao.SysRoleDao;
import com.base.modules.sys.entity.SysDeptEntity;
import com.base.modules.sys.entity.SysRoleEntity;
import com.base.modules.sys.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public List<SysRoleEntity> querySysRoleList(SysRoleEntity role) {
		if(role==null){
			return null;
		}
		EntityWrapper<SysRoleEntity> wapper = new EntityWrapper<>();
		if(!"".equals(String.valueOf(role.getDeptId()))){
			wapper.eq("dept_id",role.getDeptId());
		}
		List<SysRoleEntity> sysRoleEntities = this.selectList(wapper);
		return sysRoleEntities;
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public Page<SysRoleEntity> queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");

		Page<SysRoleEntity> page = this.selectPage(
			new Query<SysRoleEntity>(params).getPage(),
			new EntityWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null && !" ()".equals(params.get(Constant.SQL_FILTER)), (String)params.get(Constant.SQL_FILTER))
		);

		for(SysRoleEntity sysRoleEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
		}

		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.insert(role);

		//保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		List<Long> deptIdList = new ArrayList<>();
		deptIdList.add(role.getDeptId());
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), deptIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		// 修改角色部门信息，需清除改角色和用户的关系 wh add
		Long newDeptId = role.getDeptId();
		Long roleId = role.getRoleId();
		SysRoleEntity dbSysRole = this.selectById(roleId);
		if (dbSysRole != null && newDeptId != null) {
			if (!String.valueOf(newDeptId).equals(String.valueOf(dbSysRole.getDeptId()))) {
				Long[] roleIds = { roleId };
				sysUserRoleService.deleteBatch(roleIds);
			}
		}
				
		this.updateAllColumnById(role);

		//更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		List<Long> deptIdList = new ArrayList<>();
		deptIdList.add(role.getDeptId());
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), deptIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		this.deleteBatchIds(Arrays.asList(roleIds));

		//删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		//删除角色与部门关联
		sysRoleDeptService.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);
	}


}
