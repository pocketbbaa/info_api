package com.kg.platform.common.thirdLogin.weibo;

public class WBConfig {

    // 微博测试appkey
    public static final String client_ID = "3883397390";

    // f授权回调地址
    public static final String redirectUri = "https://t.kg.com/association";

    // 密匙
    public static final String SECRET = "193338dc7e48eac91a010efef935ce3f";

    // // 正式微博appkey
    // public static final String client_ID = "285478707";
    //
    // // 授权回调地址
    // public static final String redirectUri =
    // "https://www.kg.com/relevanceAccount/list.html";
    //
    // // 密匙
    // public static final String SECRET = "1d0c5b69813e4f9ed87abb154bb14089";

    // 请求用户授权Token
    public static final String token_URL = "https://api.weibo.com/oauth2/authorize?client_id=clientId&redirect_uri=redirectUri&response_type=code";

    // 获取授权过的Access Token
    public static final String access_token_URL = "https://api.weibo.com/oauth2/access_token?client_id=clientId&client_secret=SECRET&grant_type=authorization_code&redirect_uri=redirectUri&code=CODE";

    // 授权信息查询接口
    public static final String get_token_info_URL = "https://api.weibo.com/oauth2/access_token?client_id=clientId&client_secret=SECRET&grant_type=authorization_code&redirect_uri=redirectUri&code=CODE";

    // 获取微博用户信息
    public static final String get_info_URL = "https://api.weibo.com/2/users/show.json?access_token=ACCESS_TOKEN&uid=UID";

    // 获取微博用户信息
    public static final String refresh_access_token_URL = "https://api.weibo.com/oauth2/access_token?client_id=clientId&client_secret=SECRET&grant_type=refresh_token&redirect_uri=redirectUri&refresh_token=refreshToken";

    // 解除微博授权
    public static final String unbind_access_token_URL = "https://api.weibo.com/oauth2/revokeoauth2?access_token=ACCESS_TOKEN";
}
