package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.Role;

import java.util.List;

public interface RoleRMapper {

    Role selectByPrimaryKey(Integer roleId);

    List<Role> selectAllInfo();

}
