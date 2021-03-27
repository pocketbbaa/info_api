package com.kg.platform.model.response.admin;

import java.util.ArrayList;
import java.util.List;

import com.kg.platform.dao.entity.SysMenu;
import com.kg.platform.model.TokenModel;

public class LoginResponse {

    private List<SysMenu> menuList = new ArrayList<>();

    private TokenModel token;

    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenu> menuList) {
        this.menuList = menuList;
    }

    public TokenModel getToken() {
        return token;
    }

    public void setToken(TokenModel token) {
        this.token = token;
    }
}
