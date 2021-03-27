package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Role;

public interface RoleWMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role record);

    int insertSelective(Role record);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
