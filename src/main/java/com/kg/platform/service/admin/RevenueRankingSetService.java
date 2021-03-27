package com.kg.platform.service.admin;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.RevenueRankingSetRequest;

public interface RevenueRankingSetService {

    /**
     * 获取设置
     *
     * @param key
     * @return
     */
    JsonEntity getSet(String key);

    /**
     * 更新设置
     *
     * @param key
     * @param request
     * @return
     */
    JsonEntity updateSet(String key, RevenueRankingSetRequest request, SysUser sysUser);
}
