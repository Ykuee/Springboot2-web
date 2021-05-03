package com.ykuee.datamaintenance.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * 
  * @version:
  * @Description: 重写Shiro缓存管理器
  * @author: Ykuee
  * @date: 2021-3-3 15:29:21
 */
public class CustomCacheManager implements CacheManager {
	
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new CustomCache<K,V>();
    }
}
