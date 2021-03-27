package com.kg.platform.common.thirdLogin.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JsonUtil;

public class WeixinUtil {

    // 微信appID
    public static String APPID = WxConfig.APPID;

    // 授权回调地址
    public static String REDIRECTURI = WxConfig.REDIRECTURI;

    // 密匙
    public static String SECRET = WxConfig.SECRET;

    /**
     * 第一步：用户同意授权，获取code(引导关注者打开如下页面：) 获取 code、state
     */

    public static String getStartURLToGetCode() {
        String takenUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        takenUrl = takenUrl.replace("APPID", APPID);
        takenUrl = takenUrl.replace("REDIRECT_URI", REDIRECTURI);
        takenUrl = takenUrl.replace("SCOPE", "snsapi_userinfo");
        System.out.println(takenUrl);
        return takenUrl;
    }

    /**
     * 获取access_token、openid 第二步：通过code获取access_token
     * 
     * @param code
     *            url = "https://api.weixin.qq.com/sns/oauth2/access_token
     *            ?appid=APPID &secret=SECRET &code=CODE
     *            &grant_type=authorization_code"
     */
    public static OAuthInfo getAccessToken(String code) {
        String authUrl = WxConfig.access_token_URL;
        authUrl = authUrl.replace("APPID", APPID);
        authUrl = authUrl.replace("SECRET", SECRET);
        authUrl = authUrl.replace("CODE", code);
        String jsonString = HttpUtil.get(authUrl);
        OAuthInfo auth = new OAuthInfo();
        try {
            HashMap<String, Object> map = (HashMap<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            if (map.get("errcode") != null) {
                auth = null;
            } else {
                auth.setAccessToken(String.valueOf(map.get("access_token")));
                auth.setExpiresIn(Long.valueOf(map.get("expires_in") + ""));
                auth.setRefreshToken(String.valueOf(map.get("refresh_token")));
                auth.setOpenId(String.valueOf(map.get("openid")));
                auth.setScope(String.valueOf(map.get("scope")));
                auth.setUnionid(String.valueOf(map.get("unionid")));
                auth.setAccessTime(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }

    /**
     * 获取用户个人信息
     * 
     */
    public static WxUserInfo getWxUser(OAuthInfo oAuthInfo) {
        String authUrl = WxConfig.get_info_URL;
        authUrl = authUrl.replace("ACCESS_TOKEN", oAuthInfo.getAccessToken());
        authUrl = authUrl.replace("OPENID", oAuthInfo.getOpenId());
        String jsonString = HttpUtil.get(authUrl);
        System.out.println("jsonString: " + jsonString);
        WxUserInfo wxUserInfo = new WxUserInfo();
        try {
            Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(jsonString, Map.class);
            wxUserInfo.setNickName(String.valueOf(map.get("nickname")));
            wxUserInfo.setHeadImgurl(String.valueOf(map.get("headimgurl")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxUserInfo;
    }

    public static void main(String[] args) {
        String s = "{\"accessToken\":\"8_xr4rBoHJ_J-9wS5Ry2VMHUMzVkGCwZ5OmOruji2_oc9iEgQSPKPVRsvXjyarO1jNMLOEOhcD1JyyezEGyhingQ\",\"refreshToken\":\"8_3hyWnirunx6IAzdsqONh4jSHJ-p74keefEAtrrd50zR04NgqDTHLECrPfqotlq_X1DftfD_F27ta7mFrQ-aJTw\",\"expiresIn\":\"7200\",\"openId\":\"ohWRv0bfI2mQE5zDUmeTjDUX3n04\",\"scope\":\"snsapi_login\",\"errorCode\":null,\"unionid\":\"o2yYv0dwOFG14U5KBRb9d2CnsYxQ\"}";
        Map<String, Object> map = (Map<String, Object>) JsonUtil.readJson(s, Map.class);
        System.out.println(map.get("errcode") == null);
    }
}