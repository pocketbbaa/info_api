package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AccountQueryRequest;
import com.kg.platform.model.response.admin.AccountQueryResponse;
import com.kg.platform.model.response.admin.TotalBonusQueryResponse;

public interface AdminAccountRMapper {

    List<AccountQueryResponse> selectByCondition(AccountQueryRequest request);

    long selectCountByCondition(AccountQueryRequest request);

    Double selectSumByCondition(AccountQueryRequest request);

    List<AccountQueryResponse> selectTxbFlow(AccountQueryRequest request);

    long selectTxbCount(AccountQueryRequest request);

    /**
     * 统计用户钛值和氪金奖励总额
     *
     * @param request
     * @return
     */
    TotalBonusQueryResponse sumUserBonus(AccountQueryRequest request);


    /**
     * rit流水
     * @param request
     * @return
     */
    List<AccountQueryResponse> selectRitFlow(AccountQueryRequest request);


    long selectRitCount(AccountQueryRequest request);


}
