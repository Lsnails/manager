package com.base.utils;

import java.util.Random;
import java.util.UUID;

import com.base.modules.business.system.wxuser.service.WxUserService;

/**
 * 获取UUID
 * @ClassName:  UUIDUtils   
 * @Description:获取UUID
 * @author:  huanw
 * @date:   2019年1月11日 上午10:55:39
 */
public class UUIDUtils {
	/**
     * 取得uuid
     * @return uuid串
     */
    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }

    public static String getId(){
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13);
    }
    
    public static String getRandom(int length,WxUserService wxUserService){
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(random.nextInt(10));
		}
		boolean exist = wxUserService.isExist(val);
		if(exist) {
			getRandom(length , wxUserService);
		}
		return val;
	}

    public static void main(String[] args) {
//        System.out.println(getRandom(8));
    }
}
