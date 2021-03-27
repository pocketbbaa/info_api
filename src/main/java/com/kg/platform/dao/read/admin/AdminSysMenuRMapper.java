package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.dao.entity.SysMenu;

public interface AdminSysMenuRMapper {

    List<SysMenu> selectByUserId(Long userId);

    List<SysMenu> selectAll();

}
