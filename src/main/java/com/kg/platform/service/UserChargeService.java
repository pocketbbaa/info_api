package com.kg.platform.service;

import com.kg.platform.model.request.UserChargeRequest;

public interface UserChargeService {

    /**
     * 用户充值
     * 
     * @param request
     * @return
     */
    boolean recharge(UserChargeRequest request);

}
