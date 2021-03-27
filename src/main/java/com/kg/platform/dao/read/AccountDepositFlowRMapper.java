package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.AccountDepositFlow;
import com.kg.platform.model.in.AccountDepositFlowIntModel;
import com.kg.platform.model.out.AccountDepositFlowOutModel;

public interface AccountDepositFlowRMapper {

    AccountDepositFlow selectByPrimaryKey(Long depositFlowId);

    AccountDepositFlow selectByTxid(String txId);

    List<AccountDepositFlowOutModel> selectByUserAmount(AccountDepositFlowIntModel intModel);
}