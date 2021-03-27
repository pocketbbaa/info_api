package com.kg.platform.service;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;

public interface UserkgService {

    public boolean adduser(UserkgRequest request);

    public boolean checkAddUser(UserkgRequest request);

    public Result<Integer> checkuserMobile(UserkgRequest request);

    public Result<Integer> checkuserEmail(UserkgRequest request);

    public boolean checkInviteCode(String inviteCode);

    public String getInviteCode();

    public void updateInviteCode(Long userId, String inviteCode);

    public boolean validateEmail(UserkgRequest request);

    public boolean validateMobile(UserkgRequest request);

    public boolean validatePwd(UserkgRequest request);

    public boolean validateconfirmPwd(UserkgRequest request);

    public Result<UserkgResponse> checkLogin(UserkgRequest request, HttpServletRequest req);

    public Result<UserkgResponse> mobileLogin(UserkgRequest request, HttpServletRequest req);

    void logOut(String userId);

    Result<String> getSendSmsEmailcode(String code, String verIfy);

    Result<String> getSendSmsEmailKey(String key, String verIfy);

    Result<String> checkSmsEmailcode(String code);

    Result<String> checkSmsEmailcodeAndDel(String code);

    public boolean checkRegisterLimit(String userIP);

    public int checkLoginLimit(String userIP, UserkgRequest request);

    public int checkTxpassLimit(String userIP);

    public boolean checkMobile(UserkgRequest request);

    public boolean checkEmail(UserkgRequest request);

    public Result<UserkgResponse> selectUser(UserkgRequest request);

    boolean updatePssword(UserkgRequest request);

    public Result<UserkgResponse> checklong(UserkgRequest request);

    boolean centerUped(UserkgRequest request);

    Result<UserkgResponse> getUserDetails(UserkgRequest request);

    Result<UserkgResponse> selectApply(UserkgRequest request);

    void checkLockStatus(UserkgResponse ukr, Long userId);

    // 获取用户信息
    Result<UserkgResponse> selectByUser(UserkgRequest request);

    // 验证手机是否已经被绑定过第三方账号
    boolean checkThirdPartyPhone(UserkgRequest request);

    // 通过手机号或者openid查询用户信息
    UserkgOutModel getUserPhoneByOpenId(UserkgRequest request);

    boolean checkUserIsLock(UserInModel userInModel);

    // 根据手机号或者邮箱查询用户信息
    UserkgOutModel getUserInfo(UserkgRequest request);

    /**
     * 根据手机号或者邮箱查询用户信息
     */
    UserkgOutModel getUserProfiles(UserkgRequest request);

    /**
     * 邀请新活动
     * @return
     */
    String invite(UserkgRequest request);
}
