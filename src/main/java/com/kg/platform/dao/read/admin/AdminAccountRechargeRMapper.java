package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AccountRechargeQueryRequest;
import com.kg.platform.model.response.admin.AccountRechargeQueryResponse;

public interface AdminAccountRechargeRMapper {

    List<AccountRechargeQueryResponse> selectByCondition(AccountRechargeQueryRequest request);

    long selectCountByCondition(AccountRechargeQueryRequest request);

}
