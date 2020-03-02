package com.base.modules.business.system.datainfo.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.base.modules.business.system.datainfo.entity.BuyInfoEntity;

/**
 * 
 *
 * @author huanw
 * @email 
 * @date 2020-03-02 13:48:17
 */
public interface BuyInfoService extends IService<BuyInfoEntity> {

    Page<BuyInfoEntity> queryPage(Map<String, Object> params);
    
    void importData(MultipartFile file) throws IOException;
}

