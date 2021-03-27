package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.SysUserUser;

public interface SysUserUserWMapper {
    int deleteByPrimaryKey(Integer relId);

    int insert(SysUserUser record);

    int insertSelective(SysUserUser record);

    int updateByPrimaryKeySelective(SysUserUser record);

    int updateByPrimaryKey(SysUserUser record);
}