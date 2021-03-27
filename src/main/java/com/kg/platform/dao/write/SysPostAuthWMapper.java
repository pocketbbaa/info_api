package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SysPostAuth;

public interface SysPostAuthWMapper {
    int deleteByPrimaryKey(Integer postAuthId);

    int insert(SysPostAuth record);

    int insertSelective(SysPostAuth record);

    int updateByPrimaryKeySelective(SysPostAuth record);

    int updateByPrimaryKey(SysPostAuth record);

    int deleteByPostId(Integer postId);
}
