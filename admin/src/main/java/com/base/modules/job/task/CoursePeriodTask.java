package com.base.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.base.modules.customizesys.helper.ContentUtils;

/**
 * 课程相关定时任务
 * @ClassName:  CoursePeriodTask   
 * @Description:课程相关定时任务
 * @author:  huanw
 * @date:   2019年3月7日 上午9:46:57
 */
@Component("coursePeriodTask")
public class CoursePeriodTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 通过跑定时任务，开课时间在当天的 状态为0的未开课状态变成已开课
	 * @Title: updateStatusBatchByStartTime   
	 * @Description: 通过跑定时任务，开课时间在当天的 状态为0的未开课状态变成已开课
	 * @author: huanw
	 * @param: @param params      
	 * @return: void
	 * @date:   2019年3月6日 下午7:08:07       
	 * @throws
	 */
	public void updateStatusBatchByStartTime(){
		logger.info("定时修改已经开课的课程周期状态方法updateStatusBatchByStartTime，正在被执行，");
//		coursePeriodService.updateStatusBatchByStartTime("1");//课程周期状态（0：未开课 1：已开课）
		logger.info("结束定时修改课程周期任务，时间："+ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}
}
