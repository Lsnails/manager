package com.base.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * json工具类
 * @ClassName:  JsonUtils
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author:  huanw
 * @date:   2019年8月13日 上午10:00:38
 */
public class JsonUtils {

    /**
     * @Title: fromJson
     * @Description: json转对象
     * @author: huanw
     * @param: @param json  json内容
     * @param: @param clazz
     * @param: @return
     * @return: T
     * @date:   2019年8月13日 上午10:01:04
     * @throws
     */
    public static <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }
    /**
     * @Title: toJson
     * @Description: 对象转json串
     * @author: huanw
     * @param: @param object
     * @param: @return
     * @return: String
     * @date:   2019年8月13日 上午10:02:21
     * @throws
     */
    public static String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    public static Map<String,Object> fromJsonToMap(String json){
        JSONObject parseObject = JSON.parseObject(json);
        Set<String> keySet = parseObject.keySet();
        Map<String,Object> map=new HashMap<>();
        for (String key : keySet) {
            Object object2 = parseObject.get(key);
            map.put(key, object2);
        }
        return map;
    }

}
