package com.kg.platform.common.utils;

import java.util.*;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.JedisClusterCRC16;

/**
 * jedis tools<br>
 * 增加缓存开关，缓存关闭时忽略任何操作
 * 
 * @author zhaiwenbo
 * @version $Id: JedisUtils.java, v 0.1 2016年11月21日 下午7:29:10 Administrator Exp
 *          $
 */
public final class JedisUtils {
    Logger logger = Logger.getLogger(JedisUtils.class);
    /** 缓存开关 */
    private static boolean IS_OPEN = true;

    @Autowired
    protected ShardedJedisPool shardedJedisPool;

    @Autowired
    protected JedisPool jedisPool;

    @Autowired(required = false)
    protected JedisCluster jedisCluster;

    /**
     * 设置缓存开关
     * 
     * @param isOpen
     */
    public static void setOpen(boolean isOpen) {
        IS_OPEN = isOpen;
    }

    public Jedis getJedis(Integer dataBases){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if(jedis == null){
                logger.error("【redis连接获取为空】");
                return null;
            }
            if(dataBases == null){
                dataBases = 0;
            }
            jedis.select(dataBases);
        }catch (Exception e){
            logger.error("【获取redis连接出现异常 e:】"+e.getMessage());
        }
        return jedis;
    }

    public Set<String> keys(String key) {
        if (!IS_OPEN) {
            return null;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getSingle(String key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.get(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getSet(String key, String value) {
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.getSet(key, value);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.getSet(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public byte[] get(byte[] key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.get(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.hkeys(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.hkeys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(byte[] key, byte[] value, int seconds) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.set(key, value);
                jedisCluster.expire(key, seconds);
                return str;
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key, value);
            jedis.expire(key, seconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @return
     */
    public Long hincrby(String key, String field, long value) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.hincrBy(key, field, value);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.hincrBy(key, field, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.hget(key, field);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long hset(String key, String field, String value) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.hset(key, field, value);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.hset(key, field, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 往集合中添加元素
     * 
     * @param key
     * @param members
     * @return
     */
    public Long sadd(String key, String... members) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.sadd(key, members);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.sadd(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除集合的元素
     * 
     * @param key
     * @return
     */
    public Long srem(String key, String... members) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.srem(key, members);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.srem(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回集合中所有的元素
     * 
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                Set<String> set = jedisCluster.smembers(key);
                if (set == null) {
                    return Collections.emptySet();
                }
                return set;
            }
            jedis = shardedJedisPool.getResource();
            Set<String> set = jedis.smembers(key);
            if (set == null) {
                return Collections.emptySet();
            }
            return set;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @param field
     * @return
     */
    public String hincrby(String key, String field) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.hget(key, field);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incr(String key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.incr(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.incr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incrBy(String key, long integer) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.incrBy(key, integer);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.incrBy(key, integer);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 支持对象存储,json序列化到redis
     * 
     * @param key
     * @param value
     * @return
     */
    public <K, V> String set(K key, V value) {
        if (!IS_OPEN) {
            return null;
        }

        if (key == null || value == null) {
            return null;
        }
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.set(key.toString(), JsonUtil.writeValueAsString(value));
            }
            jedis = shardedJedisPool.getResource();
            return jedis.set(key.toString(), JsonUtil.writeValueAsString(value));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 支持对象存储,json序列化到redis
     * 
     * @param key
     * @param value
     * @return
     */
    public <K, V> String setSingle(K key, V value, int seconds) {
        if (!IS_OPEN) {
            return null;
        }

        if (key == null || value == null) {
            return null;
        }
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key.toString(), JsonUtil.writeValueAsString(value));
            jedis.expire(key.toString(), seconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 支持对象存储,json序列化到redis
     * 
     * @param key
     * @param value
     * @return
     */
    public <K, V> String set(K key, V value, int seconds) {
        if (!IS_OPEN) {
            return null;
        }

        if (key == null || value == null) {
            return null;
        }
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.set(key.toString(), JsonUtil.writeValueAsString(value));
                jedisCluster.expire(key.toString(), seconds);
                return str;
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key.toString(), JsonUtil.writeValueAsString(value));
            jedis.expire(key.toString(), seconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @param javaType
     * @return
     */
    public <K, V> V get(K key, Class<V> javaType) {
        if (!IS_OPEN) {
            return null;
        }

        if (key == null) {
            return null;
        }
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.get(key.toString());
                if (str == null) {
                    return null;
                }
                return JsonUtil.readJson(str, javaType);
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.get(key.toString());
            if (str == null) {
                return null;
            }
            return JsonUtil.readJson(str, javaType);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @param collectionClass
     * @param elementClasses
     * @return
     */
    public <K, V> V get(K key, Class<?> collectionClass, Class<?>... elementClasses) {
        if (!IS_OPEN) {
            return null;
        }

        if (key == null) {
            return null;
        }
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.get(key.toString());
                if (str == null) {
                    return null;
                }
                return JsonUtil.readJson(str, collectionClass, elementClasses);
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.get(key.toString());
            if (str == null) {
                return null;
            }
            return JsonUtil.readJson(str, collectionClass, elementClasses);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.set(key, value);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value, int seconds) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.set(key, value);
                jedisCluster.expire(key, seconds);
                return str;
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key, value);
            jedis.expire(key, seconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value, long mseconds) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                String str = jedisCluster.set(key, value);
                jedisCluster.pexpireAt(key, mseconds);
                return str;
            }
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key, value);
            jedis.pexpireAt(key, mseconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long expire(String key, int seconds) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.expire(key, seconds);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.expire(key, seconds);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(String key) {
        if (!IS_OPEN) {
            return null;
        }

        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.del(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long ttl(String key) {
        ShardedJedis jedis = null;
        try {
            if (null != jedisCluster) {
                return jedisCluster.ttl(key);
            }
            jedis = shardedJedisPool.getResource();
            return jedis.ttl(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public void delKeys(String keysPattern,List<String> exclude) {
        if(null!= jedisCluster){
            Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();

            for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
                Jedis jedis = entry.getValue().getResource();
                try {
                    if (!jedis.info("replication").contains("role:slave")) {
                        Set<String> keys = jedis.keys(keysPattern);

                        if (keys.size() > 0) {
                            Map<Integer, List<String>> map = new HashMap<>(6600);
                            for (String key : keys) {
                                if(!exclude.contains(key)){
                                    int slot = JedisClusterCRC16.getSlot(key);//cluster模式执行多key操作的时候，这些key必须在同一个slot上，不然会报:JedisDataException: CROSSSLOT Keys in request don't hash to the same slot
                                    //按slot将key分组，相同slot的key一起提交
                                    if (map.containsKey(slot)) {
                                        map.get(slot).add(key);
                                    } else {
                                        map.put(slot, Lists.newArrayList(key));
                                    }
                                }
                            }
                            for (Map.Entry<Integer, List<String>> integerListEntry : map.entrySet()) {
                                jedis.del(integerListEntry.getValue().toArray(new String[integerListEntry.getValue().size()]));
                            }
                        }
                    }
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

            }
        }else {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                Set<String> keys = jedis.keys(keysPattern);
                if(keys!=null&&keys.size()>0){
                    String[] skeys = keys.toArray(new String[keys.size()]);
                    List<String> skeys2 = Lists.newArrayList();
                    if(exclude!=null&&exclude.size()>0){
                        for (String skey:skeys) {
                            if(!exclude.contains(skey)){
                                skeys2.add(skey);
                            }
                        }
                        if(skeys2.size()>0){
                            jedis.del((String[]) skeys2.toArray());
                        }
                    }else {
                        jedis.del(skeys);
                    }
                }
            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }
}
