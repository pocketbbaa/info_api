package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.dao.entity.SysUserUser;
import com.kg.platform.model.request.admin.SysUserUserRequest;
import com.kg.platform.model.response.admin.SysUserUserResponse;

public interface SysUserUserRMapper {

    SysUserUser selectByPrimaryKey(Integer relId);

    List<SysUserUserResponse> selectBySysUser(Integer sysUserId);

    SysUserUser selectByUserAndKgUser(SysUserUserRequest request);

}
