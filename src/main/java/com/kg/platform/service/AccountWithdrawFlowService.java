package com.kg.platform.service;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.request.AccountWithdrawFlowRequest;
import com.kg.platform.model.response.AccountWithdrawFlowResponse;

public interface AccountWithdrawFlowService {

    boolean updateByUserSelective(AccountWithdrawFlowRequest request);

    Result<AccountWithdrawFlowResponse> selectByUserFlow(AccountWithdrawFlowRequest request);
}
