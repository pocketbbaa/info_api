package com.kg.platform.controller.app;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.AccountFlowAppNewResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.read.KgArticleStatisticsRMapper;
import com.kg.platform.enumeration.AccountTypeEnum;
import com.kg.platform.model.MailModel;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.out.KgArticleStatisticsOutModel;
import com.kg.platform.model.response.AccountFlowAppResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.EmailService;

public class PersonalCenterControllerTest extends BaseTest {

    protected Logger logger = LoggerFactory.getLogger(PersonalCenterControllerTest.class);

    @Inject
    private PersonalCenterController personalCenterController;

    @Inject
    private UserKgAppController userKgAppController;

    @Inject
    private KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Inject
    private EmailService emailService;

    @Test
    public void accountInfo() {
        UserkgResponse response = new UserkgResponse();
        response.setUserId("400636375558397952");
        AppJsonEntity appJsonEntity = personalCenterController.accountInfo(response);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void myCoinBill() {

        AccountFlowAppRequest accountFlowRequest = new AccountFlowAppRequest();
        accountFlowRequest.setCurrentPage(1);
        accountFlowRequest.setType(1);
        accountFlowRequest.setClassify(1);
        accountFlowRequest.setStartDate("2016-04-07");
        accountFlowRequest.setEndDate("2019-04-08");

        UserkgResponse userToken = new UserkgResponse();
        userToken.setUserId("407889555056898048");

        PageModel<AccountFlowAppResponse> page = new PageModel<>();
        AppJsonEntity appJsonEntity = personalCenterController.myCoinBill(accountFlowRequest, userToken, page);

        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void myCoinBillV125() {
        AccountFlowAppRequest accountFlowRequest = new AccountFlowAppRequest();
        accountFlowRequest.setCurrentPage(1);
        accountFlowRequest.setType(1);
        accountFlowRequest.setClassify(0);


        UserkgResponse userToken = new UserkgResponse();
        userToken.setUserId("429285772550610944");

        PageModel<AccountFlowAppNewResponse> page = new PageModel<>();
        AppJsonEntity appJsonEntity = personalCenterController.myCoinBillV125(accountFlowRequest, userToken, page);

        System.err.println("-------返回结果---------" + JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void detailBill() {
        AccountFlowAppRequest accountFlowAppRequest = new AccountFlowAppRequest();
        accountFlowAppRequest.setAccountFlowId(411199308579348480L);
        accountFlowAppRequest.setType(CoinEnum.KG.getCode());
        AppJsonEntity appJsonEntity = personalCenterController.detailBill(accountFlowAppRequest);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void getColumns() {
        UserProfileRequest request = new UserProfileRequest();
        Long userId1 = 449168532492693504L; // role = 2
        Long userId2 = 406830103805440000L; // role = 1
        request.setUserId(userId1);
        JSONObject json = personalCenterController.getUserInfo(request);
        System.err.println(json.toString());
    }

    @Test
    public void myCoinInfo() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setCoinType(3);
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("4260719339064606721");
        JSONObject result = personalCenterController.myCoinInfo(request, kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void myCoinInfo_v125() {
        AccountRequest request = new AccountRequest();
        request.setCoinType(3);
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("430756406061309952");
        JSONObject result = personalCenterController.myCoinInfo_v125(request, kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getCollectAll() throws Exception {
        UserCollectRequest request = new UserCollectRequest();
        request.setUserId(426071933906460672l);
        request.setCurrentPage(1);
        request.setOperType(1);
        JSONObject result = personalCenterController.getCollectAll(request, new PageModel<>());
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void infoPushState() throws Exception {
        KgInfoSwitchRequest request = new KgInfoSwitchRequest();
        UserkgResponse user = new UserkgResponse();
        user.setUserId("425761755415846912");
        JSONObject result = personalCenterController.infoPushState(request, user);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void setupPushState() throws Exception {
        KgInfoSwitchRequest request = new KgInfoSwitchRequest();
        request.setSystemInfoSwitch(1);
        request.setNewsflashSwitch(0);
        UserkgResponse user = new UserkgResponse();
        user.setUserId("425761755415846912");
        JSONObject result = personalCenterController.setupPushState(request, user, new MockHttpServletRequest());
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void validationMobile() {
        UserkgRequest request = new UserkgRequest();
        request.setUserMobile("13880264646");
        request.setCode("845279");
        AppJsonEntity appJsonEntity = personalCenterController.validationMobile(request);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    /**
     * 发消息
     *
     * @throws Exception
     */
    @Test
    public void sendSmsCode() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setVerIfy("18224449005");
        request.setMobileArea("86");
        request.setValiDation("1");
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        JSONObject result = userKgAppController.sendSmsCode(request, request1);
        System.err.println(result.toJSONString());
    }

    /**
     * 修改手机
     */
    @Test
    public void modifyEmail() {
        UserkgRequest request = new UserkgRequest();
        request.setUserEmail("541230721@qq.com");
        request.setCode("150902");
        request.setCall_method("Onebinding");
        request.setVerIfy("pocketcoder@qq.com");
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("383631064624664576");
        AppJsonEntity appJsonEntity = personalCenterController.modifyMobile(request, kguser);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void sendValidationEmail() {
        UserkgRequest request = new UserkgRequest();
        request.setUserEmail("cqj1280210@163.com");
        request.setCode("550462");
        request.setUserMobile("13880264646");
        request.setUserName("陈秋菊");

        UserkgResponse response = new UserkgResponse();
        response.setUserId("1");
        AppJsonEntity appJsonEntity = personalCenterController.sendValidationEmail(request, response);
        System.out.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void sendEmail() {
        MailModel mail = new MailModel();
        mail.setToEmails("11133115wyd@163.com");
        mail.setContent("<p><h4>wangy 您好:  感谢您使用千氪财经，点击此链接来验证您的e-mail:</h4></p>"
                + "<p><a style=\"color: blue\" href='http://kg.btc123.com/dist/views/emailVerify/list.html?data=eyJjb2RlIjoiODI5ODZiNjEtYjhlOC00ZTczLWJhNTUtYzdjZWRhZDUxNTA0MTUyNDQ0NzUyNDM1NiIsImVtYWlsT2xkIjoicG9ja2V0Y29kZXJAcXEuY29tIiwidXNlckVtYWlsIjoiNTQxMjMwNzIxQHFxLmNvbSIsInVzZXJJZCI6NDM1ODc5MDE4NTgwMDI1MzQ0fQ=='>http://kg.btc123.com/dist/views/emailVerify/list.html?data=eyJjb2RlIjoiODI5ODZiNjEtYjhlOC00ZTczLWJhNTUtYzdjZWRhZDUxNTA0MTUyNDQ0NzUyNDM1NiIsImVtYWlsT2xkIjoicG9ja2V0Y29kZXJAcXEuY29tIiwidXNlckVtYWlsIjoiNTQxMjMwNzIxQHFxLmNvbSIsInVzZXJJZCI6NDM1ODc5MDE4NTgwMDI1MzQ0fQ==</a></p>");
        boolean result = emailService.sendEmail(mail);
        System.err.println("result:" + result);
    }

    @Test
    public void deleteCollect() {
        UserCollectRequest userCollectRequest = new UserCollectRequest();
        userCollectRequest.setUserId(1L);
        userCollectRequest.setArticleId(429278646000295936L);
        // 1：收藏 2：点赞
        userCollectRequest.setOperType(2);

        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("1");
        AppJsonEntity appJsonEntity = personalCenterController.deleteCollect(userCollectRequest, kguser);
        System.out.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void modifyUserInfo() {
        /**
         * inModel.setUserId(request.getUserId());
         * inModel.setAvatar(request.getAvatar());
         * inModel.setSex(request.getSex());
         * inModel.setCountry(request.getCountry());
         * inModel.setProvince(request.getProvince());
         * inModel.setCity(request.getCity());
         * inModel.setCounty(request.getCounty());
         * inModel.setResume(request.getResume());
         */
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setAvatar("asdasdassdasdasd"); // 头像
        userProfileRequest.setSex("女"); // 性别
        userProfileRequest.setResume("asdasdasdasdasdasdasd"); // 个人简介
        userProfileRequest.setUsername("pocketc");
        userProfileRequest.setProvince("四川");
        userProfileRequest.setCity("成都");
        userProfileRequest.setCountry("去后");
        userProfileRequest.setCounty("卡卡角觉");
        userProfileRequest.setUsername("用户名");

        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("384812161026887680");
        AppJsonEntity appJsonEntity = personalCenterController.modifyUserInfo(userProfileRequest, kguser);
        System.out.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void commitSuggestion() {
        FeedbackAppRequest feedbackAppRequest = new FeedbackAppRequest();
        feedbackAppRequest.setCreateUser("1");
        feedbackAppRequest.setFeedbackDetail("这是我的意见。。。。");
        // 反馈意见类型1：功能建议，2：内容建议，3：体验建议
        feedbackAppRequest.setFeedbackType(2);
        feedbackAppRequest.setFeedbackPhone("13880264646");
        feedbackAppRequest.setFromUrl("反馈链接");
        AppJsonEntity appJsonEntity = personalCenterController.commitSuggestion(feedbackAppRequest);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void logOut() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setUserId(425717878151585792l);
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        JSONObject result = personalCenterController.logOut(new UserkgResponse(), request1);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void centerUpdatePd() {
        UserkgRequest request = new UserkgRequest();
        request.setNewPwd("123456wy");
        request.setConfirmPassword("123456wy");
        UserkgResponse response = new UserkgResponse();
        response.setUserId("1");
        AppJsonEntity appJsonEntity = personalCenterController.modifyPwd(request, response);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void setPassword() {

        UserkgRequest request = new UserkgRequest();
        request.setUserMobile("13880264646");
        request.setCode("37748");
        request.setNewPwd("123456wy");
        request.setConfirmPassword("123456wy");
        UserkgResponse response = new UserkgResponse();
        response.setUserId("1");

        AppJsonEntity appJsonEntity = personalCenterController.setPassword(request, response);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void test12() {
        KgArticleStatisticsInModel in = new KgArticleStatisticsInModel();
        in.setArticleId(7L);
        KgArticleStatisticsOutModel out = kgArticleStatisticsRMapper.selectByPrimaryKey(in);
        System.err.println(JSONObject.toJSONString(out));
    }

    @Test
    public void amountOfTribute() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("433657149114163200");
        JSONObject result = personalCenterController.amountOfTribute(kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void inviteInfo() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("439093433349578752");
        JSONObject result = personalCenterController.inviteInfo(kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getTeacherInfo() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("478570663083380736");
        Object result = personalCenterController.getTeacherInfo(kguser);
        logger.info("测试结果：" + JSONObject.toJSONString(result));
    }

    @Test
    public void getContributionList() throws Exception {
        DiscipleRequest request = new DiscipleRequest();
        request.setColumnType(1);
        request.setCurrentPage(1);
        request.setPageSize(25);
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("433657149114163200");
        JSONObject result = personalCenterController.getContributionList(request, new PageModel<>(), kguser);
        System.err.println(result.toJSONString());
    }

    @Test
    public void bindingTeacher() throws Exception {
        UserRelationRequest relationRequest = new UserRelationRequest();
        relationRequest.setInviteCode("DCnY4T");
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("439093433349578752");
        JSONObject result = personalCenterController.bindingTeacher(relationRequest, kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void ritExchangeButton() throws Exception {
        JSONObject jsonObject = personalCenterController.ritExchangeButton();
        System.err.println(JSONObject.toJSONString(jsonObject));
    }

    @Test
    public void ritRolloutButton() throws Exception {
        JSONObject jsonObject = personalCenterController.ritRolloutButton();
        System.err.println(JSONObject.toJSONString(jsonObject));
    }

    @Test
    public void buttonSet() {
        ButtonSetRequest request = new ButtonSetRequest();
        request.setType(2);
        JSONObject jsonObject = personalCenterController.buttonSet(request);
        System.err.println(JSONObject.toJSONString(jsonObject));
    }

    @Test
    public void myCoinList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("439093433349578752");
        JSONObject jsonObject = personalCenterController.myCoinList(kguser);
        System.err.println(JSONObject.toJSONString(jsonObject));
    }

    @Test
    public void testGetCarouselList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("434346240138944512");
        System.err.println(JSON.toJSONString(personalCenterController.getCarouselList(kguser)));
    }
}
