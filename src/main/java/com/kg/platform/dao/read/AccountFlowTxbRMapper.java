package com.kg.platform.dao.read;

import java.math.BigDecimal;
import java.util.List;

import com.kg.platform.dao.entity.AccountFlowTxb;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.out.AccountFlowOutModel;

public interface AccountFlowTxbRMapper {

    AccountFlowTxb selectByPrimaryKey(Long accountFlowId);

    List<AccountFlowOutModel> selectUserTypeFlow(AccountFlowInModel inModel);

    BigDecimal countUserTypeFlow(AccountFlowInModel inModel);

    int selectTypeFlowCount(AccountFlowInModel inModel);

    int checkArticleBonus(AccountFlowInModel inModel);

    /**
     * 验证平台氪金余额是否大于提现额度
     *
     * @param record
     * @return
     */
    int validatePlatformKgBanlace(AccountInModel accountInModel);

}
