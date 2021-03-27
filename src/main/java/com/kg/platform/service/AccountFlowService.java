package com.kg.platform.service;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountFlowResponse130;

public interface AccountFlowService {

    PageModel<AccountFlowResponse> selectByUserflow(AccountFlowRequest request, PageModel<AccountFlowResponse> page);

    PageModel<AccountFlowResponse130> selectUserTxbflow(AccountFlowRequest request, PageModel<AccountFlowResponse130> page);

    boolean addUserTimes(AccountFlowRequest request);

    Result<AccountFlowResponse> TopupTimes();

    int getTipsout(AccountFlowRequest request);

    /**
     * 通过业务类型查询我当天的打赏次数
     * 
     * @param request
     * @return
     */
    int getRewardTips(AccountFlowRequest request);

    /**
     * 查询分享奖励领取次数
     * 
     * @param request
     * @return
     */
    Integer getShareBonusRecords(AccountFlowRequest request);

    /**
     * 查询分享奖励状态
     * 
     * @param request
     * @return
     */
    Integer getShareBonusStatusCount(AccountFlowRequest request);

}
