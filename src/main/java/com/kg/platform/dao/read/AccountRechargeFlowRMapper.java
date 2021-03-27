package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.AccountRechargeFlow;

public interface AccountRechargeFlowRMapper {

    AccountRechargeFlow selectByPrimaryKey(Long rechargeFlowId);

    AccountRechargeFlow selectByTxid(String txId);

}