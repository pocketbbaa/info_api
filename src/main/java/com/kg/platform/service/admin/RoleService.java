package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.dao.entity.RoleProfile;
import com.kg.platform.model.request.admin.RoleEditRequest;
import com.kg.platform.model.request.admin.RoleQueryRequest;
import com.kg.platform.model.response.admin.RoleQueryResponse;

public interface RoleService {

    List<RoleQueryResponse> getRoleList();

    boolean setStatus(RoleEditRequest request);

    List<RoleProfile> getRoleProfile(RoleQueryRequest request);

    boolean setRoleProfile(RoleEditRequest request);
}
