package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.RoleProfileRMapper;
import com.kg.platform.model.in.RoleProfileInModel;
import com.kg.platform.model.out.RoleProfileOutModel;
import com.kg.platform.model.request.RoleProfileRequest;
import com.kg.platform.model.response.RoleProfileResponse;
import com.kg.platform.service.RoleProfileService;

@Service
public class RoleProfileServiceImpl implements RoleProfileService {

    @Inject
    RoleProfileRMapper roleProfileRMapper;

    /**
     * 获取对应角色的填入资料
     */
    @Override
    public Result<List<RoleProfileResponse>> selectByRoleidKey(RoleProfileRequest request) {
        RoleProfileInModel inModel = new RoleProfileInModel();
        inModel.setRoleId(request.getRoleId());
        List<RoleProfileOutModel> list = roleProfileRMapper.selectByRoleidKey(inModel);
        List<RoleProfileResponse> listResponse = new ArrayList<RoleProfileResponse>();
        for (RoleProfileOutModel OutModel : list) {
            RoleProfileResponse response = new RoleProfileResponse();
            response.setDefaultValue(OutModel.getDefaultValue());
            response.setDisplayStatus(OutModel.getDisplayStatus());
            response.setProfileName(OutModel.getProfileName());
            response.setProfileType(OutModel.getProfileType());
            response.setRequiredStatus(OutModel.getRequiredStatus());
            response.setRoleId(OutModel.getRoleId());
            response.setRoleProfileId(OutModel.getRoleProfileId());
            listResponse.add(response);
        }

        return new Result<List<RoleProfileResponse>>(listResponse);
    }

}
