package com.kg.platform.model.response;

import java.util.List;

public class JayCherrDetailResponse {

    private String inviteCode;

    private String nickName;

    private String headImage;

    private Integer inviteNum;

    private String imageLink;

    private Integer prizeIndex;

    private Integer isDraw;

    private List<InviteUserResponse> inviteUsers;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(Integer inviteNum) {
        this.inviteNum = inviteNum;
    }

    public List<InviteUserResponse> getInviteUsers() {
        return inviteUsers;
    }

    public void setInviteUsers(List<InviteUserResponse> inviteUsers) {
        this.inviteUsers = inviteUsers;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Integer getPrizeIndex() {
        return prizeIndex;
    }

    public void setPrizeIndex(Integer prizeIndex) {
        this.prizeIndex = prizeIndex;
    }

    public Integer getIsDraw() {
        return isDraw;
    }

    public void setIsDraw(Integer isDraw) {
        this.isDraw = isDraw;
    }

}