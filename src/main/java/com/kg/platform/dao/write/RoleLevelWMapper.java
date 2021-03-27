package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.RoleLevel;

public interface RoleLevelWMapper {
    int deleteByPrimaryKey(Integer roleLevelId);

    int insert(RoleLevel record);

    int insertSelective(RoleLevel record);

    int updateByPrimaryKeySelective(RoleLevel record);

    int updateByPrimaryKey(RoleLevel record);
}
