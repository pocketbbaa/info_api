package com.kg.platform.common.lock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kg.platform.common.utils.PropertyLoader;

@Component
public class RedissonUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    private static RedissonClient redissonClient;

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

    private RedissonUtil() {
    }

    public static RedissonUtil getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static final RedissonUtil instance = new RedissonUtil();
    }

    @PostConstruct
    private void init() {
        logger.info("init redissonClient..........{}", redissonClient);

        Config config = new Config();
        String hostName = propertyLoader.getProperty("redis", "redis.host");
        String password = propertyLoader.getProperty("redis", "redis.password");
        String port = propertyLoader.getProperty("redis", "redis.port");
        config.useSingleServer().setAddress("redis://" + hostName + ":" + port);
        if (StringUtils.isNotBlank(password)) {
            config.useSingleServer().setPassword(password);
        }
        redissonClient = Redisson.create(config);

        logger.info("init redissonClient finish ..........{}", redissonClient);

    }
}
