package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AccountDipositQueryRequest;
import com.kg.platform.model.response.admin.AccountDipositQueryResponse;

public interface AdminAccountDipositRMapper {

    List<AccountDipositQueryResponse> selectByCondition(AccountDipositQueryRequest request);

    long selectCountByCondition(AccountDipositQueryRequest request);

}
