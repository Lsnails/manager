package com.base.modules.business.system.config.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.base.common.utils.PageUtils;
import com.base.common.utils.Query;

import com.base.modules.business.system.config.dao.CodeDao;
import com.base.modules.business.system.config.entity.CodeEntity;
import com.base.modules.business.system.config.service.CodeService;
import com.base.modules.customizesys.helper.ContentUtils;
import com.base.utils.UUIDUtils;


@Service("codeService")
public class CodeServiceImpl extends ServiceImpl<CodeDao, CodeEntity> implements CodeService {
//http://localhost:8080/admin/swagger-ui.html#!/27979357972550921475/loginUsingPOST
    @Override
    public Page<CodeEntity> queryPage(Map<String, Object> params) {
        Page<CodeEntity> page = this.selectPage(
                new Query<CodeEntity>(params).getPage(),
                new EntityWrapper<CodeEntity>()
        );

        return page;
    }

	@Override
	public void insertCodeEntity(CodeEntity codeEntity) {
		codeEntity.setCreateDate(new Date());
		codeEntity.setUpdateDate(codeEntity.getCreateDate());
        this.insert(codeEntity);
	}

	@Override
	public CodeEntity queryCodeEntityByDate(String date) {
		CodeEntity selectOne =null;
		EntityWrapper<CodeEntity> entityWrapper = new EntityWrapper<CodeEntity>();
		if(StringUtils.isNotBlank(date)) {
			entityWrapper.eq("date", date);
			selectOne = this.selectOne(entityWrapper);
		}
		if(selectOne == null){
			selectOne = new CodeEntity();
			String uuId = UUIDUtils.getRandomUUID();
			selectOne.setId(uuId);
			selectOne.setDate(ContentUtils.getStringToDate(date));
			this.insertCodeEntity(selectOne);
			selectOne = this.selectById(uuId);
		}
		return selectOne;
	} 
    
}
