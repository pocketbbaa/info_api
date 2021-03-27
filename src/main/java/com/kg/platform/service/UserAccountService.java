package com.kg.platform.service;

import com.kg.platform.model.in.AccountInModel;

public interface UserAccountService {

    /**
     * 初始化账户
     * 
     * @param request
     * @return
     */
    boolean init(AccountInModel accountIn);

}
