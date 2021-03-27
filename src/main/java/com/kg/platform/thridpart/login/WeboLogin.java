package com.kg.platform.thridpart.login;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * https://open.weibo.com/
 *
 */
public class WeboLogin {


    /**
     * 获取access_token
     *
     * @param appKey      申请应用时分配的AppKey
     * @param appSecret   申请应用时分配的AppSecret
     * @param code        调用authorize获得的code值
     * @param redirectUri 回调地址，需需与注册应用里的回调地址一致
     * @return
     */
    public static String getAccessToken(String appKey, String appSecret, String code, String redirectUri) {

        String url = "https://api.weibo.com/oauth2/access_token";

        Map<String, Object> map = new HashMap<>();
        map.put("client_id", appKey);
        map.put("client_secret", appSecret);
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        map.put("redirect_uri", redirectUri);

        try {
            String response = HttpUtil.post(url, map);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            if (response.contains("errcode")) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            //获取所需要的信息
            String accessToken = object.getString("access_token");  //用户授权的唯一票据
            String expiresIn = object.getString("expires_in");   //access_token的生命周期，单位是秒数

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取微博用户信息
     *
     * @param accessToken
     * @return
     */
    public static String getUserInfo(String accessToken) {
        String url = "https://api.weibo.com/2/users/show.json?access_token=" + accessToken;
        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            if (response.contains("errcode")) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            String idstr = object.getString("idstr");  //用户唯一标识
            String screenName = object.getString("screen_name");
            String name = object.getString("name");
            String profileImageUrl = object.getString("profile_image_url");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
