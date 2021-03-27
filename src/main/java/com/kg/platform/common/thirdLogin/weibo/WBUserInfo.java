package com.kg.platform.common.thirdLogin.weibo;

/**
 * 微博用户信息
 * 
 * @author zouhs
 *
 */
public class WBUserInfo {

    // 用户昵称
    private String screen_name;

    // 图片地址
    private String profile_image_url;

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

}
