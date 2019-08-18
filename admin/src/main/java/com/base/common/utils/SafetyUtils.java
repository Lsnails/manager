package com.base.common.utils;

import com.base.common.encrypt.AesEncryptAlgorithm;
import com.base.common.encrypt.EncryptAlgorithm;

import java.util.Map;



public class SafetyUtils {

	private static EncryptAlgorithm encryptAlgorithm = new AesEncryptAlgorithm();
	
	
	/**
	 * @Title: decrypt   
	 * @Description: 解密成对象
	 * @author: huanw
	 * @param: @param data 需要解密的字符串
	 * @param: @param key  加密key
	 * @param: @param clazz 
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: T
	 * @date:   2019年8月13日 上午10:27:10       
	 * @throws
	 */
	public static <T> T decryptToObject(String data,String key,Class<T> clazz) throws Exception {
		String decyptJsonData = encryptAlgorithm.decrypt(data, key);
		
		return JsonUtils.fromJson(decyptJsonData, clazz);
	}
	
	public static Map<String,Object> decryptToMap(String data,String key) throws Exception {
		String decyptJsonData = encryptAlgorithm.decrypt(data, key);
		
		return JsonUtils.fromJsonToMap(decyptJsonData);
	}
	/**
	 * 将字符串解密成字符串
	 * @Title: decryptToString   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @author: huanw
	 * @param: @param data
	 * @param: @param key
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String
	 * @date:   2019年8月13日 下午2:10:15       
	 * @throws
	 */
	public static String decryptToString(String data,String key) throws Exception {
		
		return encryptAlgorithm.decrypt(data, key);
	}
	
	/**
	 * 将对象转成json然后加密
	 * @Title: encryptObject   
	 * @Description: 将对象转成json然后加密
	 * @author: huanw
	 * @param: @param object
	 * @param: @param key
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String
	 * @date:   2019年8月13日 下午1:59:21       
	 * @throws
	 */
	public static String encryptObject(Object object,String key) throws Exception {
		String toJson = JsonUtils.toJson(object);
		
		return  encryptAlgorithm.encrypt(toJson, key);
	}
	
	/**
	 * 加密字符串
	 * @Title: encryptString   
	 * @Description: 加密字符串
	 * @author: huanw
	 * @param: @param data
	 * @param: @param key
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String
	 * @date:   2019年8月13日 下午2:08:36       
	 * @throws
	 */
	public static String encryptString(String data,String key) throws Exception {
		return  encryptAlgorithm.encrypt(data, key);
	}
	
	
}
