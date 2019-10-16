package com.base.utils;

import java.util.UUID;

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

    public static void main(String[] args) {
        System.out.println(getId());
    }
}
