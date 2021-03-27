package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.UserColumnIdentity;

import java.util.List;

public interface UserColumnIdentityRMapper {

    /**
     * 获取用户专栏身份
     *
     * @return
     */
    List<UserColumnIdentity> getAll();

}
