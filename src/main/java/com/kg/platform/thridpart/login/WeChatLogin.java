package com.kg.platform.thridpart.login;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.StringUtils;

import java.util.Map;

/**
 * https://open.weixin.qq.com/cgi-bin/index?t=home/index&lang=zh_CN
 * <p>
 * 微信三方登录接口
 * 流程： APP发起微信授权，获取code  ->  服务端请求微信API获取access_token  ->  服务端通过access_token进行接口调用，获取用户基本数据资源
 * <p>
 * 调用频率限制
 * 通过code换取access_token   5万/分钟
 * 获取用户基本信息            5万/分钟
 * 刷新access_token          10万/分钟
 */
public class WeChatLogin {


    /**
     * 通过code获取access_token
     *
     * @param code   获取的code参数
     * @param appId  应用唯一标识，在微信开放平台提交应用审核通过后获得
     * @param secret 应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * @return
     */
    public static Map<String, Object> getAccessToken(String code, String appid, String secret) {

        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?";

        //调用微信后台服务器
        String url = tokenUrl + "appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            if (response.contains("errcode")) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            //获取所需要的信息
            String accessToken = object.getString("access_token");  //接口调用凭证
            String openID = object.getString("openid");  //授权用户唯一标识
            String refreshToken = object.getString("refresh_token");  //用户刷新access_token
            long expiresIn = object.getLong("expires_in");   //access_token接口调用凭证超时时间，单位（秒）

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 刷新或续期access_token使用
     * <p>
     * access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新
     * access_token刷新结果有两种：
     * 1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
     * 2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
     * refresh_token拥有较长的有效期（30天）且无法续期，当refresh_token失效的后，需要用户重新授权后才可以继续获取用户头像昵称。
     *
     * @param appid
     * @param refresh_token
     * @return
     */
    public static String refreshToken(String appid, String refreshToken) {

        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appid + "&grant_type=refresh_token&refresh_token=" + refreshToken;

        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            String accessToken = object.getString("access_token");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param accessToken
     * @param openid
     * @return 0：有效 ，非0：无效
     */
    public static Integer checkAccessToken(String accessToken, String openid) {

        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openid;

        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            return object.getInteger("errcode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 获取用户微信个人信息
     *
     * @param accessToken
     * @param openid
     * @return
     */
    public static String getUserInfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;

        try {
            String response = HttpUtil.get(url);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            if (response.contains("errcode")) {
                return null;
            }
            JSONObject object = JSONObject.parseObject(response);
            String unionid = object.getString("unionid");  //用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
