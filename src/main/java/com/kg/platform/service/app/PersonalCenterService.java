package com.kg.platform.service.app;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;

import javax.servlet.http.HttpServletRequest;

public interface PersonalCenterService {

    /**
     * 查询用户安全状态信息
     *
     * @param request
     * @return
     */
    Result<UserkgAppResponse> getUserDetails(UserkgRequest request);

    /**
     * 获取推送开关状态
     *
     * @param request
     * @return
     */
    AppJsonEntity infoPushState(KgInfoSwitchRequest request, UserkgResponse kguser);

    /**
     * 设置推送开关状态
     *
     * @param request
     * @return
     */
    AppJsonEntity setupPushState(KgInfoSwitchRequest request, UserkgResponse kguser, HttpServletRequest servletRequest);

    /**
     * 修改手机
     *
     * @param request
     * @return
     */
    AppJsonEntity modifyMobile(UserkgRequest request);

    /**
     * 修改登录密码
     *
     * @param request
     * @return
     */
    AppJsonEntity modifyPwd(UserkgRequest request);

    /**
     * 意见反馈
     *
     * @param request
     * @return
     */
    AppJsonEntity addFeedbackx(FeedbackAppRequest request);

    /**
     * 登出
     *
     * @param servletRequest
     * @return
     */
    AppJsonEntity logOut(UserkgResponse kguser, HttpServletRequest servletRequest);

    /**
     * 查询专栏作者信息
     *
     * @param request
     * @return
     */
    UserProfileColumnAppResponse selectByuserprofileId(UserProfileRequest request);

    /**
     * 发送验证邮件
     *
     * @param request
     * @return
     */
    AppJsonEntity sendValidationEmail(UserkgRequest request);

    /**
     * 修改或绑定邮箱
     *
     * @param request
     * @return
     */
    AppJsonEntity modifyEmail(UserkgRequest request);

    /**
     * 校验手机验证码
     *
     * @param request
     * @return
     */
    AppJsonEntity validationMobile(UserkgRequest request);

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public Result<UserkgResponse> checklong(UserkgRequest request);

    /**
     * 设置登录密码
     *
     * @param request
     * @return
     */
    AppJsonEntity setPassword(UserkgRequest request);

    /**
     * 邀新收徒页面信息
     *
     * @param
     * @return
     */
    AppJsonEntity getInviteInfo(UserkgResponse kguser);

    /**
     * 我的师傅信息
     *
     * @return
     */
    AppJsonEntity getTeacherInfo(UserkgResponse kguser);

    /**
     * 我的徒弟列表
     *
     * @return
     */
    AppJsonEntity getContributionList(DiscipleRequest request,
                                      PageModel<DiscipleInfoResponse> page, UserkgResponse kguser);

    /**
     * 通过邀请码绑定我的师傅
     *
     * @return
     */
    AppJsonEntity bindingTeacher(UserRelationRequest request, UserkgResponse kguser);

    /**
     * 邀新奖金提现
     *
     * @return
     */
    AppJsonEntity applyWithdraw(UserRelationRequest request, UserkgResponse kguser);

    /**
     * 徒弟进贡总额
     *
     * @param kguser
     * @return
     */
    AppJsonEntity amountOfTribute(UserkgResponse kguser);

    /**
     * 校验APPtoken
     *
     * @return
     */
    AppJsonEntity checkAppToken();

    /**
     * RIT兑换按钮设置
     *
     * @return
     */
    AppJsonEntity ritExchangeButton();

    /**
     * RIT转出按钮设置
     *
     * @return
     */
    AppJsonEntity ritRolloutButton();

    /**
     * 我的币种信息
     *
     * @param kguser
     * @return
     */
    AppJsonEntity myCoinList(UserkgResponse kguser);

    /**
     * 我的币种详情
     *
     * @param kguser
     * @return
     */
    AppJsonEntity myCoinInfo(UserkgResponse kguser, AccountRequest request);

    AppJsonEntity buttonSet(ButtonSetRequest request);

    /**
     * 获取用户动态信息
     * 1 粉丝关注动态
     * 2 徒弟进贡动态
     * 3 文章、视频评论动态
     * 4 文章、视频点赞动态
     * @param kguser
     * @return
     */
    AppJsonEntity getCarouselListService(UserkgResponse kguser);
}
