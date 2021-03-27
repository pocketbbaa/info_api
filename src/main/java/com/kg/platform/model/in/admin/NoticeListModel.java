package com.kg.platform.model.in.admin;

import com.kg.platform.model.request.admin.AdminBaseRequest;

public class NoticeListModel extends AdminBaseRequest {

    private String addUser;

    private String updateUser;

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

}
