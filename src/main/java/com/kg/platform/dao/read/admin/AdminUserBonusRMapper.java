package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.UserBonusQueryRequest;
import com.kg.platform.model.response.admin.UserBonusQueryResponse;

public interface AdminUserBonusRMapper {

    /**
     * 查询用户奖励列表
     * 
     * @param request
     * @return
     */
    List<UserBonusQueryResponse> getUserBonusList(UserBonusQueryRequest request);

    /**
     * 查询用户奖励列表条数
     * 
     * @param request
     * @return
     */
    Long getUserBonusCount(UserBonusQueryRequest request);

    /**
     * 查看用户奖励详情
     * 
     * @param request
     * @return
     */
    UserBonusQueryResponse getUserBonusDetail(UserBonusQueryRequest request);

}
