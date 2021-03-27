package com.kg.platform.common.thirdLogin.weixin;

public class WxConfig {

    // appID
    public static final String APPID = "wx1fa983464d64de9e";

    // 密匙
    public static final String SECRET = "e0f842d85d00b00830691be56eac8b5b";

    // 授权回调地址
    public static final String REDIRECTURI = "https://t.kg.com/association";

    // 获取授权过的Access Token
    public static final String access_token_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    // 获取微信用户信息
    public static final String get_info_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
}
