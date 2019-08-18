package com.base.modules.customizesys.dictionary.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.common.utils.R;
import com.base.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 
 * @author huanw
 *
 */
@RestController
@RequestMapping("/hkaom")
@Api(tags="获取文件路径")
public class ContextPathController  extends AbstractController {
	
	@GetMapping("/getContextPath")
    @ApiOperation("获取项根目录")
    public R getContextPath(HttpServletRequest request){
    	String contextPath = request.getContextPath();//项目名称
    	return R.ok().put("contextPath", contextPath);
    }
}
