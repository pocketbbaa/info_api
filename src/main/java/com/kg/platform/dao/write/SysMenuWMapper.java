package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SysMenu;

public interface SysMenuWMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);
}
