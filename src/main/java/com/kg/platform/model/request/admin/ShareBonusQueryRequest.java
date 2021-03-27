package com.kg.platform.model.request.admin;

/**
 * 奖励管理入参
 */
public class ShareBonusQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -3856391458987099549L;

    private String userId;

    private String nickName;

    private String userPhone;

    private Long userRoleId;

    private String userLevel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

}
