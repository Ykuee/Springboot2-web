package com.ykuee.datamaintenance.common.support;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.shiro.prop.JWTProp;

import cn.hutool.json.JSONUtil;

/**
 * 
  * @version:
  * @Description: TODO(描述这个类的作用) 
  * @author: Ykuee
  * @date: 2021-3-26 16:28:20
 */
@Component
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    public void setJedisCluster(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }
    

    private static JWTProp jwtProp;
    
    @Autowired
    public void setJwtProp(JWTProp jwtProp) {
        RedisUtil.jwtProp = jwtProp;
    }
    /**
     * 
      *<p>Title: getJedis</p>
      *<p>Description: 获取Jedis实例</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @return
     */
    public static synchronized RedisTemplate getJedis() {
        try {
            if (redisTemplate != null) {
                return redisTemplate;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException("获取Jedis资源异常:" + e.getMessage());
        }
    }


    /**
     * 
      *<p>Title: getObject</p>
      *<p>Description: 获取redis键值-object</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @return
     */
    public static Object getObject(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw new BusinessException("获取Redis键值getObject方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 
      *<p>Title: setObject</p>
      *<p>Description: 设置redis键值-object</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @param value
      * @return
     */
    public static boolean setObject(String key, Object value) {
        try {
        	redisTemplate.opsForValue().set(key, value);
        	return true;
        } catch (Exception e) {
            throw new BusinessException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 
      *<p>Title: setObject</p>
      *<p>Description: 设置redis键值-object-expiretime</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @param value
      * @param expiretime
      * @return
     */
    public static boolean setObject(String key, Object value, int expiretime) {
        try {
            redisTemplate.opsForValue().set(key, value,expiretime,TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            throw new BusinessException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }
    /**
     * 
     *<p>Title: setObject</p>
     *<p>Description: 设置redis键值-object-expiretime</p>
     * @author Ykuee
     * @date 2021-3-2 
     * @param key
     * @param value
     * @param expiretime
     * @return
     */
    public static boolean setObjectShiroExpiTime(String key, Object value) {
    	try {
    		return setObject(key,value,Integer.valueOf(jwtProp.getShiroCacheExpireTime()));
    	} catch (Exception e) {
    		throw new BusinessException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
    	}
    }
    
    public static boolean setObjectRefreshExpiTime(String key, Object value) {
    	try {
    		return setObject(key,value,Integer.valueOf(jwtProp.getRefreshTokenExpireTime()));
    	} catch (Exception e) {
    		throw new BusinessException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
    	}
    }

    /**
     * 
      *<p>Title: getJson</p>
      *<p>Description: 获取redis键值-Json</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @return
     */
    public static String getJson(String key) {
        try {
            return JSONUtil.toJsonStr(getObject(key));
        } catch (Exception e) {
            throw new BusinessException("获取Redis键值getJson方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }


    /**
     * 
      *<p>Title: delKey</p>
      *<p>Description: 删除key</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @return
     */
    public static boolean delKey(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            throw new BusinessException("删除Redis的键delKey方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 
      *<p>Title: exists</p>
      *<p>Description: key是否存在</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @return
     */
    public static boolean exists(String key) {
        try{
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new BusinessException("查询Redis的键是否存在exists方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }


    /**
     * 
      *<p>Title: ttl</p>
      *<p>Description: 获取过期剩余时间</p>
      * @author Ykuee
      * @date 2021-3-2 
      * @param key
      * @return
     */
    public static Long getExpire(String key) {
        Long result = -2L;
        try {
            result = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return result;
        } catch (Exception e) {
            throw new BusinessException("获取Redis键过期剩余时间ttl方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }
    
    public static Long deleteAll() {
    	Long result = 0L;
    	try {
    		Set<String> keys = redisTemplate.keys("*");
    		if(keys!=null && keys.size()>0) {
    			result = redisTemplate.delete(keys);
    		}
    		return result;
    	} catch (Exception e) {
    		throw new BusinessException("Redis清空所有缓存deleteAll方法异常:  cause=" + e.getMessage());
    	}
    }
    
    public static Long deleteKeys(String key) {
    	Long result = -2L;
    	try {
    		Set<String> keys = redisTemplate.keys(key);
    		result = redisTemplate.delete(keys);
    		return result;
    	} catch (Exception e) {
    		throw new BusinessException("Redis清空所有缓存deleteAll方法异常:  cause=" + e.getMessage());
    	}
    }
    
    public static Set<String> getAllKeys() {
    	try {
    		return redisTemplate.keys("*");
    	} catch (Exception e) {
    		throw new BusinessException("Redis获取所有key getAllKeys方法异常:  cause=" + e.getMessage());
    	}
    }
    
    public static Set<String> getKeys(String keys) {
    	try {
    		return redisTemplate.keys(keys);
    	} catch (Exception e) {
    		throw new BusinessException("Redis获取所有key getAllKeys方法异常:  cause=" + e.getMessage());
    	}
    }
    
    public static void batchSet(Map<String, String> cacheMap) {
    	try {
    		redisTemplate.opsForValue().multiSet(cacheMap);
    	} catch (Exception e) {
    		throw new BusinessException("Redis批量set batchSet方法异常:  cause=" + e.getMessage());
    	}
    }
}
