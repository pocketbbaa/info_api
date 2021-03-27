package com.kg.platform.model.response;

/**
 * 
 * @author 活动助力详情页面邀请用户信息
 *
 */
public class InviteUserResponse {

    private String headImage;

    private Long userId;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}