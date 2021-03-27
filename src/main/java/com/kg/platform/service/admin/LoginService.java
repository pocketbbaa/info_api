package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.dao.entity.SysMenu;
import com.kg.platform.model.request.admin.UserQueryRequest;
import com.kg.platform.model.response.admin.LoginResponse;

/**
 * 后台登录模块
 */
public interface LoginService {

    /**
     * 登录接口 密码为：md5+salt加密 登录成功后返回token给前端
     * 
     * @param username
     * @param password
     * @return
     */
    LoginResponse login(String username, String password);

    void logOut(String userId);

    /**
     * 获得用户对应权限的菜单
     * 
     * @param request
     * @return
     */
    List<SysMenu> getSysMenu(UserQueryRequest request);
}
