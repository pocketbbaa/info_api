package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AccountWithdrawEditRequest;
import com.kg.platform.model.request.admin.AccountWithdrawQueryRequest;
import com.kg.platform.model.response.admin.AccountWithdrawQueryResponse;

public interface AdminAccountWithdrawRMapper {

    List<AccountWithdrawQueryResponse> selectByCondition(AccountWithdrawQueryRequest request);

    long selectCountByCondition(AccountWithdrawQueryRequest request);

    void auditAccountWithdraw(AccountWithdrawEditRequest request);

}
