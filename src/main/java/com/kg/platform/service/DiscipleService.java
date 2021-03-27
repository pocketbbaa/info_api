package com.kg.platform.service;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.DiscipleRequest;
import com.kg.platform.model.response.DiscipleInfoResponse;
import com.kg.platform.model.response.DiscipleRemindRespose;
import com.kg.platform.model.response.MasterInfoResponse;

public interface DiscipleService {

    /**
     * 获取我的徒弟进贡信息列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<DiscipleInfoResponse> getDiscipleContribution(DiscipleRequest request,
                                                            PageModel<DiscipleInfoResponse> page);

    /**
     * 获取我的徒弟进贡信息列表【1.3.4】
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<DiscipleInfoResponse> getPupilTribute(DiscipleRequest request,
                                                    PageModel<DiscipleInfoResponse> page);

    /**
     * 查询我的师傅信息
     */
    Result<MasterInfoResponse> getMaterInfo(Long userId);

    /**
     * 打赏我的徒弟
     */
    Boolean rewardDisciple(AccountRequest accountRequest);

    /**
     * 减余额
     */
    Boolean updateReductionUbalance(AccountRequest accountRequest, Long flowid);

    /**
     * 徒弟进贡提醒 师傅打赏提醒
     */
    DiscipleRemindRespose inviteRemind(DiscipleRequest request);

}
