package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.AccountWithdrawFlow;
import com.kg.platform.model.in.AccountWithdrawFlowInModel;
import com.kg.platform.model.out.AccountWithdrawFlowOutModel;

public interface AccountWithdrawFlowRMapper {

    AccountWithdrawFlow selectByPrimaryKey(Long withdrawFlowId);

    AccountWithdrawFlowOutModel getauditAmount(AccountWithdrawFlowInModel inModel);

    AccountWithdrawFlowOutModel selectByUserFlow(AccountWithdrawFlowInModel inModel);

    AccountWithdrawFlowOutModel getUserWithdraw(AccountWithdrawFlowInModel inModel);

}