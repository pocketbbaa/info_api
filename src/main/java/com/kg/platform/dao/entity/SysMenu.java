package com.kg.platform.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.kg.platform.tree.annotation.Children;
import com.kg.platform.tree.annotation.ParentId;
import com.kg.platform.tree.annotation.TreeId;

public class SysMenu {
    @TreeId
    private Integer menuId;

    private String menuName;

    private String menuLink;

    private String menuIcon;

    private String menuType;

    @ParentId
    private Integer prevMenu;

    private Integer menuOrder;

    private Integer createUser;

    private Date createDate;

    private Integer updateUser;

    private Date updateDate;

    @Children
    private List<SysMenu> children = new ArrayList<>();

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Integer getPrevMenu() {
        return prevMenu;
    }

    public void setPrevMenu(Integer prevMenu) {
        this.prevMenu = prevMenu;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}