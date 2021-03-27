package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountWithdrawFlow;
import com.kg.platform.model.in.AccountWithdrawFlowInModel;

public interface AccountWithdrawFlowWMapper {
    int deleteByPrimaryKey(Long withdrawFlowId);

    int insert(AccountWithdrawFlow record);

    int insertSelective(AccountWithdrawFlow record);

    int updateByPrimaryKeySelective(AccountWithdrawFlow record);

    int updateByPrimaryKey(AccountWithdrawFlow record);

    int insertFlowSelective(AccountWithdrawFlowInModel inModel);

    int updateByUserSelective(AccountWithdrawFlowInModel inModel);
}