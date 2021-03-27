package com.kg.platform.model.mongoTable;

public class UserLoginLog {

    private Long userId;

    private String userPhone;

    // 来源 1 IOS 2 Android 3 h5 4 pc
    private Integer platfrom;

    private String createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(Integer platfrom) {
        this.platfrom = platfrom;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserLoginLog{" +
                ", userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", platfrom=" + platfrom +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
