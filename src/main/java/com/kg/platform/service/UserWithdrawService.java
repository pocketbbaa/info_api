package com.kg.platform.service;

import com.kg.platform.model.in.UserWithdrawInModel;
import com.kg.platform.model.request.UserRelationRequest;

public interface UserWithdrawService {

    /**
     * 用户提现
     * 
     * @param request
     * @return
     */
    boolean withdraw(UserWithdrawInModel inModel);
    
    
    
    /**
     * 用户申请提现
     * 
     * @param request
     * @return
     */
    boolean applyWithdraw(UserRelationRequest request);

}
