package com.kg.platform.service;

import com.kg.platform.model.request.UserDepositRequest;

public interface UserDepositService {

    /**
     * 用户充值保证金
     * 
     * @param request
     * @return
     */
    boolean deposit(UserDepositRequest request);

}
