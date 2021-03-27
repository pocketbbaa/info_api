package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.request.RoleProfileRequest;
import com.kg.platform.model.response.RoleProfileResponse;

public interface RoleProfileService {

    Result<List<RoleProfileResponse>> selectByRoleidKey(RoleProfileRequest request);

}
