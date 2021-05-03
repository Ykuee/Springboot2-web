package com.ykuee.datamaintenance.common.redis;

import cn.hutool.core.util.RandomUtil;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 何光跃
 * @description: //TODO
 * @date 2020/7/24 14:06
 */
public class CustomRedisCacheWriter implements RedisCacheWriter {
  private final RedisConnectionFactory connectionFactory;
  private final Duration sleepTime;
  final static String REDIS_EXPIRE_TIME_KEY = "#key_expire_time";

  public CustomRedisCacheWriter(RedisConnectionFactory connectionFactory) {
    this(connectionFactory, Duration.ZERO);
  }

  CustomRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {
    Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
    Assert.notNull(sleepTime, "SleepTime must not be null!");
    this.connectionFactory = connectionFactory;
    this.sleepTime = sleepTime;
  }

  @Override
  public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
    Assert.notNull(name, "Name must not be null!");
    Assert.notNull(key, "Key must not be null!");
    Assert.notNull(value, "Value must not be null!");
    this.execute(name, (connection) -> {
      //当设置了过期时间，则修改取出
      //@Cacheable(cacheNames = "ids_cache#key_expire_time=120",keyGenerator = "myKeyGenerator")
      //name 对应 cacheNames
      //key 对应 MyKeyGenerator 自定义生成的key
      int index = name.lastIndexOf(REDIS_EXPIRE_TIME_KEY);
      int randomInt = RandomUtil.randomInt(1, 100);
      if(index > 0){
        String expireTime = name.substring(index + 1 + REDIS_EXPIRE_TIME_KEY.length());
        connection.set(key, value, Expiration.from(Long.valueOf(expireTime+randomInt), TimeUnit.SECONDS), RedisStringCommands.SetOption.upsert());
      }else if (shouldExpireWithin(ttl)) {
        connection.set(key, value, Expiration.from(ttl.toMillis()+randomInt, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.upsert());
      } else {
        connection.set(key, value);
      }

      return "OK";
    });
  }

  @Override
  public byte[] get(String name, byte[] key) {
    Assert.notNull(name, "Name must not be null!");
    Assert.notNull(key, "Key must not be null!");
    return (byte[])this.execute(name, (connection) -> {
      return connection.get(key);
    });
  }

  @Override
  public byte[] putIfAbsent(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
    Assert.notNull(name, "Name must not be null!");
    Assert.notNull(key, "Key must not be null!");
    Assert.notNull(value, "Value must not be null!");
    return (byte[])this.execute(name, (connection) -> {
      if (this.isLockingCacheWriter()) {
        this.doLock(name, connection);
      }

      byte[] var6;
      try {
        if (connection.setNX(key, value)) {
          if (shouldExpireWithin(ttl)) {
            connection.pExpire(key, ttl.toMillis());
          }

          Object var10 = null;
          return (byte[])var10;
        }

        var6 = connection.get(key);
      } finally {
        if (this.isLockingCacheWriter()) {
          this.doUnlock(name, connection);
        }

      }

      return var6;
    });
  }

  @Override
  public void remove(String name, byte[] key) {
    Assert.notNull(name, "Name must not be null!");
    Assert.notNull(key, "Key must not be null!");
    this.execute(name, (connection) -> {
      return connection.del(new byte[][]{key});
    });
  }

  @Override
  public void clean(String name, byte[] pattern) {
    Assert.notNull(name, "Name must not be null!");
    Assert.notNull(pattern, "Pattern must not be null!");
    this.execute(name, (connection) -> {
      boolean wasLocked = false;

      try {
        if (this.isLockingCacheWriter()) {
          this.doLock(name, connection);
          wasLocked = true;
        }

        byte[][] keys = (byte[][])((Set) Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())).toArray(new byte[0][]);
        if (keys.length > 0) {
          connection.del(keys);
        }
      } finally {
        if (wasLocked && this.isLockingCacheWriter()) {
          this.doUnlock(name, connection);
        }

      }

      return "OK";
    });
  }

  void lock(String name) {
    this.execute(name, (connection) -> {
      return this.doLock(name, connection);
    });
  }

  void unlock(String name) {
    this.executeLockFree((connection) -> {
      this.doUnlock(name, connection);
    });
  }

  private Boolean doLock(String name, RedisConnection connection) {
    return connection.setNX(createCacheLockKey(name), new byte[0]);
  }

  private Long doUnlock(String name, RedisConnection connection) {
    return connection.del(new byte[][]{createCacheLockKey(name)});
  }

  boolean doCheckLock(String name, RedisConnection connection) {
    return connection.exists(createCacheLockKey(name));
  }

  private boolean isLockingCacheWriter() {
    return !this.sleepTime.isZero() && !this.sleepTime.isNegative();
  }

  private <T> T execute(String name, Function<RedisConnection, T> callback) {
    RedisConnection connection = this.connectionFactory.getConnection();

    T var4;
    try {
      this.checkAndPotentiallyWaitUntilUnlocked(name, connection);
      var4 = callback.apply(connection);
    } finally {
      connection.close();
    }

    return var4;
  }

  private void executeLockFree(Consumer<RedisConnection> callback) {
    RedisConnection connection = this.connectionFactory.getConnection();

    try {
      callback.accept(connection);
    } finally {
      connection.close();
    }

  }

  private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {
    if (this.isLockingCacheWriter()) {
      try {
        while(this.doCheckLock(name, connection)) {
          Thread.sleep(this.sleepTime.toMillis());
        }

      } catch (InterruptedException var4) {
        Thread.currentThread().interrupt();
        throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name), var4);
      }
    }
  }

  private static boolean shouldExpireWithin(@Nullable Duration ttl) {
    return ttl != null && !ttl.isZero() && !ttl.isNegative();
  }

  private static byte[] createCacheLockKey(String name) {
    return (name + "~lock").getBytes(StandardCharsets.UTF_8);
  }

}
