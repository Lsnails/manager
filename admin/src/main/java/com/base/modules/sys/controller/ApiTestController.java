package com.base.modules.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.base.modules.sys.entity.SysUserEntity;

import com.base.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags="测试接口")
public class ApiTestController {
	
	    @PostMapping("testlogin")
	    @ApiOperation("登录001")
	    @ResponseBody
	    public R login(String username, String password){
	    	List<SysUserEntity> list = new ArrayList<SysUserEntity>();
	    	for (int i = 0; i < 5; i++) {
	    		SysUserEntity sysUserEntity =new SysUserEntity();
	    		sysUserEntity.setUsername(username+"_"+i);
	    		sysUserEntity.setPassword(password+"_"+i);
	    		list.add(sysUserEntity);
			}
	        //用户登录
	        Map<String, Object> map = new HashMap<>();
	        map.put("data", list);
	        return R.ok(map);
	    }
}
