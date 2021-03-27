package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.SysPost;

public interface SysPostRMapper {

    SysPost selectByPrimaryKey(Integer postId);

    List<SysPost> selectAll();

}
