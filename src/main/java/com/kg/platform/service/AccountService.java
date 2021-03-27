package com.kg.platform.service;

import java.math.BigDecimal;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountResponse;

public interface AccountService {
    boolean updateAddUbalance(AccountRequest request);

    boolean updateReductionUbalance(AccountRequest request, long flowid);

    Result<AccountResponse> validationPwd(AccountRequest request);

    Result<AccountResponse> selectByUserbalance(AccountRequest request);

    Result<AccountResponse> selectUserTxbBalance(AccountRequest request);

    /**
     * 获取RIT余额信息
     *
     * @param request
     * @return
     */
    Result<AccountResponse> selectUserRITBalance(AccountRequest request);

    Result<AccountFlowResponse> auditAmount(AccountRequest request);

    // app1.1.0屏蔽阅读奖励
    BigDecimal updateUserbalance(AccountRequest request);

    Result<AccountResponse> selectOutUserId(AccountRequest request);

    Result<AccountFlowResponse> getMaxMinTimes(AccountRequest request);

    boolean loginBonus(Long userId, Integer loginType);

    boolean realnameBonus(Long userId);

    boolean userColumnBonus(Long userId);

    /**
     * 检查钛值是否超过100
     *
     * @param userId
     * @return
     */
    Boolean checkTvAmount(Long userId);

    /**
     * 提现额是否大于平台余额
     *
     * @param request
     * @return
     */
    boolean validPlatformBalance(AccountInModel accountInModel);

    /**
     * 分享文章钛值奖励
     *
     * @param request
     * @return
     */
    boolean shareTvBonus(AccountRequest request);

    /**
     * 分享文章氪金奖励
     *
     * @param request
     * @return
     */
    boolean shareKgBonus(AccountRequest request);

    /**
     * 分享奖励
     *
     * @param request
     * @return
     */
    boolean shareBonus(AccountRequest request);


}
