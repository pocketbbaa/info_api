package com.kg.platform.model.response;

import java.io.Serializable;

public class UserProfileAppResponse implements Serializable {

    private static final long serialVersionUID = 8937025886437992127L;

    private String userName; // 作者名

    private String userId; // 作者ID

    private String avatar; // 作者头像

    private Integer concernedStatus; // 作者关注当前用户状态 0未关注 1已关注

    private Integer concernUserStatus; // 当前登录用户关注作者状态 0未关注 1已关注

    /** V1.0.1新增字段 **/
    private String identityTag; // 身份标识

    private int realAuthedTag; // 实名标签 0:无，1：有

    private int vipTag; // 是否有大V标签 0：无，1：有

    private String rewardIncome; // 奖励收入

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public String getRewardIncome() {
        return rewardIncome;
    }

    public void setRewardIncome(String rewardIncome) {
        this.rewardIncome = rewardIncome;
    }

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(Integer concernedStatus) {
        this.concernedStatus = concernedStatus;
    }

    public Integer getConcernUserStatus() {
        return concernUserStatus;
    }

    public void setConcernUserStatus(Integer concernUserStatus) {
        this.concernUserStatus = concernUserStatus;
    }

    public static UserProfileAppResponse initModel(UserProfileColumnAppResponse userProfileColumnResponse) {
        if (userProfileColumnResponse == null) {
            return new UserProfileAppResponse();
        }
        UserProfileAppResponse userProfileAppResponse = new UserProfileAppResponse();
        userProfileAppResponse.setUserId(userProfileColumnResponse.getCreateUser());
        userProfileAppResponse.setUserName(userProfileColumnResponse.getColumnName());
        userProfileAppResponse.setAvatar(userProfileColumnResponse.getColumnAvatar());

        userProfileAppResponse.setIdentityTag(userProfileColumnResponse.getIdentityTag());
        userProfileAppResponse.setRealAuthedTag(userProfileColumnResponse.getRealAuthedTag());
        userProfileAppResponse.setVipTag(userProfileColumnResponse.getVipTag());
        userProfileAppResponse.setRewardIncome(userProfileColumnResponse.getRewardIncome());
        return userProfileAppResponse;
    }
}