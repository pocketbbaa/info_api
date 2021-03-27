package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.RoleProfile;

public interface RoleProfileWMapper {
    int deleteByPrimaryKey(Integer roleProfileId);

    int insert(RoleProfile record);

    int insertSelective(RoleProfile record);

    RoleProfile selectByPrimaryKey(Integer roleProfileId);

    int updateByPrimaryKeySelective(RoleProfile record);

    int updateByPrimaryKey(RoleProfile record);
}