package com.base.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.annotation.DataFilter;
import com.base.common.utils.Constant;
import com.base.common.utils.DateUtils;
import com.base.modules.sys.dao.SysDeptDao;
import com.base.modules.sys.entity.SysDeptEntity;
import com.base.modules.sys.entity.SysRoleDeptEntity;
import com.base.modules.sys.entity.SysRoleEntity;
import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.sys.service.SysDeptService;
import com.base.modules.sys.service.SysRoleDeptService;
import com.base.modules.sys.service.SysRoleService;
import com.base.modules.sys.service.SysUserRoleService;
import com.base.modules.sys.service.SysUserService;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		Wrapper<SysDeptEntity> wrapper = new EntityWrapper<SysDeptEntity>().orderBy("orderNum desc");
		Long userId = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUserId();
//		if(userId != Constant.SUPER_ADMIN){
//			wrapper.eq("create_by",userId);
//		}
		List<SysDeptEntity> deptList =
			this.selectList(wrapper
			.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null && !" ()".equals(params.get(Constant.SQL_FILTER)), (String)params.get(Constant.SQL_FILTER)));

		for(SysDeptEntity sysDeptEntity : deptList){
			SysDeptEntity parentDeptEntity =  this.selectById(sysDeptEntity.getParentId());
			if(parentDeptEntity != null){
				sysDeptEntity.setParentName(parentDeptEntity.getName());
			}else {
				sysDeptEntity.setParentName("");
			}
		}
		return deptList;
	}

	@Override
	public List<SysDeptEntity> getList() {
//		Long queryNumber = baseMapper.queryNumber();
		EntityWrapper<SysDeptEntity> entityWrapper = new EntityWrapper<SysDeptEntity>();
		entityWrapper.eq("del_flag", 0);
//		entityWrapper.eq("number", queryNumber);
        List<SysDeptEntity> sysDeptEntities = this.selectList(entityWrapper);
        return sysDeptEntities;
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

    @Override
    public SysDeptEntity getWdInfo(String id) {
        EntityWrapper<SysDeptEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("dept_id", id);
        return this.selectOne(entityWrapper);
    }

    /**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteDeptAndRelatedInfo(Long deptId) {
		//1.删除部门
		this.deleteById(deptId);
		//4.删除用户和此部门下角色的关系
		//查询此部门下角色
		EntityWrapper<SysRoleDeptEntity> sysRoleDeptWrapper1 = new EntityWrapper<SysRoleDeptEntity>();
		sysRoleDeptWrapper1.eq("dept_id", deptId);
		List<SysRoleDeptEntity> SysRoleDeptList = sysRoleDeptService.selectList(sysRoleDeptWrapper1);
		List<Long> roleIdList=new ArrayList<>();
		for (SysRoleDeptEntity sysRoleDeptEntity : SysRoleDeptList) {
			roleIdList.add(sysRoleDeptEntity.getRoleId());
		}
		if(roleIdList!=null && !roleIdList.isEmpty()) {
			//按部门删除用户和角色的关系
			sysUserRoleService.deleteSysUserRoleByRoles(roleIdList);
		}
		//2.删除部门和角色关系
		EntityWrapper<SysRoleDeptEntity> sysRoleDeptWrapper = new EntityWrapper<SysRoleDeptEntity>();
		sysRoleDeptWrapper.eq("dept_id", deptId);
		sysRoleDeptService.delete(sysRoleDeptWrapper);
		//3.解除用户和部门的关系
		SysUserEntity user =new SysUserEntity();
		user.setDeptId(-1L);
		EntityWrapper<SysUserEntity> userWrapper = new EntityWrapper<SysUserEntity>();
		userWrapper.eq("dept_id", deptId);
		sysUserService.update(user, userWrapper);
		
		//5.解除角色表中的部门id关系
		SysRoleEntity role =new SysRoleEntity();
		EntityWrapper<SysRoleEntity> roleWrapper = new EntityWrapper<SysRoleEntity>();
		role.setDeptId(-1L);
		roleWrapper.eq("dept_id", deptId);
		sysRoleService.update(role, roleWrapper);
	}

	@Override
	public SysDeptEntity getQrCode() {
		long now = DateUtils.dateToLong(new Date(), "HH:mm:ss");//取当前的时分秒
        List<SysDeptEntity> list = this.getList();
        SysDeptEntity rBean = null;
        for (SysDeptEntity entity : list) {
            String showTime = entity.getShowTime();
            Map<String,Object> obj = new HashMap<String,Object>();
            obj.put("showTime",showTime);
            Long queryNumber = baseMapper.queryNumber(obj);
            if (StringUtils.isNotBlank(showTime)) {
                String[] split = showTime.split(" - ");
                long start = DateUtils.strToLong(split[0], "HH:mm:ss");
                long end = DateUtils.strToLong(split[1], "HH:mm:ss");
                if (now >= start && now <= end && queryNumber == Long.valueOf(entity.getNumber())) {
                    rBean = entity;
                    break;
                }
            }
        }
        Map<String,Object> obj = new HashMap<String,Object>();
        obj.put("number", 1);
        obj.put("id", rBean.getDeptId());
        baseMapper.updateCnt(obj);
        return rBean;
	}
}
