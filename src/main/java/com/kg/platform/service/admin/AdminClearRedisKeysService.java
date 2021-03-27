package com.kg.platform.service.admin;

import com.kg.platform.model.request.admin.AdminRedisUtilRequest;

/**
 * Created by Administrator on 2018/8/3.*/


public interface AdminClearRedisKeysService {
    Boolean clearRedisKeys(AdminRedisUtilRequest request);
}
