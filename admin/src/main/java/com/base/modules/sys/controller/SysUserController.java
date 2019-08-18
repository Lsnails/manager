package com.base.modules.sys.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.annotation.SysLog;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.Assert;
import com.base.common.validator.ValidatorUtils;
import com.base.common.validator.group.AddGroup;
import com.base.common.validator.group.UpdateGroup;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.sys.service.SysUserRoleService;
import com.base.modules.sys.service.SysUserService;
import com.base.modules.sys.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
@Api("用户管理")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		Page<SysUserEntity> page = sysUserService.queryPage(params);

		return PageUtils.convertFrom(page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");

		//原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		user.setPassword(Constant.DEFAULT_PWD);
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		sysUserService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		//获取数据密码目的是，管理员修改用户的时候不需要设置密码，参数user 中没有密码信息
		//1.获取当前用户数据库密码
		Long userId = user.getUserId();
		SysUserEntity dbSysUser = sysUserService.selectById(userId);
		String oldPwd = dbSysUser.getPassword();

		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		//2.修改当前用户
		sysUserService.update(user);

		//3.修改当前用户密码为原密码
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(oldPwd);
		sysUserService.update(userEntity,
				new EntityWrapper<SysUserEntity>().eq("user_id", userId));
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		//1.删除用户
		sysUserService.deleteBatchIds(Arrays.asList(userIds));
		//2.删除用户与角色的关系
		sysUserRoleService.deleteSysUserRoleByUserIds(Arrays.asList(userIds));
		return R.ok();
	}


	/**
	 * 管理员重置密码
	 */
	@SysLog("管理员重置密码")
	@GetMapping("/resetPwd/{userId}")
	@RequiresPermissions("sys:user:update")
	@ApiOperation("管理员重置密码")
	public R resetPwd(@PathVariable String userId){
		//设置默认密码
		SysUserEntity userEntity = sysUserService.selectById(userId);
		userEntity.setPassword(Constant.DEFAULT_PWD);
		
		ValidatorUtils.validateEntity(userEntity, UpdateGroup.class);
		//2.修改密码
		sysUserService.update(userEntity);

		return R.ok();
	}

}
