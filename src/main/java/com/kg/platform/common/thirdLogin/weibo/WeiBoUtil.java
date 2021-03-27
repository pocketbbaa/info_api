package com.kg.platform.common.thirdLogin.weibo;

import java.util.Map;

import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JsonUtil;

public class WeiBoUtil {

    public static final String clientId = WBConfig.client_ID;

    public static final String redirectUri = WBConfig.redirectUri;

    public static final String SECRET = WBConfig.SECRET;

    /**
     * 第一步：用户同意授权，获取code
     */
    public static String getStartURLToGetCode() {
        String takenUrl = WBConfig.token_URL;
        takenUrl = takenUrl.replace("clientId", clientId);
        takenUrl = takenUrl.replace("redirectUri", redirectUri);
        String jsonString = HttpUtil.post(takenUrl, "");
        System.out.println("jsonString: " + jsonString);
        return jsonString;
    }

    /**
     * 获取access_token、openid 第二步：通过code获取access_token post请求
     * 
     * @param code
     *            https://api.weibo.com/oauth2/access_token?client_id=clientId&
     *            client_secret
     *            =SECRET&grant_type=authorization_code&redirect_uri
     *            =redirectUri&code=CODE
     */
    public static WbAuthInfo getAccessToken(String code) {
        String authUrl = WBConfig.access_token_URL;
        authUrl = authUrl.replace("clientId", clientId);
        authUrl = authUrl.replace("SECRET", SECRET);
        authUrl = authUrl.replace("CODE", code);
        authUrl = authUrl.replace("redirectUri", redirectUri);
        String jsonString = HttpUtil.post(authUrl, "");
        System.out.println("jsonString: " + jsonString);
        WbAuthInfo wbAuthInfo = new WbAuthInfo();
        try {
            Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            // 重新授权页面
            if (map.get("error_code") != null) {
                // 如果返回值为空 则进入重新授权页面
                wbAuthInfo = null;
            } else {
                wbAuthInfo = (WbAuthInfo) JsonUtil.readJson(jsonString, WbAuthInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wbAuthInfo;
    }

    /**
     * 授权失败 重新刷新accessToken
     */
    public static WbAuthInfo refreshAccessToken(String refreshToken) {
        String authUrl = WBConfig.refresh_access_token_URL;
        authUrl = authUrl.replace("clientId", clientId);
        authUrl = authUrl.replace("SECRET", SECRET);
        authUrl = authUrl.replace("redirectUri", redirectUri);
        authUrl = authUrl.replace("refreshToken", refreshToken);
        String jsonString = HttpUtil.post(authUrl, "");
        WbAuthInfo wbAuthInfo = new WbAuthInfo();
        try {
            Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            wbAuthInfo.setAccess_token(map.get("access_token") + "");
            wbAuthInfo.setRemind_in(map.get("expires_in") + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wbAuthInfo;
    }

    /**
     * 获取用户个人信息
     * 
     * @param code
     *            http请求方式: GET
     *            https://api.weibo.com/2/users/show.json?access_token
     *            =ACCESS_TOKEN&uid=uid
     */
    public static WBUserInfo getWbUserInfo(WbAuthInfo wbAuthInfo) {
        String authUrl = WBConfig.get_info_URL;
        authUrl = authUrl.replace("ACCESS_TOKEN", wbAuthInfo.getAccess_token());
        authUrl = authUrl.replace("UID", wbAuthInfo.getUid());
        String jsonString = HttpUtil.get(authUrl);
        WBUserInfo wbUserInfo = new WBUserInfo();
        try {
            Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            wbUserInfo.setProfile_image_url(String.valueOf(map.get("profile_image_url")));
            wbUserInfo.setScreen_name(String.valueOf(map.get("screen_name")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wbUserInfo;
    }

    /**
     * 解除微博授权信息
     * 
     */
    public static String unbindAccount(WbAuthInfo wbAuthInfo) {
        String authUrl = WBConfig.unbind_access_token_URL;
        authUrl = authUrl.replace("ACCESS_TOKEN", wbAuthInfo.getAccess_token());
        String jsonString = HttpUtil.get(authUrl);
        try {
            Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            return String.valueOf(map.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    public static void main(String[] args) {
        WbAuthInfo wb = WeiBoUtil.getAccessToken("d8b774a23ef36f849e0cebf55d29d4a8");
        System.out.println(JsonUtil.writeValueAsString(wb));

    }

}