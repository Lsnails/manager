package com.base.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AfterServiceStarted implements ApplicationRunner{
	protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 会在服务启动完成后立即执行
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

    	logger.info("Successful service startup!");
    }
}