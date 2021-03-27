package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SysPost;

public interface SysPostWMapper {
    int deleteByPrimaryKey(Integer postId);

    int insert(SysPost record);

    int insertSelective(SysPost record);

    int updateByPrimaryKeySelective(SysPost record);

    int updateByPrimaryKey(SysPost record);
}
