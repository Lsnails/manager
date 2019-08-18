package com.base.modules.sys.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.sys.entity.SysLogEntity;


import java.util.Map;


/**
 * 系统日志
 * @date 2017-03-08 10:40:56
 */
public interface SysLogService extends IService<SysLogEntity> {

	Page<SysLogEntity> queryPage(Map<String, Object> params);

}
