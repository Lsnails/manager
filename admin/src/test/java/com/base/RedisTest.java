package com.base;

import com.base.common.utils.RedisUtils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.customizesys.helper.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void contextLoads() {
        SysUserEntity user = new SysUserEntity();
        user.setEmail("123456测试稿呜呜呜呜@qq.com");
        redisUtils.setObjToJson("user", user,1*60*2);

        SysUserEntity sysUserEntity = redisUtils.getToObj("user", SysUserEntity.class);
//        redisUtils.getToObj("user", SysUserEntity.class, 1*60*10);
		System.out.println(ToStringBuilder.reflectionToString(sysUserEntity));
        String mobile="15321772160";
        String key=mobile+Constant.VC;
        String mobileVerificationCode="99999";
        redisUtils.set(key, mobileVerificationCode);
        redisUtils.set(key+"001", mobileVerificationCode, 1*60*2);
        String value1 = redisUtils.get(key);
        
        System.out.println(value1 +"第一次剩余时间："+ redisUtils.ttl(key));
        System.out.println(sysUserEntity.getEmail() +"第一次剩余时间："+ redisUtils.ttl(key));
        
        redisUtils.expire("user",1*60*10);
        System.out.println("user剩余时间："+ redisUtils.ttl("user"));
        System.out.println("全部key："+ redisUtils.keys("*").toString());
//        String value2 = redisUtils.get(key, 1*60*5);
//        System.out.println(value2);
//        System.out.println(value2 +"第二次剩余时间："+ redisUtils.ttl(key));
    }

}
