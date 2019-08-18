package com.base.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

//import javax.annotation.Resource;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    @Resource(name="redisTemplate")
//    private ValueOperations<String, String> valueOperations;
//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, Object> hashOperations;
//    @Resource(name="redisTemplate")
//    private ListOperations<String, Object> listOperations;
//    @Resource(name="redisTemplate")
//    private SetOperations<String, Object> setOperations;
//    @Resource(name="redisTemplate")
//    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    /**存储对象*/
    
    /**
     * 设置value为对象并且转成json
     * 测试通过
     * @param key
     * @param value
     * @param expire
     */
    public void setObjToJson(String key, Object value, long expire){
    	set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    
    /**
     * 根据key获取对象，同时可以设置此key的有效时间，expire为-1不设置有效时间
     * 测试通过
     * @param key
     * @param value
     * @param expire
     */
    public <T> T getToObj(String key, Class<T> clazz, long expire) {
        String value = get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }
    /**
     * 获取对象
     * 测试通过
     * @param key
     * @param value
     * @param expire
     */
    public <T> T getToObj(String key, Class<T> clazz) {
        return getToObj(key, clazz, NOT_EXPIRE);
    }

    /**String（字符串）*/

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     * 测试通过
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     * 测试通过
     * @param key
     * @param value
     * @param timeout
     *            （以秒为单位）
     */
    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     * 测试通过
     * @param key
     * @return value
     */
    public String get(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }
    /**
     * 根据key获取值，同时可以设置此key的有效时间，expire为-1不设置有效时间
     * 测试通过
     * @param key
     * @param expire
     * @return
     */
    public String get(String key, long expire) {
        String value = (String)redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }
    /**
     * 实现命令：DEL key，删除一个key
     * 
     * @param key
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     * 测试通过
     * @param key
     * @return
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }
    /**
     * 实现命令：expire 设置过期时间，单位秒
     * 测试通过
     * @param key
     * @return
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     * 测试通过
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /** Hash（哈希表） */
    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     * 
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * 
     * @param key
     * @param fields
     */
    public void hdel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     * 
     * @param key
     * @return
     */
    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /** List（列表） */

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     * 
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     * 
     * @param key
     * @return 列表key的头元素。
     */
    public String lpop(String key) {
        return (String)redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     * 
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
    
    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }
}
