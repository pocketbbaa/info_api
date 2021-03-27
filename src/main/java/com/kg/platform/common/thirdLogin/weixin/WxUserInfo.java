package com.kg.platform.common.thirdLogin.weixin;

/**
 * 微信用户信息
 * 
 * @author 74190
 *
 */
public class WxUserInfo {

    private String openId;

    private String sex;

    private String headImgurl;

    private String scope;

    private String city;

    private String province;

    private String country;

    private String nickName;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImgurl() {
        return headImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        this.headImgurl = headImgurl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
