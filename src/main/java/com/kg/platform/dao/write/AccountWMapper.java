package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Account;
import com.kg.platform.model.in.AccountInModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface AccountWMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(Account record);

    int insertSelective(Account record);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    int updateByUserId(Account record);

    int updateOutUserId(AccountInModel inModel);

    int updateAddBalance(AccountInModel inModel);

    int updateReduction(AccountInModel inModel);

    // 打赏文章作者+金额
    int updateAddUbalance(AccountInModel inModel);

    // 打赏文章用户-金额
    int updateReductionUbalance(AccountInModel inModel);

    // 作者账号冻结金额减浏览金额
    int updateFbalance(AccountInModel inModel);

    // 阅读用户账户余额加浏览金额
    int updateUbalance(AccountInModel inModel);

    int addUserbalance(AccountInModel inModel);

    int addTxbBalance(AccountInModel inModel);

    int reduceTxbBalance(AccountInModel inModel);

    int reduceFBalance(AccountInModel inModel);

    int increaseFbalance(AccountInModel inModel);

    int addRitFrozenBalance(AccountInModel inModel);

    int addRitBalance(AccountInModel inModel);

    int reduceRitBalance(AccountInModel inModel);

    int reduceRitFrozenBalance(AccountInModel inModel);

    int addRitBalanceReduceRitFrozenBalance(AccountInModel inModel);

    int addRitFrozenBalanceReduceRitBalance(AccountInModel inModel);

    /**
     * 氪金兑换RIT
     *
     * @param inModel
     * @return
     */
    int upatekgToRitBalance(AccountInModel inModel);

    /**
     * 更新系统账户
     *
     * @param inModel
     * @return
     */
    int upatekgToRitSysBalance(AccountInModel inModel);

    int reduceBtcBalance(AccountInModel inModel);


    int issueBTCEarnings(@Param("userId") Long userId, @Param("price") BigDecimal price);
}
