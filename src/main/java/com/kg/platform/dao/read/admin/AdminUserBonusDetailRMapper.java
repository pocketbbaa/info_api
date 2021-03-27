package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.UserBonusQueryRequest;
import com.kg.platform.model.response.admin.UserBonusDetailQueryResponse;

public interface AdminUserBonusDetailRMapper {

    /**
     * 用户奖励详情列表
     * 
     * @param request
     * @return
     */
    List<UserBonusDetailQueryResponse> getUserBonusDetailList(UserBonusQueryRequest request);

    /**
     * 用户奖励详情总条数
     * 
     * @param request
     * @return
     */
    Long getUserBonusDetailCount(UserBonusQueryRequest request);

}
