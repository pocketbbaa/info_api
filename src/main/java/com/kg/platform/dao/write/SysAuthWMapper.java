package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SysAuth;

public interface SysAuthWMapper {
    int deleteByPrimaryKey(Integer sysAuthId);

    int insert(SysAuth record);

    int insertSelective(SysAuth record);

    int updateByPrimaryKeySelective(SysAuth record);

    int updateByPrimaryKey(SysAuth record);
}
