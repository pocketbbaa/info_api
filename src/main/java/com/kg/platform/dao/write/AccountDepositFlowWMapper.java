package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountDepositFlow;

public interface AccountDepositFlowWMapper {
    int deleteByPrimaryKey(Long depositFlowId);

    int insert(AccountDepositFlow record);

    int insertSelective(AccountDepositFlow record);

    int updateByPrimaryKeySelective(AccountDepositFlow record);

    int updateByPrimaryKey(AccountDepositFlow record);

}