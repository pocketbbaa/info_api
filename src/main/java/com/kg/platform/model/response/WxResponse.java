package com.kg.platform.model.response;

import java.io.Serializable;

public class WxResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2584210840695298309L;

    // 头像
    private String avatar;

    // 昵称
    private String nickName;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
