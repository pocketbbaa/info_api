package com.kg.platform.model.out;

import java.util.List;

/**
 * Created by Administrator on 2018/8/3.
 */
public class AdminRedisUtilOutModel {

    public static final String SETTING_KEY="clear_redis_keys";

    private String userId;

    private List<String> redisKeys;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRedisKeys() {
        return redisKeys;
    }

    public void setRedisKeys(List<String> redisKeys) {
        this.redisKeys = redisKeys;
    }
}
