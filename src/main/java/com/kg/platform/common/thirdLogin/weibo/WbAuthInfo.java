package com.kg.platform.common.thirdLogin.weibo;

import java.io.Serializable;

/**
 * 微博授权信息
 * 
 * @author 74190
 *
 */
public class WbAuthInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8602789221932114402L;

    private String access_token;

    private Long expires_in;

    private String remind_in;

    private String uid;

    private String isRealName;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRemind_in() {
        return remind_in;
    }

    public void setRemind_in(String remind_in) {
        this.remind_in = remind_in;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(String isRealName) {
        this.isRealName = isRealName;
    }

}
