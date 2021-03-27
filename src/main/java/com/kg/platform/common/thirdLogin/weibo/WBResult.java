package com.kg.platform.common.thirdLogin.weibo;

/**
 * 微博授权请求接口Bean
 * 
 * @author zouhs
 *
 */
public class WBResult {

    /***************** OAuth2的authorize接口 ********************/

    // 用于第二步调用oauth2/access_token接口，获取授权后的access token。
    private String code;

    // 如果传递参数，会回传该参数。
    private String state;

    /***************** OAuth2的access_token接口 ********************/

    // 用户授权的唯一票据，用于调用微博的开放接口，同时也是第三方应用验证微博用户登录的唯一票据，第三方应用应该用该票据和自己应用内的用户建立唯一影射关系，来识别登录状态，不能使用本返回值里的UID字段来做登录识别。
    private String access_token;

    // access_token的生命周期，单位是秒数。
    private String expires_in;

    // access_token的生命周期（该参数即将废弃，开发者请使用expires_in）。
    private String remind_in;

    // 授权用户的UID，本字段只是为了方便开发者，减少一次user/show接口调用而返回的，第三方应用不能用此字段作为用户登录状态的识别，只有access_token才是用户授权的唯一票据。
    private String uid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
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

}
