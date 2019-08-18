package com.base.modules.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @date 2016年11月24日 下午11:05:27
 */
@Controller
public class SysPageController {
	
	/**
	 * 设置多模块跳转
	 * @param modules
	 * @param module
	 * @param url
	 * @return
	 * @author huanw
	 */
	@RequestMapping("{modules}/{module}/{url}.html")
	public String module(@PathVariable("modules") String modules,@PathVariable("module") String module,
			@PathVariable("url") String url){
		return modules+"/" + module + "/" + url;
	}
	@RequestMapping("{modules}/{url}.html")
	public String module(@PathVariable("modules") String modules,
			@PathVariable("url") String url){
		return modules+"/"+ url;
	}
	
//	@RequestMapping("modules/{module}/{url}.html")
//	public String module(@PathVariable("module") String module, @PathVariable("url") String url){
//		return "modules/" + module + "/" + url;
//	}
	/**
	 * 默认到项目名称下的，走项目/index.html
	 * @Title: index1   
	 * @Description: 默认到项目名称下的，走项目/index.html
	 * @author: huanw
	 * @param: @return      
	 * @return: String
	 * @date:   2019年4月18日 下午1:02:32       
	 * @throws
	 */
//	@RequestMapping(value = {"/", "index.html"})
//	public String index(){
//		return "index";
//	}

	@RequestMapping("index.html")
	public String index1(){
		return "index";
	}

	@RequestMapping("hkaoMLogin.html")
	public String login(){
		return "hkaoMLogin";
	}

	@RequestMapping("main.html")
	public String main(){
		return "main";
	}

	@RequestMapping("404.html")
	public String notFound(){
		return "404";
	}
	
	@RequestMapping("500.html")
	public String error500(){
		return "500";
	}

}
