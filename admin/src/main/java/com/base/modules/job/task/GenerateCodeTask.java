package com.base.modules.job.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.modules.business.system.config.service.CodeService;
import com.base.modules.customizesys.helper.ContentUtils;

/**
 * 生成日期对应code（每天生成一次
 * @author huanw
 *
 */
@Component("generateCodeTask")
public class GenerateCodeTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeService codeService;
	
	/**
	 * 生成日期对应code（每天生成一次）-- 每天凌晨1点执行一次
	 */
	public void generateCode(){
		logger.info("生成日期对应code（每天生成一次）-- 每天凌晨1点执行一次，正在被执行，");
		Date currentDate =new Date();
		codeService.queryCodeEntityByDate(ContentUtils.getDateToString(currentDate, "yyyy-MM-dd"));
		logger.info("结束生成日期对应code任务，时间："+ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}
}
