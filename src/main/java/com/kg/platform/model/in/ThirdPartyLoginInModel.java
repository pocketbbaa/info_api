package com.kg.platform.model.in;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方登录
 * 
 * @author 7419
 *
 */
public class ThirdPartyLoginInModel implements Serializable {

    /**
     * 
     */

    private static final long serialVersionUID = 9029186834138237452L;

    private Long thirdPartyId;

    private String openId;

    private String source;

    private String avatar;

    private String code;

    private Long userId;

    private Integer openType;

    private Date expireTime;

    private String accessToken;

    private String nickName;

    private String refreshAccessToken;

    private Date bindTime;

    public Long getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(Long thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefreshAccessToken() {
        return refreshAccessToken;
    }

    public void setRefreshAccessToken(String refreshAccessToken) {
        this.refreshAccessToken = refreshAccessToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}