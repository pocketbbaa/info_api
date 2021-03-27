package com.kg.platform.weixin;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class AccessToken implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1563174305686676551L;

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expires_in")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
