package com.kg.platform.thridpart.login;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.StringUtils;

/**
 * 准备工作 : https://connect.qq.com/
 */
public class QQLogin {


    /**
     * 获取AccessToken
     *
     * @param appid
     * @param redirectUri
     * @return
     */
    public static String getAccessToken(String appid, String redirectUri) {

        String url = " https://openmobile.qq.com/oauth2.0/m_authorize?response_type=token" +
                "&client_id=" + appid +
                "&redirect_uri=" + redirectUri +
                "&scope=get_user_info";

        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取用户openid
     *
     * @param accessToken
     * @return
     */
    public static String getOpenId(String accessToken) {

        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return "";
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @param accessToken      可通过使用Authorization_Code获取Access_Token 或来获取. access_token有3个月有效期。
     * @param oauthConsumerKey 申请QQ登录成功后，分配给应用的appid
     * @param openid           用户的ID，与QQ号码一一对应 可通过调用https://graph.qq.com/oauth2.0/me?access_token=YOUR_ACCESS_TOKEN 来获取。
     * @return
     */
    public static String getUserInfo(String accessToken, String oauthConsumerKey, String openid) {

        String url = "https://graph.qq.com/user/get_user_info?" +
                "access_token=" + accessToken +
                "&oauth_consumer_key=" + oauthConsumerKey +
                "&openid=" + openid +
                "&format=json";
        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            //获取所需要的信息
            String nickname = object.getString("nickname");  //用户在QQ空间的昵称
            String figureurl = object.getString("figureurl");  //大小为30×30像素的QQ空间头像URL。
            String gender = object.getString("gender");  //性别。 如果获取不到则默认返回"男"

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
