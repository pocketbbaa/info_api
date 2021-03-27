package com.kg.platform.model.request.admin;

import java.util.List;

public class UserBonusListRequest {


    private Integer adminId;

    ////0是RIT（余额），1是RIT（冻结），2是KG
    private Integer awardType;

    private List<UserBonus> list;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getAwardType() {
        return awardType;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public List<UserBonus> getList() {
        return list;
    }

    public void setList(List<UserBonus> list) {
        this.list = list;
    }
}
