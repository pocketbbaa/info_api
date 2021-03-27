package com.kg.platform.model.request.admin;

public class BaseinfoStatusRequest {

    private Integer id;

    private Integer updateUser;

    private Integer infoOrder;

    public Integer getInfoOrder() {
        return infoOrder;
    }

    public void setInfoOrder(Integer infoOrder) {
        this.infoOrder = infoOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }
}
