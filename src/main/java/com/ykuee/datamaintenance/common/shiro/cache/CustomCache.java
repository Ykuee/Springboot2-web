package com.ykuee.datamaintenance.common.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.support.JwtUtil;
import com.ykuee.datamaintenance.common.support.RedisUtil;

/**
 * 
  * @version:
  * @Description: 重写Shiro的Cache保存读取
  * @author: Ykuee
  * @date: 2021-3-3 15:26:26
  * @param <K>
  * @param <V>
 */
public class CustomCache<K,V> implements Cache<K,V> {


	/**
	 * 
	  *<p>Title: getKey</p>
	  *<p>Description: 缓存的key名称获取为shiro:cache:loginName</p>
	  * @author Ykuee
	  * @date 2021-3-3 
	  * @param key
	  * @return
	 */
	private String getKey(Object key) {
        return Constant.PREFIX_SHIRO_CACHE + JwtUtil.getClaim(key.toString(), Constant.LOGIN_NAME);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(Boolean.FALSE.equals(RedisUtil.exists(this.getKey(key)))){
            return null;
        }
        return RedisUtil.getObject(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 设置Redis的Shiro缓存
        return RedisUtil.setObjectShiroExpiTime(this.getKey(key), value);
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(Boolean.FALSE.equals(RedisUtil.exists(this.getKey(key)))){
            return null;
        }
        RedisUtil.delKey(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        RedisUtil.deleteKeys(Constant.PREFIX_SHIRO+"*");
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        int size = Objects.requireNonNull(RedisUtil.getKeys(Constant.PREFIX_SHIRO+"*")).size();
    	return size;
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        Set<String> keys = Objects.requireNonNull(RedisUtil.getAllKeys());
        return keys;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(RedisUtil.getObject(this.getKey(key)));
        }
        return values;
    }
}
