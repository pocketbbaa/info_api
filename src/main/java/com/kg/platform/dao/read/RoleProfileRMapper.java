package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.RoleProfile;
import com.kg.platform.model.in.RoleProfileInModel;
import com.kg.platform.model.out.RoleProfileOutModel;

public interface RoleProfileRMapper {

    RoleProfile selectByPrimaryKey(Integer roleProfileId);

    List<RoleProfileOutModel> selectByRoleidKey(RoleProfileInModel inModel);

    List<RoleProfile> selectByRoleId(Integer roleId);

    int updateByPrimaryKeySelective(RoleProfile record);

    int updateByPrimaryKey(RoleProfile record);

}