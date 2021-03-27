package com.kg.platform.model.response;


import java.io.Serializable;

public class UserkgAppResponse implements Serializable{

    private String userId;

    private String userEmail;  //邮箱，为空则没有绑定邮箱

    private String userMobile;

    private long realnameAuthed;  //是否实名 1：已实名，其余：未实名

    private int setPwd;  //是否设置密码 1:是，0：否

    private long isResetPwd = 0; //是否设置交易密码 1是 0否

    private int certification; //是否实名认证 1:是，0：否，2:失败,3：认证中

    private String realName; //真实姓名

    private String failInfo; //失败原因

    private String idcard; //身份证号码

    public long getIsResetPwd() {
        return isResetPwd;
    }

    public void setIsResetPwd(long isResetPwd) {
        this.isResetPwd = isResetPwd;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getSetPwd() {
        return setPwd;
    }

    public void setSetPwd(int setPwd) {
        this.setPwd = setPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public long getRealnameAuthed() {
        return realnameAuthed;
    }

    public void setRealnameAuthed(long realnameAuthed) {
        this.realnameAuthed = realnameAuthed;
    }
}