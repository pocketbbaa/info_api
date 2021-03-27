package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SysUser;

public interface SysUserWMapper {
    int deleteByPrimaryKey(Integer sysUserId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}
