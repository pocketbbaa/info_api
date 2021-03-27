package com.kg.platform.model;

public class UserTagBuild {

    private int role;
    private String identityTag; //身份标识
    private int realAuthedTag; //实名标签 0:无，1：有
    private int vipTag; //是否有大V标签 0：无，1：有

    private UserTagBuild(String identityTag, int realAuthedTag, int vipTag) {
        this.identityTag = identityTag;
        this.realAuthedTag = realAuthedTag;
        this.vipTag = vipTag;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public static UserTagBuild initUserTage() {
        return new UserTagBuild("", 0, 0);
    }
}