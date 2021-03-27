package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.UserCertEditRequest;
import com.kg.platform.model.request.admin.UserCertQueryRequest;
import com.kg.platform.model.response.admin.UserCertQueryResponse;

public interface AdminUserCertRMapper {

    List<UserCertQueryResponse> selectByCondition(UserCertQueryRequest request);

    long selectCountByCondition(UserCertQueryRequest request);

    void auditUserCert(UserCertEditRequest request);

}
