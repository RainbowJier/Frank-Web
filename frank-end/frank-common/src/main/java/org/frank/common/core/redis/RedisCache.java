package org.frank.common.core.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.exception.RedisOperationException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author Frank
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings({"unchecked"})
public class RedisCache {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis缓存设置失败，key: {}", key, e);
            throw new RedisOperationException("缓存设置失败", e);
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Redis缓存设置失败，key: {}, timeout: {}", key, timeout, e);
            throw new RedisOperationException("缓存设置失败", e);
        }
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        try {
            ValueOperations<String, Object> operation = redisTemplate.opsForValue();
            return (T) operation.get(key);
        } catch (Exception e) {
            log.error("Redis缓存获取失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     */
    public boolean deleteObjects(final Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> long setCacheSet(final String key, final Set<T> dataSet) {
        if (dataSet == null || dataSet.isEmpty()) {
            return 0;
        }
        try {
            Long count = redisTemplate.opsForSet().add(key, dataSet.toArray());
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("Redis Set缓存设置失败，key: {}", key, e);
            throw new RedisOperationException("Set缓存设置失败", e);
        }
    }
    
    /**
     * 获得缓存的Set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        try {
            return (Set<T>) redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis Set缓存获取失败，key: {}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <K, V> Map<K, V> getCacheMap(final String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        return (T) opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    /**
     * 使用scan命令获取键，避免在生产中使用keys
     * @param pattern
     * @return
     */
    public Set<String> scan(String pattern) {
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<String> keys = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build())) {
                cursor.forEachRemaining(item -> keys.add(new String(item, StandardCharsets.UTF_8)));
            }
            return keys;
        });
    }

    /**
     * 清除指定前缀的所有缓存
     * <p>
     * 使用 `SCAN` 命令进行迭代删除, 避免阻塞.
     * </p>
     *
     * @param prefix 前缀
     * @return 清除的键数量
     */
    public long clearCacheByPrefix(String prefix) {
        try {
            Set<String> keys = scan(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                return deleteObjects(keys) ? keys.size() : 0;
            }
            return 0;
        } catch (Exception e) {
            log.error("Redis清除缓存失败，prefix: {}", prefix, e);
            return 0;
        }
    }

    // =========================== 原子操作 ===========================

    /**
     * 原子操作 - 如果key不存在则设置值
     *
     * @param key   键
     * @param value 值
     * @return 设置成功返回true，否则返回false
     */
    public <T> boolean setIfAbsent(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit));
        } catch (Exception e) {
            log.error("Redis原子操作失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 递增操作
     *
     * @param key   键
     * @param delta 递增量
     * @return 递增后的值
     */
    public long increment(final String key, final long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis递增操作失败，key: {}", key, e);
            throw new RedisOperationException("递增操作失败", e);
        }
    }

    /**
     * 递减操作
     *
     * @param key   键
     * @param delta 递减量
     * @return 递减后的值
     */
    public long decrement(final String key, final long delta) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis递减操作失败，key: {}", key, e);
            throw new RedisOperationException("递减操作失败", e);
        }
    }
    
    // =========================== 分布式锁 ===========================

    /**
     * 分布式锁 - 获取锁
     *
     * @param lockKey    锁键
     * @param requestId  请求标识
     * @param expireTime 过期时间(秒)
     * @return 是否获取成功
     */
    public boolean tryLock(final String lockKey, final String requestId, final long expireTime) {
        return setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 分布式锁 - 释放锁
     *
     * @param lockKey   锁键
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(final String lockKey, final String requestId) {
        try {
            // 使用Lua脚本确保原子性
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
            return result > 0;
        } catch (Exception e) {
            log.error("Redis释放分布式锁失败，lockKey: {}", lockKey, e);
            return false;
        }
    }
}