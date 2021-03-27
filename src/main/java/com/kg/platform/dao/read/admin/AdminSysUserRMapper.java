package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.response.admin.SysUserQueryResponse;
import com.kg.platform.model.response.admin.SysUserUserResponse;

public interface AdminSysUserRMapper {

    List<SysUserQueryResponse> selectByCondition(SysUserQueryRequest request);

    long selectCountByCondition(SysUserQueryRequest request);

    List<SysUserUserResponse> selectSysUsers();

}
