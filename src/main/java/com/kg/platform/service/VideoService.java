package com.kg.platform.service;


import com.kg.platform.common.entity.JsonEntity;

/**
 * Created by Administrator on 2018/6/2.
 */
public interface VideoService {
    /**
     * 首页热门视频
     * @return
     */
    JsonEntity hotVideoList();
}
