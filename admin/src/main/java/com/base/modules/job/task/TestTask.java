package com.base.modules.job.task;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.sys.service.SysUserService;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @since 1.2.0 2016-11-28
 */
@Component("testTask")
public class TestTask {
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
	
	
	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
	/**
	 * 测试定时器，给数据库插入数据
	 * @Title: test03   
	 * @Description: 测试定时器，给数据库插入数据
	 * @author: huanw
	 * @param:       
	 * @return: void
	 * @date:   2019年3月5日 下午6:35:20       
	 * @throws
	 */
	public void testCCC(){
		logger.info("我是案例插入定时任务testCCC方法，正在被执行");
//		CasesEntity cases=new CasesEntity();
//		String caseId = UUIDUtils.getRandomUUID();
//    	cases.setCaseId(caseId);
//    	cases.setCreatetime(new Date());
//    	cases.setCreateby("admin"+caseId.substring(20));
//    	cases.setLastmodifyby("admin"+caseId.substring(20));
//    	cases.setLastmodifytime(new Date());
//		casesService.insert(cases);
//		logger.info("结束----插入案例："+caseId+"   时间："+ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}
	
}
