package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountRechargeFlow;

public interface AccountRechargeFlowWMapper {
    int deleteByPrimaryKey(Long rechargeFlowId);

    int insert(AccountRechargeFlow record);

    int insertSelective(AccountRechargeFlow record);

    int updateByPrimaryKeySelective(AccountRechargeFlow record);

    int updateByPrimaryKey(AccountRechargeFlow record);

    int updateByTxid(AccountRechargeFlow record);

}