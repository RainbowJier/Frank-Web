package org.frank.common.core.redis;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * spring redis 工具类
 *
 * @author Frank
 **/
@Slf4j
@Component
public class RedisCache {

    @Resource
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis缓存设置失败，key: {}", key, e);
            throw new RuntimeException("缓存设置失败", e);
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
    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Redis缓存设置失败，key: {}, timeout: {}", key, timeout, e);
            throw new RuntimeException("缓存设置失败", e);
        }
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(String key, long timeout) {
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
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(String key) {
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
    public <T> T getCacheObject(String key) {
        try {
            ValueOperations<String, T> operation = redisTemplate.opsForValue();
            return operation.get(key);
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
    public boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(Collection collection) {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(String key, List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(String key, Map<String, T> dataMap) {
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
    public <T> Map<String, T> getCacheMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(String key, String hKey, T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(String key, String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(String key, String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    // =========================== 新增功能方法 ===========================

    /**
     * 原子操作 - 如果key不存在则设置值
     *
     * @param key   键
     * @param value 值
     * @return 设置成功返回true，否则返回false
     */
    public <T> boolean setIfAbsent(String key, T value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
        } catch (Exception e) {
            log.error("Redis原子操作失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 原子操作 - 如果key存在则设置值
     *
     * @param key   键
     * @param value 值
     * @return 设置成功返回true，否则返回false
     */
    public <T> boolean setIfPresent(String key, T value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfPresent(key, value));
        } catch (Exception e) {
            log.error("Redis原子操作失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 批量设置缓存
     *
     * @param keyValueMap 键值对
     */
    public <T> void batchSetCacheObject(Map<String, T> keyValueMap) {
        if (keyValueMap != null && !keyValueMap.isEmpty()) {
            try {
                redisTemplate.opsForValue().multiSet(keyValueMap);
            } catch (Exception e) {
                log.error("Redis批量设置缓存失败", e);
                throw new RuntimeException("批量设置缓存失败", e);
            }
        }
    }

    /**
     * 批量获取缓存
     *
     * @param keys 键集合
     * @return 值列表
     */
    public <T> List<T> batchGetCacheObject(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            log.error("Redis批量获取缓存失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取并设置新值
     *
     * @param key      键
     * @param newValue 新值
     * @return 旧值
     */
    public <T> T getAndSet(String key, T newValue) {
        try {
            return (T) redisTemplate.opsForValue().getAndSet(key, newValue);
        } catch (Exception e) {
            
            log.error("Redis获取并设置失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 递增操作
     *
     * @param key   键
     * @param delta 递增量
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            
            log.error("Redis递增操作失败，key: {}", key, e);
            throw new RuntimeException("递增操作失败", e);
        }
    }

    /**
     * 递减操作
     *
     * @param key   键
     * @param delta 递减量
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        try {
            return redisTemplate.opsForValue().decrement(key, delta);
        } catch (Exception e) {
            
            log.error("Redis递减操作失败，key: {}", key, e);
            throw new RuntimeException("递减操作失败", e);
        }
    }

    /**
     * 获取原子计数器
     *
     * @param key 键
     * @return RedisAtomicLong对象
     */
    public RedisAtomicLong getAtomicCounter(String key) {
        return new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
    }

    /**
     * 添加元素到List头部
     *
     * @param key   键
     * @param value 值
     * @return 操作后的列表长度
     */
    public <T> long leftPush(String key, T value) {
        try {
            Long result = redisTemplate.opsForList().leftPush(key, value);
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis左推操作失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 添加元素到List尾部
     *
     * @param key   键
     * @param value 值
     * @return 操作后的列表长度
     */
    public <T> long rightPush(String key, T value) {
        try {
            Long result = redisTemplate.opsForList().rightPush(key, value);
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis右推操作失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 从List头部弹出元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public <T> T leftPop(String key) {
        try {
            return (T) redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            
            log.error("Redis左弹出操作失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 从List尾部弹出元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public <T> T rightPop(String key) {
        try {
            return (T) redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            
            log.error("Redis右弹出操作失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取List长度
     *
     * @param key 键
     * @return 列表长度
     */
    public long listSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            
            log.error("Redis获取列表长度失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 获取List指定索引的元素
     *
     * @param key   键
     * @param index 索引
     * @return 元素
     */
    public <T> T listIndex(String key, long index) {
        try {
            return (T) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            
            log.error("Redis获取列表索引元素失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 向Set添加元素
     *
     * @param key    键
     * @param values 值集合
     * @return 添加成功的数量
     */
    public <T> long setAdd(String key, Set<T> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        try {
            Long result = redisTemplate.opsForSet().add(key, values.toArray());
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis添加Set元素失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 从Set中移除元素
     *
     * @param key    键
     * @param values 值集合
     * @return 移除成功的数量
     */
    public <T> long setRemove(String key, Set<T> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        try {
            Long result = redisTemplate.opsForSet().remove(key, values.toArray());
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis移除Set元素失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 获取Set大小
     *
     * @param key 键
     * @return Set大小
     */
    public long setSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            
            log.error("Redis获取Set大小失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 判断Set是否包含某个值
     *
     * @param key   键
     * @param value 值
     * @return 是否包含
     */
    public <T> boolean setIsMember(String key, T value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            
            log.error("Redis判断Set成员失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取Hash大小
     *
     * @param key 键
     * @return Hash大小
     */
    public long hashSize(String key) {
        try {
            Long size = redisTemplate.opsForHash().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            
            log.error("Redis获取Hash大小失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 判断Hash是否包含某个键
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 是否包含
     */
    public boolean hashHasKey(String key, String hashKey) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, hashKey));
        } catch (Exception e) {
            
            log.error("Redis判断Hash键失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取Hash所有键
     *
     * @param key 键
     * @return 键集合
     */
    public Set<Object> hashKeys(String key) {
        try {
            return redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            
            log.error("Redis获取Hash键失败，key: {}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 获取Hash所有值
     *
     * @param key 键
     * @return 值集合
     */
    public List<Object> hashValues(String key) {
        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            
            log.error("Redis获取Hash值失败，key: {}", key, e);
            return Collections.emptyList();
        }
    }

    // =========================== 高级功能 ===========================

    /**
     * 分布式锁 - 获取锁
     *
     * @param lockKey    锁键
     * @param requestId  请求标识
     * @param expireTime 过期时间(秒)
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS));
        } catch (Exception e) {
            
            log.error("Redis获取分布式锁失败，lockKey: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 分布式锁 - 释放锁
     *
     * @param lockKey   锁键
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        try {
            // 使用Lua脚本确保原子性
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
            if (result instanceof Long) {
                return ((Long) result) > 0;
            }
            return false;
        } catch (Exception e) {
            
            log.error("Redis释放分布式锁失败，lockKey: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 缓存穿透保护 - 缓存空值
     *
     * @param key 键
     */
    public void cacheNullValue(String key) {
        try {
            redisTemplate.opsForValue().set(key, "", 300, TimeUnit.SECONDS); // 缓存5分钟
        } catch (Exception e) {
            
            log.error("Redis缓存空值失败，key: {}", key, e);
        }
    }

    /**
     * 判断是否为缓存的空值
     *
     * @param key 键
     * @return 是否为空值
     */
    public boolean isNullValue(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            return "".equals(value);
        } catch (Exception e) {
            
            log.error("Redis判断空值失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 缓存预热 - 异步批量设置
     *
     * @param keyValueMap 键值对
     * @param timeout     过期时间
     * @param timeUnit    时间单位
     */
    public <T> CompletableFuture<Void> warmUpCacheAsync(Map<String, T> keyValueMap, long timeout, TimeUnit timeUnit) {
        return CompletableFuture.runAsync(() -> {
            keyValueMap.forEach((key, value) -> {
                try {
                    redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
                    log.debug("缓存预热成功: {}", key);
                } catch (Exception e) {
                    log.error("缓存预热失败: {}", key, e);
                    
                }
            });
        });
    }

    /**
     * 获取缓存并自动续期
     *
     * @param key      键
     * @param timeout  续期时间
     * @param timeUnit 时间单位
     * @return 缓存值
     */
    public <T> T getCacheObjectWithAutoRenewal(String key, long timeout, TimeUnit timeUnit) {
        try {
            T value = getCacheObject(key);
            if (value != null) {
                expire(key, timeout, timeUnit);
            }
            return value;
        } catch (Exception e) {
            
            log.error("Redis获取缓存并续期失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 计数器限流
     *
     * @param key        限流键
     * @param maxCount   最大请求数
     * @param timeWindow 时间窗口(秒)
     * @return 是否允许通过
     */
    public boolean rateLimitByCounter(String key, int maxCount, int timeWindow) {
        try {
            Long current = redisTemplate.opsForValue().increment(key);
            if (current != null && current == 1) {
                redisTemplate.expire(key, timeWindow, TimeUnit.SECONDS);
            }
            return current != null && current <= maxCount;
        } catch (Exception e) {
            
            log.error("Redis计数器限流失败，key: {}", key, e);
            return true; // 发生异常时默认允许通过
        }
    }

    /**
     * 滑动窗口限流
     *
     * @param key        限流键
     * @param maxCount   最大请求数
     * @param timeWindow 时间窗口(秒)
     * @return 是否允许通过
     */
    public boolean rateLimitBySlidingWindow(String key, int maxCount, int timeWindow) {
        try {
            long currentTime = System.currentTimeMillis();
            long windowStart = currentTime - (timeWindow * 1000L);

            // 使用ZSet实现滑动窗口
            redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);
            redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);
            redisTemplate.expire(key, timeWindow, TimeUnit.SECONDS);

            Long count = redisTemplate.opsForZSet().size(key);
            return count != null && count <= maxCount;
        } catch (Exception e) {
            
            log.error("Redis滑动窗口限流失败，key: {}", key, e);
            return true; // 发生异常时默认允许通过
        }
    }

    /**
     * 执行Lua脚本
     *
     * @param script     Lua脚本
     * @param keys       键列表
     * @param args       参数列表
     * @param resultType 返回类型
     * @return 执行结果
     */
    public <T> T executeLuaScript(String script, List<String> keys, List<Object> args, Class<T> resultType) {
        try {
            DefaultRedisScript<T> redisScript = new DefaultRedisScript<>(script, resultType);
            return (T) redisTemplate.execute(redisScript, keys, args);
        } catch (Exception e) {
            
            log.error("Redis执行Lua脚本失败", e);
            throw new RuntimeException("执行Lua脚本失败", e);
        }
    }

    /**
     * 发布消息
     *
     * @param channel 频道
     * @param message 消息
     */
    public void publish(String channel, Object message) {
        try {
            redisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            
            log.error("Redis发布消息失败，channel: {}", channel, e);
        }
    }

    /**
     * 清除指定前缀的所有缓存
     *
     * @param prefix 前缀
     * @return 清除的键数量
     */
    public long clearCacheByPrefix(String prefix) {
        try {
            Collection<String> keys = redisTemplate.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                return redisTemplate.delete(keys);
            }
            return 0;
        } catch (Exception e) {
            
            log.error("Redis清除缓存失败，prefix: {}", prefix, e);
            return 0;
        }
    }

    /**
     * 获取Redis连接信息
     *
     * @return 连接信息
     */
    public String getConnectionInfo() {
        try {
            return String.format("Redis连接信息 - 连接工厂: %s",
                    redisTemplate.getConnectionFactory().getClass().getSimpleName());
        } catch (Exception e) {
            return "Redis连接信息获取失败: " + e.getMessage();
        }
    }

    // =========================== Geo地理空间操作 ===========================

    /**
     * 添加地理位置
     *
     * @param key       键
     * @param member    成员
     * @param longitude 经度
     * @param latitude  纬度
     * @return 添加成功的数量
     */
    public long geoAdd(String key, String member, double longitude, double latitude) {
        try {
            Long result = redisTemplate.opsForGeo().add(key,
                    new RedisGeoCommands.GeoLocation<>(member,
                            new Point(longitude, latitude)));
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis添加地理位置失败，key: {}, member: {}", key, member, e);
            return 0;
        }
    }

    /**
     * 获取地理位置
     *
     * @param key     键
     * @param members 成员列表
     * @return 地理位置列表
     */
    public List<Point> geoPos(String key, String... members) {
        try {
            return redisTemplate.opsForGeo().position(key, Arrays.asList(members));
        } catch (Exception e) {
            
            log.error("Redis获取地理位置失败，key: {}", key, e);
            return Collections.emptyList();
        }
    }

    /**
     * 计算两个位置之间的距离
     *
     * @param key     键
     * @param member1 成员1
     * @param member2 成员2
     * @param unit    距离单位
     * @return 距离
     */
    public Double geoDist(String key, String member1, String member2, RedisGeoCommands.DistanceUnit unit) {
        try {
            Distance distance = redisTemplate.opsForGeo().distance(key, member1, member2, unit);
            return distance != null ? distance.getValue() : null;
        } catch (Exception e) {
            
            log.error("Redis计算距离失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取指定半径内的位置
     *
     * @param key    键
     * @param member 中心成员
     * @param radius 半径
     * @param unit   距离单位
     * @return 位置列表
     */
    public List<GeoResults<RedisGeoCommands.GeoLocation<String>>> geoRadiusByMember(
            String key, String member, double radius, RedisGeoCommands.DistanceUnit unit) {
        try {
            return Collections.singletonList(redisTemplate.opsForGeo().radius(key, member,
                    new Distance(radius, unit)));
        } catch (Exception e) {
            
            log.error("Redis获取半径内位置失败，key: {}", key, e);
            return Collections.emptyList();
        }
    }

    // =========================== HyperLogLog基数统计 ===========================

    /**
     * 添加元素到HyperLogLog
     *
     * @param key    键
     * @param values 值集合
     * @return 是否成功
     */
    public boolean hyperLogLogAdd(String key, String... values) {
        try {
            Long result = redisTemplate.opsForHyperLogLog().add(key, values);
            return result != null && result > 0;
        } catch (Exception e) {
            
            log.error("Redis HyperLogLog添加失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取HyperLogLog的基数
     *
     * @param key 键
     * @return 基数
     */
    public long hyperLogLogSize(String key) {
        try {
            Long size = redisTemplate.opsForHyperLogLog().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            
            log.error("Redis HyperLogLog获取基数失败，key: {}", key, e);
            return 0;
        }
    }

    // =========================== Bitmap位图操作 ===========================

    /**
     * 设置位图的指定位
     *
     * @param key    键
     * @param offset 偏移量
     * @param value  值(true/false)
     * @return 是否成功
     */
    public boolean bitmapSetBit(String key, long offset, boolean value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setBit(key, offset, value));
        } catch (Exception e) {
            
            log.error("Redis Bitmap设置位失败，key: {}, offset: {}", key, offset, e);
            return false;
        }
    }

    /**
     * 获取位图的指定位
     *
     * @param key    键
     * @param offset 偏移量
     * @return 位的值
     */
    public boolean bitmapGetBit(String key, long offset) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(key, offset));
        } catch (Exception e) {
            
            log.error("Redis Bitmap获取位失败，key: {}, offset: {}", key, offset, e);
            return false;
        }
    }

    /**
     * 计算位图中值为1的位数
     *
     * @param key 键
     * @return 1的位数
     */
    public long bitmapBitCount(String key) {
        try {
            Object result = redisTemplate.execute(
                    RedisScript.of("return redis.call('bitcount', KEYS[1])", Long.class),
                    Collections.singletonList(key)
            );
            if (result instanceof Long) {
                return (Long) result;
            }
            return 0;
        } catch (Exception e) {
            
            log.error("Redis Bitmap计算位数失败，key: {}", key, e);
            return 0;
        }
    }

    // =========================== ZSet有序集合操作 ===========================

    /**
     * 添加元素到有序集合
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否成功
     */
    public boolean zSetAdd(String key, String value, double score) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
        } catch (Exception e) {
            
            log.error("Redis ZSet添加失败，key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 获取有序集合指定范围的元素
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 元素集合
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            
            log.error("Redis ZSet获取范围失败，key: {}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 获取有序集合指定分数范围的元素
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    public Set<Object> zSetRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            
            log.error("Redis ZSet按分数获取范围失败，key: {}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 获取元素的分数
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    public Double zSetScore(String key, String value) {
        try {
            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            
            log.error("Redis ZSet获取分数失败，key: {}, value: {}", key, value, e);
            return null;
        }
    }

    /**
     * 获取有序集合大小
     *
     * @param key 键
     * @return 大小
     */
    public long zSetSize(String key) {
        try {
            Long size = redisTemplate.opsForZSet().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            
            log.error("Redis ZSet获取大小失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 移除有序集合中的元素
     *
     * @param key    键
     * @param values 值集合
     * @return 移除的数量
     */
    public long zSetRemove(String key, Object... values) {
        try {
            Long result = redisTemplate.opsForZSet().remove(key, values);
            return result == null ? 0 : result;
        } catch (Exception e) {
            
            log.error("Redis ZSet移除失败，key: {}", key, e);
            return 0;
        }
    }
}
