package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.RoleLevelAuth;

public interface RoleLevelAuthWMapper {
    int deleteByPrimaryKey(Integer authId);

    int insert(RoleLevelAuth record);

    int insertSelective(RoleLevelAuth record);

    int updateByPrimaryKeySelective(RoleLevelAuth record);

    int updateByPrimaryKey(RoleLevelAuth record);
}
