package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.LoginRequest;

public interface SysUserRMapper {

    SysUser selectByPrimaryKey(Long sysUserId);

    List<SysUser> selectByUsernameAndPassword(LoginRequest request);

    List<SysUser> selectByUsername(LoginRequest request);

    List<SysUser> selectAll();

}
