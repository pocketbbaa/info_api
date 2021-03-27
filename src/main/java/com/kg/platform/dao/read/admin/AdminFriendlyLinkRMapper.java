package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AdminBaseRequest;
import com.kg.platform.model.response.admin.FriendlyLinkQueryResponse;

public interface AdminFriendlyLinkRMapper {

    List<FriendlyLinkQueryResponse> selectByCondition(AdminBaseRequest request);

    long selectCountByCondition();

}
