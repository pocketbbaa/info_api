package com.kg.platform.controller.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.TokenException;
import com.kg.platform.common.utils.EmojiUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.service.app.AppAccountService;
import com.kg.platform.service.app.PersonalCenterService;
import org.wltea.analyzer.filter.Filter;

/**
 * 个人中心相关
 * <p>
 * by wangyang 2018/03/21
 */
@Controller
@RequestMapping("/kgApp/personal")
public class PersonalCenterController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppAccountService appAccountService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PersonalCenterService personalCenterService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UsercollectService usercollectService;

    @Autowired
    private LynnService lynnService;

    /**
     * 账户安全状态
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/accountInfo", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity accountInfo(@RequestAttribute UserkgResponse kguser) {
        log.info("[/kgApp/personal/accountInfo] request params : {}", JSONObject.toJSONString(kguser));
        if (null == kguser || null == kguser.getUserId()) {
            log.info("accountInfo -> null == kguser || null == kguser.getUserId()");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        log.info("accountInfo -> kguser.getUserId():{}", kguser.getUserId());
        UserkgRequest request = new UserkgRequest();
        request.setUserId(Long.valueOf(kguser.getUserId()));
        Result<UserkgAppResponse> response = personalCenterService.getUserDetails(request);
        if (response == null || !response.isOk()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        log.info("accountInfo -> response:{}", JSONObject.toJSONString(response));
        return AppJsonEntity.makeSuccessJsonEntity(response.getData());
    }

    /**
     * 获取个人基本信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/myProfile", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity myProfile(@RequestAttribute UserkgResponse kguser, HttpServletRequest servletRequest) {
        if (kguser == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(kguser.getUserId())) {
            log.info("获取个人基本信息 -> 没有传入用户ID ！！！");
            throw new TokenException(ExceptionEnum.TOKENRERROR);
        }
        UserBaseInfoResponse userBaseInfoResponse = appAccountService.getUserBaseInfoById(kguser,servletRequest);
        if (userBaseInfoResponse == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        log.info("获取个人基本信息 -> myProfile userBaseInfoResponse:" + JSONObject.toJSONString(userBaseInfoResponse));
        return AppJsonEntity.makeSuccessJsonEntity(userBaseInfoResponse);
    }

    /**
     * 用户交易明细(账单列表)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("myCoinBill")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountFlowAppRequest.class)
    public AppJsonEntity myCoinBill(@RequestAttribute AccountFlowAppRequest request,
                                    @RequestAttribute(required = false) UserkgResponse kguser, PageModel<AccountFlowAppResponse> page) {

        if (request == null || kguser == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (kguser.getUserId() == null || Long.valueOf(kguser.getUserId()) <= 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));

        initAccount(request);

        int currentPage = request.getCurrentPage();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        page.setCurrentPage(currentPage);
        int type = request.getType();
        CoinEnum coinEnum = CoinEnum.getByCode(type);
        if (coinEnum.equals(CoinEnum.TV)) {
            page = appAccountService.selectUserTzflow(request, page);
            log.info("用户交易明细(账单列表) -> TZ response:" + JSONObject.toJSONString(page));
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        if (coinEnum.equals(CoinEnum.KG)) {
            page = appAccountService.selectUserTxbflow(request, page);
            log.info("用户交易明细(账单列表) -> KG response:" + JSONObject.toJSONString(page));
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        if (coinEnum.equals(CoinEnum.RIT)) {
            page = appAccountService.selectUserRitflow(request, page);
            log.info("用户交易明细(账单列表) -> RIT response:" + JSONObject.toJSONString(page));
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        page.setData(new ArrayList<>());
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }


    /**
     * 用户交易明细(账单列表)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/v1_2_5/myCoinBill")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountFlowAppRequest.class)
    public AppJsonEntity myCoinBillV125(@RequestAttribute AccountFlowAppRequest request,
                                        @RequestAttribute(required = false) UserkgResponse kguser, PageModel<AccountFlowAppNewResponse> page) {

        if (request == null || kguser == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (kguser.getUserId() == null || Long.valueOf(kguser.getUserId()) <= 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));

        initAccount(request);

        int currentPage = request.getCurrentPage();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        page.setCurrentPage(currentPage);
        int type = request.getType();
        CoinEnum coinEnum = CoinEnum.getByCode(type);
        if (coinEnum.equals(CoinEnum.TV)) {
            page = appAccountService.selectUserTzflow125(request, page);
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        if (coinEnum.equals(CoinEnum.KG)) {
            //page = appAccountService.selectUserTxbflow125(request, page);
            page = appAccountService.selectUserTxbflow130(request, page);
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        if (coinEnum.equals(CoinEnum.RIT)) {
            // page = appAccountService.selectUserRitflow125(request, page);
            page = appAccountService.selectUserRitflow130(request, page);
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        page.setData(new ArrayList<>());
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    /**
     * 初始化账户
     *
     * @param request
     */
    private void initAccount(AccountFlowAppRequest request) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(request.getUserId());
        Result<AccountResponse> result = accountService.selectOutUserId(accountRequest);
        if (result.getData() == null) {
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(request.getUserId());
            userAccountService.init(accountInModel);
            log.info("initAccount -> 账户初始化成功 -> userId:" + request.getUserId());
        }
    }

    /**
     * 账单详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("detailBill")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountFlowAppRequest.class)
    public AppJsonEntity detailBill(@RequestAttribute AccountFlowAppRequest request) {
        if (request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        log.info("[detailBill] -> request:" + JSONObject.toJSONString(request));
        AccountFlowResponse130 accountFlowResponse = appAccountService.getDetailBillById(request);
        if (accountFlowResponse == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return AppJsonEntity.makeSuccessJsonEntity(accountFlowResponse);
    }

    /**
     * 校验手机验证码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("validationMobile")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity validationMobile(@RequestAttribute UserkgRequest request) {
        if (null == request.getCode() && "".equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }
        return personalCenterService.validationMobile(request);
    }

    /**
     * 修改手机
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("modifyMobile")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity modifyMobile(@RequestAttribute UserkgRequest request,
                                      @RequestAttribute UserkgResponse kguser) {
        if (null == request.getCode() && "".equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));
        return personalCenterService.modifyMobile(request);
    }

    /**
     * 修改或者绑定邮箱
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("modifyEmail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity modifyEmail(@RequestAttribute UserkgRequest request) {
        log.info("修改或者绑定邮箱 -> request:" + JSONObject.toJSONString(request));
        if (null == request.getCode() && "".equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }
        return personalCenterService.modifyEmail(request);
    }

    /**
     * 校验验证码,发送验证邮件
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("sendValidationEmail")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity sendValidationEmail(@RequestAttribute UserkgRequest request,
                                             @RequestAttribute UserkgResponse kguser) {
        if (null == request.getCode() && "".equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));
        log.info("sendValidationEmail resuest params:{}", JSONObject.toJSONString(request));
        AppJsonEntity appJsonEntity = personalCenterService.sendValidationEmail(request);
        log.info("sendValidationEmail response:{}", JSONObject.toJSONString(appJsonEntity));
        return appJsonEntity;
    }

    /**
     * 获取用户基本信息（个人中心，返回数据分专栏用户和普通用户） 加入用户基本信息修改信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getUserInfo")
    @BaseControllerNote(needCheckParameter = true, needCheckToken = false, beanClazz = UserProfileRequest.class)
    public JSONObject getUserInfo(@RequestAttribute UserProfileRequest request) {
        UserProfileColumnAppResponse response = personalCenterService.selectByuserprofileId(request);
        log.info("获取用户基本信息（个人中心，返回数据分专栏用户和普通用户）-> getUserInfo response:" + JSONObject.toJSONString(response));
        if (null != response) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(response));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity("10003", "用户不存在"));
    }

    /**
     * 用户各币种余额及收入情况
     *
     * @param request 1：TV 2：TXB 3:RIT
     * @return
     */
    @ResponseBody
    @RequestMapping("myCoinInfo")
    @BaseControllerNote(needCheckParameter = true, needCheckToken = true, beanClazz = AccountRequest.class)
    public JSONObject myCoinInfo(@RequestAttribute AccountRequest request, @RequestAttribute UserkgResponse kguser) {

        if (kguser != null) {
            request.setUserId(Long.valueOf(kguser.getUserId()));
        }
        AccountResponse response = null;
        if (request.getUserId() == null || request.getCoinType() == null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }
        Integer coinType = request.getCoinType();
        CoinEnum coinEnum = CoinEnum.getByCode(coinType);
        if (coinEnum.equals(CoinEnum.TV)) {
            response = accountService.selectByUserbalance(request).getData();
        }
        if (coinEnum.equals(CoinEnum.KG)) {
            response = accountService.selectUserTxbBalance(request).getData();
        }
        if (coinEnum.equals(CoinEnum.RIT)) {
            response = accountService.selectUserRITBalance(request).getData();
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(response));
    }


    /**
     * 收藏,点赞列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCollectAll")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public JSONObject getCollectAll(@RequestAttribute UserCollectRequest request, PageModel<UserCollectResponse> page) {
        Integer currentPage = request.getCurrentPage();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        page.setCurrentPage(currentPage);
        page = usercollectService.getCollectAll(request, page);
        if (page != null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage()));
    }

    @ResponseBody
    @RequestMapping("infoPushState")
    @BaseControllerNote(needCheckToken = true, beanClazz = KgInfoSwitchRequest.class)
    public JSONObject infoPushState(@RequestAttribute KgInfoSwitchRequest request,
                                    @RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.infoPushState(request, kguser));
    }

    @ResponseBody
    @RequestMapping("setupPushState")
    @BaseControllerNote(needCheckToken = true, beanClazz = KgInfoSwitchRequest.class)
    public JSONObject setupPushState(@RequestAttribute KgInfoSwitchRequest request,
                                     @RequestAttribute UserkgResponse kguser, HttpServletRequest servletRequest) {
        return AppJsonEntity.jsonFromObject(personalCenterService.setupPushState(request, kguser, servletRequest));
    }

    /**
     * 设置登录密码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("setPassword")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity setPassword(@RequestAttribute UserkgRequest request, @RequestAttribute UserkgResponse kguser) {
        request.setUserId(Long.valueOf(kguser.getUserId()));
        return personalCenterService.setPassword(request);
    }

    /**
     * 个人中心修改密码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("modifyPwd")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = UserkgRequest.class)
    public AppJsonEntity modifyPwd(@RequestAttribute UserkgRequest request, @RequestAttribute UserkgResponse kguser) {
        request.setUserId(Long.valueOf(kguser.getUserId()));
        return personalCenterService.modifyPwd(request);
    }

    /**
     * 取消收藏,取消点赞
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteCollect")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public AppJsonEntity deleteCollect(@RequestAttribute UserCollectRequest request,
                                       @RequestAttribute UserkgResponse kguser) {
        int currentPage = request.getCurrentPage();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        request.setCurrentPage(currentPage);
        request.setUserId(Long.valueOf(kguser.getUserId()));
        usercollectService.deleteByPrimaryKey(request);
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
    }

    /**
     * 修改基本资料
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("modifyUserInfo")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public AppJsonEntity modifyUserInfo(@RequestAttribute UserProfileRequest request,
                                        @RequestAttribute UserkgResponse kguser) {

        if (kguser == null || request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));

        request.setUsername(EmojiUtil.filterUtf8mb4(request.getUsername()));
        request.setResume(EmojiUtil.filterUtf8mb4(request.getResume()));

        try {
            if (StringUtils.isNotEmpty(request.getUsername())) {
                org.wltea.analyzer.result.Result<List<String>> filterResult = Filter.doFilter(request.getUsername().replaceAll(" ", ""));
                if (filterResult.getCode().equals("100")) {
                    return AppJsonEntity.makeExceptionJsonEntity("", "昵称包含敏感词：" + JSONObject.toJSONString(filterResult.getData()));
                }
            }
            if (StringUtils.isNotEmpty(request.getResume())) {
                org.wltea.analyzer.result.Result<List<String>> filterResultResume = Filter.doFilter(request.getResume().replaceAll(" ", ""));
                if (filterResultResume.getCode().equals("100")) {
                    return AppJsonEntity.makeExceptionJsonEntity("", "简介包含敏感词：" + JSONObject.toJSONString(filterResultResume.getData()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean success = userProfileService.updateProfile(request);
        if (success) {
            return AppJsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "修改失败");
    }

    /**
     * 意见反馈
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("commitSuggestion")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = FeedbackAppRequest.class)
    public AppJsonEntity commitSuggestion(@RequestAttribute FeedbackAppRequest request) {
        return personalCenterService.addFeedbackx(request);
    }

    @ResponseBody
    @RequestMapping("logOut")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false, isLogin = true)
    public JSONObject logOut(@RequestAttribute UserkgResponse kguser, HttpServletRequest servletRequest) {
        return AppJsonEntity.jsonFromObject(personalCenterService.logOut(kguser, servletRequest));
    }

    @ResponseBody
    @RequestMapping("inviteInfo")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject inviteInfo(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.getInviteInfo(kguser));
    }

    @ResponseBody
    @RequestMapping("getTeacherInfo")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject getTeacherInfo(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.getTeacherInfo(kguser));
    }

    @ResponseBody
    @RequestMapping("getContributionList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = DiscipleRequest.class)
    public JSONObject getContributionList(@RequestAttribute DiscipleRequest request,
                                          PageModel<DiscipleInfoResponse> page, @RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.getContributionList(request, page, kguser));
    }

    @ResponseBody
    @RequestMapping("bindingTeacher")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserRelationRequest.class)
    public JSONObject bindingTeacher(@RequestAttribute UserRelationRequest request, @RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.bindingTeacher(request, kguser));
    }

    @ResponseBody
    @RequestMapping("applyWithdraw")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserRelationRequest.class)
    public JSONObject applyWithdraw(@RequestAttribute UserRelationRequest request, @RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.applyWithdraw(request, kguser));
    }

    @ResponseBody
    @RequestMapping("amountOfTribute")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject amountOfTribute(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.amountOfTribute(kguser));
    }

    @ResponseBody
    @RequestMapping("checkAppToken")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject checkAppToken() {
        return AppJsonEntity.jsonFromObject(personalCenterService.checkAppToken());
    }


    /**
     * RIT兑换按钮设置
     *
     * @return
     */
    @Cacheable(key = "ritExchangeButton", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("ritExchangeButton")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject ritExchangeButton() {
        return AppJsonEntity.jsonFromObject(personalCenterService.ritExchangeButton());
    }

    /**
     * RIT转出按钮设置
     *
     * @return
     */
    @Cacheable(key = "ritRolloutButton", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("ritRolloutButton")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject ritRolloutButton() {
        return AppJsonEntity.jsonFromObject(personalCenterService.ritRolloutButton());
    }

    /**
     * 按钮设置
     *
     * @return
     */
//    @Cacheable(category = "buttonSet", key = "#{request.type}", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("buttonSet")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = ButtonSetRequest.class)
    public JSONObject buttonSet(@RequestAttribute ButtonSetRequest request) {
        return AppJsonEntity.jsonFromObject(personalCenterService.buttonSet(request));
    }

    /**
     * 用户币种信息列表
     *
     * @return
     */
//    @Cacheable(category = "myCoinList", key = "#{kguser.userId}", expire = 5, dateType = DateEnum.MINUTES)
    @ResponseBody
    @RequestMapping("myCoinList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject myCoinList(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.myCoinList(kguser));
    }

    /**
     * 用户各币种余额及收入情况
     *
     * @param request 1：TV 2：TXB 3:RIT
     * @return
     */
    @ResponseBody
    @RequestMapping("V1_2_5/myCoinInfo")
    @BaseControllerNote(needCheckParameter = true, needCheckToken = true, beanClazz = AccountRequest.class)
    public JSONObject myCoinInfo_v125(@RequestAttribute AccountRequest request, @RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(personalCenterService.myCoinInfo(kguser, request));
    }


    /**
     * 设置交易密码
     */
    @ResponseBody
    @RequestMapping(value = "setTxPassword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public JsonEntity setAppTxPassword(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser) {
        return lynnService.setTxPassword(request, kguser);
    }

    /**
     * 获取授权码
     */
    @ResponseBody
    @RequestMapping(value = "getAuthCode", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public AppJsonEntity getAuthCode(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser) {
        return lynnService.getTxPasswdCode(request, kguser);
    }

    /**
     * 修改交易密码
     */
    @ResponseBody
    @RequestMapping(value = "updateTxPassword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public JsonEntity updateAppTxPassword(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser) {
        return lynnService.updateTxPassword(request, kguser);
    }

    /**
     * 实名认证
     */
    @ResponseBody
    @RequestMapping(value = "userCert", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserCertEditRequest.class)
    public JsonEntity userCert(@RequestAttribute UserCertEditRequest request) {
        return lynnService.userCert(request);
    }

    /**
     * 获取用户动态信息 (关注 点赞 收藏 评论)
     */
    @ResponseBody
    @RequestMapping(value = "getCarouselList", method = RequestMethod.POST)
    @BaseControllerNote(needCheckParameter = false)
    public AppJsonEntity getCarouselList(@RequestAttribute UserkgResponse kguser) {
        return personalCenterService.getCarouselListService(kguser);
    }

}
