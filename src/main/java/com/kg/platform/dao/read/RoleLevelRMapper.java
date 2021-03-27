package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.RoleLevel;

import java.util.List;

public interface RoleLevelRMapper {

    RoleLevel selectByPrimaryKey(Integer roleLevelId);

    List<RoleLevel> selectAllInfo();

}
