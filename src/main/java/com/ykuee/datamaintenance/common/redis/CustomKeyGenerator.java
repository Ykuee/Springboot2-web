package com.ykuee.datamaintenance.common.redis;

import com.google.common.collect.Maps;

import cn.hutool.json.JSONUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 何光跃
 * @description: //TODO
 * @date 2020/7/24 10:15
 */
@Component
public class CustomKeyGenerator implements KeyGenerator {

  @Override
  public Object generate(Object target, Method method, Object... params) {
    Map<String, Object> container = Maps.newHashMap();
    Class<?> targetClassClass = target.getClass();
    container.put("class",targetClassClass.toGenericString());
    container.put("methodName",method.getName());
    container.put("package",targetClassClass.getPackage());
    for (int i = 0; i < params.length; i++) {
      container.put(String.valueOf(i),params[i]);
    }
    return DigestUtils.sha256Hex(JSONUtil.toJsonStr(container));
  }




}
