package com.base.modules.job.task;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.sys.service.SysUserService;
import com.base.modules.customizesys.helper.ContentUtils;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @since 1.2.0 2016-11-28
 */
@Component("myTestTask")
public class MyTestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysUserService sysUserService;
	
	public void test(String params){
		logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SysUserEntity user = sysUserService.selectById(1L);
		System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	
	
	public void testAAAA(){
		logger.info("王欢自己的方法，正在被执行");
		logger.info("王欢自己的方法，结束时间："+ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}
}
