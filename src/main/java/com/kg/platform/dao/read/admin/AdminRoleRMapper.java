package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.response.admin.RoleQueryResponse;

public interface AdminRoleRMapper {

    List<RoleQueryResponse> selectByCondition();

}
