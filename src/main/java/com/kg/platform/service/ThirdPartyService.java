package com.kg.platform.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.thirdLogin.weibo.WBUserInfo;
import com.kg.platform.common.thirdLogin.weibo.WbAuthInfo;
import com.kg.platform.common.thirdLogin.weixin.OAuthInfo;
import com.kg.platform.common.thirdLogin.weixin.WxUserInfo;
import com.kg.platform.model.in.ThirdPartyLoginInModel;
import com.kg.platform.model.out.ThirdPartyLoginOutModel;
import com.kg.platform.model.request.ThirdPartyLoginRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;

public interface ThirdPartyService {

    /**
     * 获取微信token
     */
    OAuthInfo getAccessToken(String code);

    /**
     * 获取微信用户信息
     */
    WxUserInfo getWxUserInfo(String openId);

    /**
     * 获取微博token
     */
    WbAuthInfo getWbAccessToken(String code);

    /**
     * 获取微博用户信息
     */
    WBUserInfo getWbUserInfo(String openId);

    /**
     * 插入微信第三方绑定数据
     */
    boolean insertThirdPartyInfo(ThirdPartyLoginInModel thirdPartyLoginInModel);

    /**
     * 检查是登录还是需要绑定
     */
    Result<UserkgResponse> checkThirdUserLogin(ThirdPartyLoginInModel thirdPartyLoginInModel, HttpServletRequest req);

    /**
     * 返回登录信息
     */
    Result<UserkgResponse> getLoginInfo(UserkgRequest userkgRequest, HttpServletRequest req);

    /**
     * 用户详情
     * 
     * @param request
     * @param requestse
     * @return
     */
    Result<UserkgResponse> getUserDetails(UserkgRequest request);

    /**
     * 检查用户是否被锁定
     * 
     * @param request
     * @param requestse
     * @return
     */
    void checkLockStatus(UserkgResponse ukr);

    /**
     * 注册并绑定第三方账号
     * 
     * @param request
     * @param requestse
     * @return
     */
    ThirdPartyLoginInModel bindRegistUser(UserkgRequest request, HttpServletRequest requestse);

    /**
     * 手机验证码登录绑定第三方账号
     * 
     * @param request
     * @param requestse
     * @return
     */
    ThirdPartyLoginInModel bindMobileLogin(UserkgRequest request);

    /**
     * 账号密码登录绑定第三方账号
     * 
     * @param request
     * @param requestse
     * @return
     */
    Boolean bindLoginUser(UserkgRequest request, HttpServletRequest requestse);

    /**
     * 微信微博解绑
     * 
     * @param request
     * @param requestse
     * @return
     */
    Boolean unbind(ThirdPartyLoginRequest request);

    /**
     * 判断是否绑定微博或者微信
     * 
     * @param request
     * @return
     */
    List<ThirdPartyLoginOutModel> checkThirdStatus(ThirdPartyLoginRequest request);

    /**
     * 判断微博微信绑状态
     * 
     * @param request
     * @param requestse
     * @return
     */
    ThirdPartyLoginOutModel checkBindStatus(ThirdPartyLoginRequest request);

    /**
     * 通过openId和type判断是否已经绑定
     * 
     * @param request
     * @param requestse
     * @return
     */
    ThirdPartyLoginOutModel checkBindStatusByOpenId(ThirdPartyLoginRequest request);

    /**
     * 从redis里面读取授权信息
     * 
     * @param request
     * @param requestse
     * @return
     */
    ThirdPartyLoginInModel getAuthFromRedis(ThirdPartyLoginInModel thirdPartyLoginInModel, UserkgRequest userkgRequest);

    /**
     * 检查user信息
     * 
     * @param request
     * @param requestse
     * @return
     */
    public boolean checkAddUser(UserkgRequest request);

    /**
     * 验证邮箱
     * 
     * @param request
     * @param requestse
     * @return
     */
    public boolean validateEmail(UserkgRequest request);

    /**
     * 验证手机号码
     * 
     * @param request
     * @param requestse
     * @return
     */
    public boolean validateMobile(UserkgRequest request);

    public boolean checkInviteCode(String inviteCode);

    public String getInviteCode();

    public Result<UserkgResponse> mobileLogin(UserkgRequest request, HttpServletRequest req);

}
