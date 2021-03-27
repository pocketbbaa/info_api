package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.Account;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.out.AccountOutModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface AccountRMapper {

    Account selectByPrimaryKey(Long accountId);

    Account selectByUserId(Long userId);

    AccountOutModel selectOutUserId(AccountInModel inModel);

    AccountOutModel validationPwd(AccountInModel inModel);

    /**
     * 获取账户信息
     *
     * @param inModel
     * @return
     */
    AccountOutModel selectByUserbalance(AccountInModel inModel);

    /**
     * 验证平台余额是否大于提现额度
     *
     * @param record
     * @return
     */
    int validatePlatformBanlace(AccountInModel inModel);

    /**
     * 验证平台rit余额是否大于提现额度
     *
     * @param record
     * @return
     */
    int validatePlatformRitBanlace(AccountInModel inModel);


    BigDecimal getBtcBalance(@Param("userId") Long userId);
}
