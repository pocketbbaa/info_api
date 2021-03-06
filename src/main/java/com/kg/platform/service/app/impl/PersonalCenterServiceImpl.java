package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoDao;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.KgUserCertificated;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.write.FeedbackWMapper;
import com.kg.platform.dao.write.KgInfoSwitchWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.CoinBgModel;
import com.kg.platform.model.EmailLinkConfig;
import com.kg.platform.model.MailModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.*;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.model.response.admin.UserQueryResponse;
import com.kg.platform.service.*;
import com.kg.platform.service.admin.UserService;
import com.kg.platform.service.app.PersonalCenterService;
import com.kg.platform.service.app.UserConcernService;
import com.kg.platform.service.app.UserKgAppService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.util.TextUtils;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Integer APP_FROM_TYPE = 2;

    private static final Integer USER_ROLE_AUTHOR = 2; // ??????

    private static final Integer USER_ROLE_NORMAL = 1; // ????????????

    private static final Integer ERROR_RESULT_CODE = 10005; // ????????????

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserCertRMapper userCertRMapper;

    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    private KgInfoSwitchWMapper kgInfoSwitchWMapper;

    @Autowired
    private UserkgService userkgService;

    @Inject
    private FeedbackWMapper feedbackWMapper;

    @Inject
    private IDGen idGenerater;

    @Inject
    private TokenManager tokenManager;

    @Inject
    private ArticleRMapper articleRMapper;

    @Inject
    private UserProfileRMapper userProfileRMapper;

    @Inject
    private KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Inject
    private EmailService emailService;

    @Autowired
    private EmailLinkConfig config;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private UserKgAppService userKgAppService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private DiscipleService discipleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private UserTagsUtil userTagsUtil;

    @Autowired
    private UserConcernRMapper userConcernRMapper;

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private AccountRMapper accountRMapper;

    @Autowired
    private CoinRMapper coinMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private UserRelationRMapper userRelationRMapper;

    @Autowired
    private KgUserCertificatedRMapper kgUserCertificatedRMapper;

    @Autowired
    private UserConcernService userConcernService;

    @Autowired
    private PushService pushService;


    @Override
    public Result<UserkgAppResponse> getUserDetails(UserkgRequest request) {
        UserInModel inModel = new UserInModel();
        inModel.setUserId(request.getUserId());
        UserkgOutModel model = userRMapper.getUserDetails(inModel.getUserId());
        UserCertInModel userCertInModel = new UserCertInModel();
        userCertInModel.setUserId(String.valueOf(request.getUserId()));
        UserCertOutModel userCertOutModel = userCertRMapper.selectByUser(userCertInModel);

        UserkgAppResponse response = new UserkgAppResponse();
        if (model != null) {
            response.setUserId(model.getUserId());
            String email = model.getUserEmail();
            if (StringUtils.isNotEmpty(email)) {
                response.setUserEmail(email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
            }
            response.setUserEmail(model.getUserEmail());
            response.setUserMobile(model.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            if (userCertOutModel == null) {
                response.setRealnameAuthed(-1);// ?????????
            } else {
                response.setRealnameAuthed(userCertOutModel.getStatus());
            }
            String password = model.getUserPassword();
            if (StringUtils.isEmpty(password)) {
                response.setSetPwd(0); // ??????????????????
            } else {
                if (password.length() < 25) { // ???????????????25??????????????????????????????????????????????????????
                    response.setSetPwd(0);
                } else {
                    response.setSetPwd(1);
                }
            }
        }
        //1.3.0??????????????????
        appendNewVersionParams(response, request.getUserId());
        return new Result<>(response);
    }

    private void appendNewVersionParams(UserkgAppResponse response, Long userId) {
        //??????????????????
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        response.setIsResetPwd(userkgOutModel.getTradepasswordSet());
        //????????????????????????
        KgUserCertificated kgUserCertificated = kgUserCertificatedRMapper.selectByPrimaryKey(userId);
        if (kgUserCertificated == null) {
            response.setCertification(3);
            return;
        } else {
            response.setCertification(kgUserCertificated.getCertificateStatus());
        }
        String realName = kgUserCertificated.getRealName();
        if (StringUtils.isNotEmpty(realName)) {
            response.setRealName(kgUserCertificated.getRealName().substring(0, 1) + StringUtils.getChar(realName.length()));
        }
        String idCard = kgUserCertificated.getIdcardNo();
        if (StringUtils.isNotEmpty(idCard)) {
            response.setIdcard(idCard.replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****************$2"));
        }
        response.setFailInfo(kgUserCertificated.getRefuseReason());
    }

    public static String idMask(String idCardNum, int front, int end) {
        //?????????????????????
        if (TextUtils.isEmpty(idCardNum)) {
            return null;
        }
        //???????????????????????????????????????????????????
        if ((front + end) > idCardNum.length()) {
            return null;
        }
        //???????????????????????????0
        if (front < 0 || end < 0) {
            return null;
        }
        //??????*?????????
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

    @Override
    public AppJsonEntity modifyMobile(UserkgRequest request) {

        if (request.getUserId() == null || request.getUserId() <= 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(request.getUserId());

        // ????????????????????????
        // Result<String> checkcodeOld =
        // userkgService.checkSmsEmailcode(userkgOutModel.getUserMobile());
        // if (checkcodeOld.getData() == null) {
        // return AppJsonEntity.makeExceptionJsonEntity("","???????????????");
        // }
        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }
        if (request.getVerIfy() != null) {
            boolean isPhone = userkgService.checkMobile(request);
            if (!isPhone) {
                return AppJsonEntity.makeExceptionJsonEntity("", "????????????????????????");
            }
            request.setUserMobile(request.getVerIfy());
        }
        boolean success = false;
        try {
            success = userkgService.centerUped(request);
        } catch (Exception e) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PHONE_EXIST);
        }
        if (!success) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public AppJsonEntity modifyPwd(UserkgRequest request) {

        Result<UserkgResponse> response = checklong(request);
        Result<String> checkcode = userkgService.checkSmsEmailcode(response.getData().getUserMobile());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }

        if (!userkgService.validateconfirmPwd(request)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????????????????");
        }
        /*
         * if (null == response.getData()) { return
         * AppJsonEntity.makeExceptionJsonEntity("", "??????????????????"); }
         */
        request.setUserId(request.getUserId());
        boolean success = userkgService.updatePssword(request);
        if (success) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
    }

    @Override
    public AppJsonEntity addFeedbackx(FeedbackAppRequest request) {

        String des = request.getFeedbackDetail();
        if (StringUtils.isEmpty(des)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "?????????????????????");
        }
        if (des.length() > 500) {
            return AppJsonEntity.makeExceptionJsonEntity("", "????????????????????????500?????????");
        }
        String phone = request.getFeedbackPhone();
        if (StringUtils.isEmpty(phone)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????????????????");
        }
        UserkgRequest requestKg = new UserkgRequest();
        requestKg.setVerIfy(phone);
        boolean isPhone = userkgService.checkMobile(requestKg);
        if (!isPhone) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????????????????");
        }
        FeedbackInModel inModel = new FeedbackInModel();
        inModel.setFeedbackId(idGenerater.nextId());
        inModel.setFeedbackType(request.getFeedbackType());
        inModel.setFeedbackDetail(EmojiUtil.filterUtf8mb4(request.getFeedbackDetail()));
        inModel.setFeedbackPhone(request.getFeedbackPhone());
        inModel.setFromUrl(request.getFromUrl());
        inModel.setCreateDate(new Date());
        if (StringUtils.isNotBlank(request.getCreateUser())) {
            inModel.setCreateUser(Long.parseLong(request.getCreateUser()));
        }
        inModel.setFeedbackStatus(false);
        inModel.setFromType(APP_FROM_TYPE);
        boolean success = feedbackWMapper.insertFrontForApp(inModel) > 0;
        if (success) {
            return AppJsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "????????????");
    }

    @Override
    public AppJsonEntity infoPushState(KgInfoSwitchRequest request, UserkgResponse kguser) {
        request.setUserId(Long.parseLong(kguser.getUserId()));
        AppJsonEntity result = ifHaveInfo(request);
        if (ExceptionEnum.SUCCESS.getCode().equals(result.getCode())) {
            // ??????????????????
            return result;
        } else if ("200".equals(result.getCode())) {
            KgInfoSwitchOutModel outModel = (KgInfoSwitchOutModel) result.getData();
            // ??????????????????????????????
            KgInfoSwitchResponse response = new KgInfoSwitchResponse();
            response.setUserId(outModel.getUserId().toString());
            response.setSystemInfoSwitch(outModel.getSystemInfoSwitch());
            response.setDynamicMessageSwitch(outModel.getDynamicMessageSwitch());
            response.setBonusSwitch(outModel.getBonusSwitch());
            response.setHotNewsSwitch(outModel.getHotNewsSwitch());
            response.setNewsflashSwitch(outModel.getNewsflashSwitch());
            return AppJsonEntity.makeSuccessJsonEntity(response);
        } else {
            return result;
        }

    }

    @Override
    public AppJsonEntity setupPushState(KgInfoSwitchRequest request, UserkgResponse kguser,
                                        HttpServletRequest servletRequest) {
        request.setUserId(Long.valueOf(kguser.getUserId()));
        AppJsonEntity result = ifHaveInfo(request);
        if (ExceptionEnum.SUCCESS.getCode().equals(result.getCode())) {
            // ??????????????????
            return AppJsonEntity.makeSuccessJsonEntity(null);
        } else if ("200".equals(result.getCode())) {
            KgInfoSwitchInModel inModel = new KgInfoSwitchInModel();
            inModel.setUserId(Long.valueOf(kguser.getUserId()));
            inModel.setSystemInfoSwitch(request.getSystemInfoSwitch());
            inModel.setDynamicMessageSwitch(request.getDynamicMessageSwitch());
            inModel.setHotNewsSwitch(request.getHotNewsSwitch());
            inModel.setNewsflashSwitch(request.getNewsflashSwitch());
            inModel.setBonusSwitch(request.getBonusSwitch());
            kgInfoSwitchWMapper.updateByPrimaryKeySelective(inModel);
            if (request.getHotNewsSwitch() != null) {
                if (request.getHotNewsSwitch().intValue() == 0) {
                    // ?????????????????? ????????????tag??????
                    userKgAppService.mqPush(servletRequest, null, PushTypeEnum.UNBINDING_TAG.getCode(), "newsTagIfBind");
                } else if (request.getHotNewsSwitch().intValue() == 1) {
                    // ??????
                    userKgAppService.mqPush(servletRequest, null, PushTypeEnum.BINDING_TAG.getCode(), "newsTagIfBind");
                }
            }
            return AppJsonEntity.makeSuccessJsonEntity(null);
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    public AppJsonEntity ifHaveInfo(KgInfoSwitchRequest request) {
        // ???????????????KG??????
        /*
         * UserkgOutModel user =
         * userRMapper.selectByPrimaryKey(request.getUserId()); if (user ==
         * null) { return
         * AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode
         * (), ExceptionEnum.DATAERROR.getMessage()); }
         */
        // ????????????????????????
        KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper.selectByPrimaryKey(request.getUserId());
        if (outModel == null) {
            // ?????????????????????????????????
            KgInfoSwitchInModel inModel = new KgInfoSwitchInModel();
            inModel.setUserId(request.getUserId());
            inModel.setSystemInfoSwitch(request.getSystemInfoSwitch());
            inModel.setDynamicMessageSwitch(request.getDynamicMessageSwitch());
            inModel.setHotNewsSwitch(request.getHotNewsSwitch());
            inModel.setBonusSwitch(request.getBonusSwitch());
            inModel.setNewsflashSwitch(request.getNewsflashSwitch());
            int state = kgInfoSwitchWMapper.insertSelective(inModel);
            if (state > 0) {
                KgInfoSwitchResponse response = new KgInfoSwitchResponse();
                response.setUserId(inModel.getUserId().toString());
                if (request.getSystemInfoSwitch() == null) {
                    response.setSystemInfoSwitch(1);
                } else {
                    response.setSystemInfoSwitch(inModel.getSystemInfoSwitch());
                }
                if (request.getDynamicMessageSwitch() == null) {
                    response.setDynamicMessageSwitch(1);
                } else {
                    response.setDynamicMessageSwitch(inModel.getDynamicMessageSwitch());
                }
                if (request.getBonusSwitch() == null) {
                    response.setBonusSwitch(1);
                } else {
                    response.setBonusSwitch(inModel.getBonusSwitch());
                }
                if (request.getHotNewsSwitch() == null) {
                    response.setHotNewsSwitch(1);
                } else {
                    response.setHotNewsSwitch(inModel.getHotNewsSwitch());
                }
                if (request.getNewsflashSwitch() == null) {
                    response.setNewsflashSwitch(1);
                } else {
                    response.setNewsflashSwitch(inModel.getNewsflashSwitch());
                }
                return AppJsonEntity.makeSuccessJsonEntity(response);
            } else {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return new AppJsonEntity("200", "??????", outModel);
        }
    }

    @Override
    public AppJsonEntity logOut(UserkgResponse kguser, HttpServletRequest servletRequest) {
        String userId = kguser.getUserId();
        log.info("???logOut??? -> kguser???" + JSONObject.toJSONString(kguser));
        if (StringUtils.isEmpty(userId)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????????????????");
        }
        tokenManager.deleteAppToken(Long.parseLong(kguser.getUserId()));
        // ??????MQ??????


        pushService.logOut(kguser.getUserId(), servletRequest.getHeader("device_id"), servletRequest.getIntHeader("os_version"), "unBindMobile");

        return AppJsonEntity.makeSuccessJsonEntity(null);
    }

    @Override
    public UserProfileColumnAppResponse selectByuserprofileId(UserProfileRequest request) {
        ArticleOutModel article = articleRMapper.selectByPrimaryKey(request.getArticleId());
        UserProfileInModel inModel = new UserProfileInModel();
        Long userId = (null == article ? request.getUserId() : article.getCreateUser());
        inModel.setUserId(userId);
        UserProfileOutModel comments = userProfileRMapper.countComments(inModel);
        UserProfileOutModel outcount = userProfileRMapper.bowsenumCount(inModel);
        ArticleInModel articleInModel = new ArticleInModel();
        articleInModel.setCreateUser(userId);
        articleInModel.setPublishStatus("1");
        long countArt = articleRMapper.selectCountArticle(articleInModel);
        KgArticleStatisticsInModel kaSinModel = new KgArticleStatisticsInModel();
        kaSinModel.setCreateUser(userId);
        UserProfileOutModel profileOutModel = userProfileRMapper.getMedia(inModel);
        if (profileOutModel == null) {
            return null;
        }
        log.info("UserProfileOutModel -> profileOutModel:" + JSONObject.toJSONString(profileOutModel));
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        if (userkgOutModel == null) {
            return null;
        }

        log.info("?????????????????????????????????????????????");
        profileOutModel.setConcernedStatus(0);
        profileOutModel.setConcernUserStatus(0);
        if (null != request.getLoginUserId()) {
            UserConcernInModel userConcernInModel = new UserConcernInModel();
            userConcernInModel.setConcernUserId(Long.valueOf(userkgOutModel.getUserId()));
            userConcernInModel.setUserId(request.getLoginUserId());
            UserConcernOutModel userConcernOutModel = userConcernRMapper.getConcernInfo(userConcernInModel);
            if (userConcernOutModel != null) {
                profileOutModel.setConcernUserStatus(1);
            }
            userConcernInModel = new UserConcernInModel();
            userConcernInModel.setConcernUserId(request.getLoginUserId());
            userConcernInModel.setUserId(Long.valueOf(userkgOutModel.getUserId()));
            userConcernOutModel = userConcernRMapper.getConcernInfo(userConcernInModel);
            if (userConcernOutModel != null) {
                profileOutModel.setConcernedStatus(1);
            }

        }
        if (userkgOutModel.getUserRole() == 1) {
            return buildUserProfileColumnResponsexForNormal(userId, profileOutModel, userkgOutModel.getInviteCode());
        }
        return buildUserProfileColumnResponsex(comments, outcount, countArt, profileOutModel, kaSinModel,
                userkgOutModel.getInviteCode());
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @Override
    public AppJsonEntity sendValidationEmail(UserkgRequest request) {
        String emailNew = request.getVerIfy();
        String email = request.getUserEmail();
        String userName = request.getUserName();
        Long userId = request.getUserId();
        Result<String> result = checkCode(request.getCode(), request.getUserMobile());
        if (!result.isOk()) {
            return AppJsonEntity.makeExceptionJsonEntity("", result.getErrorMsg());
        }
        // ????????????getUserAssetInfo
        if (StringUtils.isNotEmpty(emailNew)) {
            Result<String> resultCheckEmail = checkEmail(request);
            // ???????????????????????????
            Result<Integer> emailCheck = userkgService.checkuserEmail(request);
            if (emailCheck == null) {
                return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
            }
            if (emailCheck.getData() <= 0) {
                return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
            }
            if (resultCheckEmail.isOk()) {
                return sendEmail(emailNew, userName, userId, email);
            }
            return AppJsonEntity.makeExceptionJsonEntity("", resultCheckEmail.getErrorMsg());
        }
        // ????????????
        request.setVerIfy(email);
        if (!userkgService.checkEmail(request)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????????????????");
        }
        return sendEmail(email, userName, userId, "");
    }

    @Override
    public AppJsonEntity modifyEmail(UserkgRequest request) {

        Result<String> checkcode = userkgService.checkSmsEmailcodeAndDel(request.getUserEmail());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "?????????????????????");
        }
        boolean success = userkgService.centerUped(request);
        if (!success) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public AppJsonEntity validationMobile(UserkgRequest request) {
        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public Result<UserkgResponse> checklong(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        model.setUserId(request.getUserId());
        model.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
        UserkgOutModel uskgoutmodel = userRMapper.selectByPrimaryForModifyPwd(model);
        log.info("checklong -> uskgoutmodel:" + JSONObject.toJSONString(uskgoutmodel));
        UserkgResponse response = new UserkgResponse();
        if (null != uskgoutmodel) {
            response.setUserId(uskgoutmodel.getUserId());
            response.setUserName(uskgoutmodel.getUserName());
            response.setUserEmail(uskgoutmodel.getUserEmail());
            response.setUserMobile(uskgoutmodel.getUserMobile());
            return new Result<>(response);
        }

        return new Result<>();
    }

    @Override
    public AppJsonEntity setPassword(UserkgRequest request) {

        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }
        Result<UserkgResponse> response = checklong(request);
        if (null == response.getData()) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }
        if (!userkgService.validateconfirmPwd(request)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "??????????????????????????????");
        }
        request.setUserId(Long.valueOf(response.getData().getUserId()));
        boolean success = userkgService.updatePssword(request);
        if (success) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }

    }

    @Override
    public AppJsonEntity getInviteInfo(UserkgResponse kguser) {
        UserRelationResponse userRelationResponse = new UserRelationResponse();
        userRelationResponse.setInviteCount(userRelationService.getInviteCount(Long.valueOf(kguser.getUserId())));
        UserProfileOutModel userProfileOutModel = userProfileRMapper
                .selectByPrimaryKey(Long.valueOf(kguser.getUserId()));

        UserkgRequest req = new UserkgRequest();
        req.setUserId(Long.valueOf(kguser.getUserId()));
        Result<UserkgResponse> response = userkgService.getUserDetails(req);
        if (CheckUtils.checkRetInfo(response)) {
            UserkgResponse ukrs = response.getData();
            String myCode = ukrs.getInviteCode();
            if (StringUtils.isBlank(myCode)) {
                myCode = userkgService.getInviteCode(); // ????????????????????????

                userkgService.updateInviteCode(Long.valueOf(kguser.getUserId()), myCode);
            }

            userRelationResponse.setInviteCode(myCode);
            userRelationResponse.setInviteStatus(ukrs.getBonusStatus());
            userRelationResponse.setInviteFreezeReason(ukrs.getBonusFreezeReason());
            if (userProfileOutModel != null) {
                userRelationResponse.setAvatar(userProfileOutModel.getAvatar());
            }
        }

        return AppJsonEntity.makeSuccessJsonEntity(userRelationResponse);
    }

    @Override
    public AppJsonEntity getTeacherInfo(UserkgResponse kguser) {
        Result<MasterInfoResponse> masterInfoResponse = discipleService.getMaterInfo(Long.valueOf(kguser.getUserId()));
        if (masterInfoResponse == null) {
            return null;
        }
        MasterInfoResponse masterInfo = masterInfoResponse.getData();
        if (masterInfo == null) {
            return null;
        }
        masterInfo = builderConcernSate(masterInfo, kguser);
        return AppJsonEntity.makeSuccessJsonEntity(masterInfo);
    }

    private MasterInfoResponse builderConcernSate(MasterInfoResponse masterInfo, UserkgResponse kguser) {
        if (masterInfo.gettUId() == null) {
            return null;
        }
        UserConcernOutModel userConcernOutModel = userConcernService.getConcernInfo(Long.valueOf(masterInfo.gettUId()), Long.valueOf(kguser.getUserId()));
        if (userConcernOutModel == null) {
            masterInfo.setConcernedStatus(0);
            return masterInfo;
        }
        masterInfo.setConcernedStatus(userConcernOutModel.getConcernStatus());
        return masterInfo;
    }

    @Cacheable(category = "PersonalCenterService", key = "#{request.currentPage}_#{kguser.userId}", expire = 3, dateType = DateEnum.MINUTES)
    @Override
    public AppJsonEntity getContributionList(DiscipleRequest request, PageModel<DiscipleInfoResponse> page,
                                             UserkgResponse kguser) {
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        request.setUserId(Long.valueOf(kguser.getUserId()));
        page = discipleService.getPupilTribute(request, page);
        if (page != null) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    @Override
    public AppJsonEntity bindingTeacher(UserRelationRequest request, UserkgResponse kguser) {
        if (null == request || null == request.getInviteCode()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        UserkgOutModel userkgOutModel = userService.selectByInviteCode(request.getInviteCode());
        // ??????????????????????????????
        if (userkgOutModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NONE_INVITER.getCode(),
                    ExceptionEnum.NONE_INVITER.getMessage());
        }
        // ???????????????????????????????????????
        UserRelation userRelation = userRelationService.getMyTeacher(Long.valueOf(kguser.getUserId()));
        if (userRelation != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.EXIST_TEACHER.getCode(),
                    ExceptionEnum.EXIST_TEACHER.getMessage());
        }
        // ??????????????????????????????????????????
        UserRelation teacherRelation = userRelationService
                .getMyTeacher(Long.valueOf(userkgOutModel.getUserId()));
        if (teacherRelation != null && teacherRelation.getUserId() == Long.parseLong(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TEACHER_BIND_SUB_ERROR.getCode(),
                    ExceptionEnum.TEACHER_BIND_SUB_ERROR.getMessage());
        }
        // ????????????????????????????????????
        if (userkgOutModel.getUserId().equals(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITER_SELF_ERROR.getCode(),
                    ExceptionEnum.INVITER_SELF_ERROR.getMessage());
        }
        // ????????????????????????????????????????????? ????????????????????????????????????
        UserQueryResponse uqr = userService.getUserInfo(Long.valueOf(kguser.getUserId()));
        if (uqr == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (uqr.getUserRole() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        // ???????????????????????????????????????
        if (userkgOutModel.getUserRole() == 1 && uqr.getUserRole() != 1) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_SPCOLUMN_ERROR.getCode(),
                    ExceptionEnum.BINGD_SPCOLUMN_ERROR.getMessage());
        }
        // ????????????
        request.setRelUserId(Long.valueOf(kguser.getUserId()));
        request.setUserId(Long.valueOf(userkgOutModel.getUserId()));
        Boolean isoK = userRelationService.bindTeacher(request);
        if (isoK) {
            Map<String, Object> map = new HashMap<>();
            if (userkgOutModel.getUserRole() != 1 && !userConcernService.isConcerned(Long.valueOf(userkgOutModel.getUserId()), Long.valueOf(kguser.getUserId()))) {
                map.put("concernMaster", 0);  //????????????0?????????????????????????????????1????????????/??????????????????
                map.put("msg", "???????????????????????????????????????????????????TA?????????TA?????????");
                return AppJsonEntity.makeSuccessJsonEntity(map);
            }
            map.put("concernMaster", 1);
            map.put("msg", "????????????");
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_ERROR.getCode(),
                ExceptionEnum.BINGD_ERROR.getMessage());
    }

    @Override
    public AppJsonEntity applyWithdraw(UserRelationRequest request, UserkgResponse kguser) {
        if (null == request) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));
        // ????????????????????????????????????????????????
        String jedisKey = JedisKey.getInviteWithdraw(request.getUserId());
        if (jedisUtils.get(jedisKey) != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_WITHDRAW_HANDLING.getCode(),
                    ExceptionEnum.INVITE_WITHDRAW_HANDLING.getMessage());
        }
        jedisUtils.set(jedisKey, request.getUserId(), 10);

        // ?????????????????????????????????
        UserkgRequest req = new UserkgRequest();
        req.setUserId(request.getUserId());
        Result<UserkgResponse> response = userkgService.getUserDetails(req);
        if (CheckUtils.checkRetInfo(response)) {
            UserkgResponse ukrs = response.getData();
            Integer bonusStatus = ukrs.getBonusStatus();
            if (bonusStatus == 0 || bonusStatus == 2) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_FREZEE.getCode(),
                        ExceptionEnum.INVITE_FREZEE.getMessage());
            }
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        // ??????????????????
        /*
         * if (StringUtils.isBlank(request.getTxPassword())) { return
         * AppJsonEntity.makeExceptionJsonEntity("", "?????????????????????"); }
         * AccountRequest accountRequest = new AccountRequest();
         * accountRequest.setTxPassword(request.getTxPassword());
         * accountRequest.setUserId(request.getUserId());
         * Result<AccountResponse> model =
         * accountService.validationPwd(accountRequest);
         *
         * if (null == model || null == model.getData()) {
         *
         * int txTimes =
         * userkgService.checkTxpassLimit(String.valueOf(request.getUserId()));
         * if (txTimes == 0) { return
         * AppJsonEntity.makeExceptionJsonEntity(20014,
         * "????????????????????????5???????????????????????????????????????"); } else { return
         * AppJsonEntity.makeExceptionJsonEntity(20014, "???????????????????????????" + (5 -
         * txTimes) + "??????????????????????????????5?????????????????????????????????????????????"); } }
         */

        Long _count = userRelationService.getInviteCount(request.getUserId());
        // ???????????????????????? ??????????????????
        if (null == _count || 0L == _count || InviteBonusEnum.TEN.get_count() > _count) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_WITHDRAW_ERROR.getCode(),
                    ExceptionEnum.INVITE_WITHDRAW_ERROR.getMessage());
        }
        request.setInviteCount(_count);

        // ????????????????????????
        BigDecimal _bonus = BigDecimal.ZERO;
        Long _inviteCount = request.getInviteCount();
        Long targetCount = request.getInviteCount();
        if (_inviteCount >= InviteBonusEnum.TEN.get_count() && _inviteCount < InviteBonusEnum.THRITY.get_count()) {
            targetCount = InviteBonusEnum.TEN.get_count();
            _bonus = InviteBonusEnum.TEN.get_bonus();

        } else if (_inviteCount >= InviteBonusEnum.THRITY.get_count()
                && _inviteCount < InviteBonusEnum.FIFTY.get_count()) {
            targetCount = InviteBonusEnum.THRITY.get_count();
            _bonus = InviteBonusEnum.THRITY.get_bonus();

        } else if (_inviteCount >= InviteBonusEnum.FIFTY.get_count()
                && _inviteCount < InviteBonusEnum.EIGHTY.get_count()) {
            targetCount = InviteBonusEnum.FIFTY.get_count();
            _bonus = InviteBonusEnum.FIFTY.get_bonus();

        } else if (_inviteCount >= InviteBonusEnum.EIGHTY.get_count()) {
            targetCount = InviteBonusEnum.EIGHTY.get_count();
            _bonus = InviteBonusEnum.EIGHTY.get_bonus();
        }
        // ???????????????????????????????????????

        request.set_bonus(_bonus);
        request.setTargetCount(targetCount);

        AccountInModel accountInModel = new AccountInModel();
        accountInModel.setUserId(Constants.PLATFORM_USER_ID);
        accountInModel.setBalance(_bonus);
        if (!accountService.validPlatformBalance(accountInModel)) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PLATFOEM_BALANCE_ERROR.getCode(),
                    ExceptionEnum.PLATFOEM_BALANCE_ERROR.getMessage());
        }
        // ????????????
        userWithdrawService.applyWithdraw(request);
        return AppJsonEntity.makeSuccessJsonEntity("??????????????????!");
    }

//    @Override
//    public AppJsonEntity amountOfTribute(UserkgResponse kguser) {
//        BigDecimal res = BigDecimal.ZERO;
//        AccountFlowInModel inModel = new AccountFlowInModel();
//        inModel.setUserId(Long.valueOf(kguser.getUserId()));
//        inModel.setBusinessTypeId(BusinessTypeEnum.SUBCONTRI.getStatus());
//        BigDecimal result = accountFlowTxbRMapper.countUserTypeFlow(inModel);
//        if (result != null) {
//            res = result;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("tributeAmount", res.toString());
//        return AppJsonEntity.makeSuccessJsonEntity(map);
//    }

    @Override
    public AppJsonEntity amountOfTribute(UserkgResponse kguser) {
        BigDecimal res = BigDecimal.ZERO;
        BasicDBObject querry = new BasicDBObject();
        querry.append("master_id", new BasicDBObject(Seach.EQ.getOperStr(), Long.valueOf(kguser.getUserId())));
        querry.append("coin_type", new BasicDBObject(Seach.EQ.getOperStr(), 2));
        log.info("--------amountOfTribute------------" + querry.toString());
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.SUB_TRIBUTE_RECORD, querry);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                BigDecimal amount = doc.get("amount") == null ? BigDecimal.ZERO : ((Decimal128) doc.get("amount")).bigDecimalValue();
                res = res.add(amount);

            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tributeAmount", res.stripTrailingZeros().toPlainString());
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }


    @Override
    public AppJsonEntity checkAppToken() {
        return AppJsonEntity.makeSuccessJsonEntity(null);
    }

    @Override
    public AppJsonEntity ritExchangeButton() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(CommonSettingEnum.RIT_EXCHANGE_BUTTON.getSettingKey());
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        RitButtonResponse response = JSONObject.parseObject(outModel.getSettingValue(), RitButtonResponse.class);
        log.info("RitButtonResponse > " + JSONObject.toJSONString(response));
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    @Override
    public AppJsonEntity ritRolloutButton() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(CommonSettingEnum.RIT_ROLLOUT_BUTTON.getSettingKey());
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        RitButtonResponse response = JSONObject.parseObject(outModel.getSettingValue(), RitButtonResponse.class);
        log.info("RitButtonResponse > " + JSONObject.toJSONString(response));
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    @Override
    public AppJsonEntity myCoinList(UserkgResponse kguser) {
        if (kguser == null || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        AccountOutModel outModel;
        try {
            String userIdStr = kguser.getUserId();
            Long userId = Long.valueOf(userIdStr);
            outModel = getBalaceInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (outModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNTERROR);
        }
        List<MyCoinListResponse> myCoinListResponses = new ArrayList<>();
        CoinEnum roles[] = CoinEnum.values();
        for (CoinEnum coinEnum : roles) {
        	if(coinEnum.getCardDisplayStatus()==0){
        		continue;
			}
            myCoinListResponses.add(initData(coinEnum, outModel));
        }
        if (CollectionUtils.isEmpty(myCoinListResponses)) {
            return AppJsonEntity.makeSuccessJsonEntity(new ArrayList<>());
        }
        return AppJsonEntity.makeSuccessJsonEntity(myCoinListResponses);
    }

    @Override
    public AppJsonEntity myCoinInfo(UserkgResponse kguser, AccountRequest request) {
        if (kguser == null || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        Long userId = Long.valueOf(kguser.getUserId());
        request.setUserId(userId);
        if (request.getUserId() == null || request.getCoinType() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Integer coinType = request.getCoinType();
        CoinEnum coinEnum = CoinEnum.getByCode(coinType);
        AccountOutModel outModel = getBalaceInfo(userId);
        if (outModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNTERROR);
        }
        CoinInfoResponse coinInfoResponse = initCoinInfoData(coinEnum, outModel);
        return AppJsonEntity.makeSuccessJsonEntity(coinInfoResponse);
    }

    @Override
    public AppJsonEntity buttonSet(ButtonSetRequest request) {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();

        if (request.getType() != 2 && request.getType() != 3) {
            return AppJsonEntity.makeSuccessJsonEntity(RitButtonResponse.init());
        }
        String settingKey = CommonSettingEnum.RIT_EXCHANGE_BUTTON.getSettingKey();
        if (request.getType() == 3) {
            settingKey = CommonSettingEnum.RIT_ROLLOUT_BUTTON.getSettingKey();
        }
        inModel.setSettingKey(settingKey);

        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        RitButtonResponse response = JSONObject.parseObject(outModel.getSettingValue(), RitButtonResponse.class);
        log.info("RitButtonResponse > " + JSONObject.toJSONString(response));
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    @Override
    public AppJsonEntity getCarouselListService(UserkgResponse kguser) {
        if (kguser == null || kguser.getUserId() == null) {
            log.info("?????????????????????,??????????????????,??????????????????");
            return new AppJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        //1 ????????????????????????
        List<DBObject> baseList = fetchConcernBaseInfo(Long.parseLong(kguser.getUserId()));
        //????????????????????????
        appendContributionInfo(kguser.getUserId(), baseList);
        //????????????,??????????????????
        appendArticleVideloCommentInfo(kguser.getUserId(), baseList);
        //?????????????????????????????????
        appendArticleVideloLikeInfo(kguser.getUserId(), baseList);
        commonService.doSortDBObjectListByField(baseList, "timestamp", "desc");
        HashMap<String, Object> rows = new HashMap<>();
        rows.put("data", baseList);
        rows.put("isApprentice", userRelationRMapper.selectInviteCountForApp(Long.parseLong(kguser.getUserId())) == 0 ? 0 : 1);
        return AppJsonEntity.makeSuccessJsonEntity(rows);
    }

    private void appendArticleVideloLikeInfo(String userId, List<DBObject> baseList) {

        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        query.put("collectDate", new BasicDBObject(Seach.GTE.getOperStr(), DateUtils.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, Constants.FRONTDAY)));
        MongoCursor<Document> cursor = MongoUtils.findByFilter(UserLogEnum.KG_USER_LIKE.getTable(), query);

        BasicDBObject concernDB;
        String time;
        HashMap<String, Object> modelMap;

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            Long articleId = doc.getLong("articleId");
            if (articleId == null || articleId <= 0) {
                continue;
            }
            concernDB = new BasicDBObject();
            time = doc.getDate("collectDate").getTime() + "";
            concernDB.put("timestamp", time);
            concernDB.put("time", DateUtils.formatDate(new Date(Long.parseLong(time)), "MM-dd HH:mm"));
            concernDB.put("targetID", doc.getLong("articleId").toString());

            ArticleInModel articleInModel = new ArticleInModel();
            articleInModel.setArticleId(articleId);
            ArticleOutModel articleOutModel = articleRMapper.selectArticleBase(articleInModel);

            modelMap = new HashMap<>();
            modelMap.put("publish_kind", articleOutModel.getPublishKind());
            modelMap.put("video_filename", articleOutModel.getVideoFilename());
            modelMap.put("article_title", articleOutModel.getArticleTitle());
            concernDB.put("info", assembleInfo(modelMap, "???"));

            UserInModel userInModel = new UserInModel();
            userInModel.setUserId(Long.valueOf(userId));
            UserkgOutModel userkgOutModel = userRMapper.getUserInfo(userInModel);
            concernDB.put("imgUrl", userkgOutModel.getAvatar() == null ? "" : userkgOutModel.getAvatar());
            concernDB.put("userName", userkgOutModel.getUserName());
            concernDB.put("dynamicType", 4);
            baseList.add(concernDB);
        }
    }

    private void appendArticleVideloCommentInfo(String userId, List<DBObject> baseList) {

        List<UserCommentOutModel> commentList = null;
        try {
            commentList = userCommentRMapper.getUserByCommentCnt(userId, DateUtils.formatDate(DateUtils.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, Constants.FRONTDAY)
                    , DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
/*            commentList = userCommentRMapper.getUserByCommentCnt(userId, "2018-04-02 00:00:00");*/
        } catch (Exception e) {
            log.error("???????????????????????? seq:5783947459823 ????????????:{}???", e.getMessage());
        }
        if (commentList == null || commentList.isEmpty()) {
            return;
        }
        BasicDBObject concernDB;
        String time;
        HashMap<String, Object> modelMap;
        for (UserCommentOutModel userCommentInModel : commentList) {
            if (userCommentInModel == null || userCommentInModel.getUserName() == null) {
                continue;
            }
            if (userCommentInModel.getPublishKind() == null) {
                continue;
            }
            concernDB = new BasicDBObject();
            time = userCommentInModel.getCommentDate().getTime() + "";
            concernDB.put("timestamp", time);
            concernDB.put("time", DateUtils.formatDate(new Date(Long.parseLong(time)), "MM-dd HH:mm"));
            concernDB.put("imgUrl", userCommentInModel.getAvaTar() == null ? "" : userCommentInModel.getAvaTar());
            modelMap = new HashMap<>();
            modelMap.put("publish_kind", userCommentInModel.getPublishKind());
            modelMap.put("article_title", userCommentInModel.getArticleTitle());
            modelMap.put("video_filename", userCommentInModel.getVideoFilename());
            concernDB.put("info", assembleInfo(modelMap, "??????"));
            concernDB.put("userName", userCommentInModel.getUserName());
            concernDB.put("dynamicType", 3);
            concernDB.put("targetID", userCommentInModel.getArticleId().toString());
            baseList.add(concernDB);
        }
    }


    private String assembleInfo(HashMap commentMap, String commonStr) {
        return commonStr + "????????????" + commentMap.get("article_title") + "???";
    }

    public static void main(String[] args) {
        System.err.println(DateUtils.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, Constants.FRONTDAY).getTime());
    }

    private void appendContributionInfo(String userId, List<DBObject> baseList) {
        List<DBObject> data = null;
        try {
            DB db = MongoDao.INSTANCE.getMongoDB();
            data = db.getCollection(MongoTables.SUB_TRIBUTE_RECORD).find(getQueryFilter(Long.parseLong(userId))).toArray();
        } catch (NumberFormatException e) {
            log.error("?????????mongo???????????????????????? seq:5647386 ???????????? e:{}???", e.getMessage());
        }
        if (data == null || data.size() == 0) {
            return;
        }
        //??????????????????userId  ??????mysql???????????????????????????
        List<Long> userIds = parserUserIds(data);
        //????????????????????????
        List<UserkgOutModel> userInfoList = userRMapper.selectMoreParentInfo(userIds);
        //????????????
        try {
            mergeData(data, userInfoList, baseList);
        } catch (Exception e) {
            log.error("??????????????? seq:574893758943 e:{}???", e.getMessage());
        }
    }

    private void mergeData(List<DBObject> data, List<UserkgOutModel> userInfoList, List<DBObject> baseList) throws Exception {

        HashMap<Long, Object> userMap = commonService.transferFormat(userInfoList, "UserId");
        BasicDBObject concernDB;
        Long userId;
        UserkgOutModel userkgOutModel;
        for (DBObject db : data) {
            concernDB = new BasicDBObject();
            if (db.get("sub_id") == null) {
                continue;
            }
            userId = Long.parseLong(db.get("sub_id") + "");
            if (userMap.get(userId) == null) {
                log.warn("??????????????????????????????userId ???{}???", userId);
                continue;
            }
            userkgOutModel = ((UserkgOutModel) userMap.get(userId));
            concernDB.put("timestamp", db.get("create_date"));
            concernDB.put("time", DateUtils.formatDate(new Date(Long.parseLong(db.get("create_date") + "")), "MM-dd HH:mm"));
            concernDB.put("imgUrl", userkgOutModel.getAvatar());
            concernDB.put("info", "???????????????" + new BigDecimal(db.get("amount") + "").stripTrailingZeros().toPlainString() + "??????");
            concernDB.put("userName", userkgOutModel.getUserName());
            concernDB.put("dynamicType", 2);
            concernDB.put("targetID", "");
            baseList.add(concernDB);
        }
    }

    private List<Long> parserUserIds(List<DBObject> data) {
        Set<Long> idsSet = new HashSet<>();
        for (DBObject db : data) {
            if (db.get("sub_id") == null) {
                continue;
            }
            idsSet.add(Long.parseLong(db.get("sub_id") + ""));
        }
        return new ArrayList<>(idsSet);
    }

    private BasicDBObject getQueryFilter(Long userId) {
        //??????????????????
        BasicDBObject filterQuery = new BasicDBObject()
                .append("master_id", userId)
                .append("coin_type", 2);

        //?????????7?????????
        BasicDBObject dateFiled = new BasicDBObject()
                .append("$gte", DateUtils.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, Constants.FRONTDAY).getTime());

        filterQuery.append("create_date", dateFiled);
        return filterQuery;
    }

    private List<DBObject> fetchConcernBaseInfo(Long userId) {
        UserConcernInModel model = new UserConcernInModel();
        model.setUserId(userId);
        model.setLastTime(DateUtils.formatDate(DateUtils.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, Constants.FRONTDAY)
                , DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));

        List<UserConcernListOutModel> data = userConcernRMapper.getConcernedList(model);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        List<DBObject> result = new ArrayList<>();
        //???????????? ??????
        /**
         * time - value
         * info - value
         */
        BasicDBObject concernDB;
        for (UserConcernListOutModel userModel : data) {
            concernDB = new BasicDBObject();
            concernDB.put("timestamp", userModel.getCreateDate().getTime());
            concernDB.put("time", DateUtils.formatDate(userModel.getCreateDate(), "MM-dd HH:mm"));
            concernDB.put("imgUrl", userModel.getAvatar() == null ? "" : userModel.getAvatar());
            concernDB.put("userName", userModel.getUserName());
            concernDB.put("info", Constants.CONCERNBASEINFO);
            concernDB.put("dynamicType", 1);
            concernDB.put("targetID", "");
            result.add(concernDB);
        }
        return result;
    }


    /**
     * ?????????????????????????????????
     *
     * @param userName
     * @return
     */
    private String getConcernBriefInfo(String userName) {
        return userName + "????????????";
    }

    private CoinInfoResponse initCoinInfoData(CoinEnum coinEnum, AccountOutModel outModel) {
        MyCoinListResponse myCoinListResponse = initData(coinEnum, outModel);
        CoinInfoResponse coinInfoResponse = new CoinInfoResponse();
        BeanUtils.copyProperties(myCoinListResponse, coinInfoResponse);
        coinInfoResponse = initIntro(coinInfoResponse, coinEnum);
        return coinInfoResponse;
    }

    private CoinInfoResponse initIntro(CoinInfoResponse coinInfoResponse, CoinEnum coinEnum) {
        CoinOutModel coinOutModel = coinMapper.getByType(coinEnum.getCode());
        String intro = "";
        if (coinOutModel != null) {
            intro = coinOutModel.getIntro();
        }
        coinInfoResponse.setIntro(intro);
        return coinInfoResponse;
    }

    private MyCoinListResponse initData(CoinEnum coinEnum, AccountOutModel outModel) {
        MyCoinListResponse response = MyCoinListResponse.initCoinData(coinEnum);
        String balance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getBalance());
        String txbBalance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getTxbBalance());
        String ritBalance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getRitBalance());

        String frozenBalance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getFrozenBalance());
        String frozenTxbBalance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getTxbFrozenBalance());
        String frozenRitBalance = NumberUtils.formatBigDecimalForMyCoinList(outModel.getRitFrozenBalance());

        CoinOutModel coinOutModel = coinMapper.getByType(coinEnum.getCode());
        String background = coinOutModel.getBackground();

        if (coinEnum.equals(CoinEnum.KG)) {
            balance = txbBalance;
            frozenBalance = frozenTxbBalance;
        }
        if (coinEnum.equals(CoinEnum.RIT)) {
            balance = ritBalance;
            frozenBalance = frozenRitBalance;
        }
        response.setBalance(StringUtils.isEmpty(balance) ? "0.000" : balance);
        response.setFrozenBalance(StringUtils.isEmpty(frozenBalance) ? "0.000" : frozenBalance);
        response.setBackground(background);
        return response;
    }


    /**
     * ????????????ID????????????????????????
     *
     * @param userId
     * @return
     */
    private AccountOutModel getBalaceInfo(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(userId);
        return accountRMapper.selectByUserbalance(inModel);
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @return
     */
    private Result<String> checkEmail(UserkgRequest request) {
        if (!userkgService.checkEmail(request)) {
            return new Result<>(ERROR_RESULT_CODE, "?????????????????????????????????");
        }
        request.setVerIfy(request.getUserEmail());
        if (!userkgService.checkEmail(request)) {
            return new Result<>(ERROR_RESULT_CODE, "????????????????????????");
        }
        return new Result<>();
    }

    /**
     * ???????????????
     *
     * @param code
     * @param userMobile
     * @return
     */
    private Result<String> checkCode(String code, String userMobile) {
        if (StringUtils.isEmpty(code)) {
            return new Result<>(ERROR_RESULT_CODE, "??????????????????");
        }
        Result<String> checkcode = userkgService.checkSmsEmailcode(userMobile);
        if (checkcode.getData() == null) {
            return new Result<>(ERROR_RESULT_CODE, "???????????????");
        }
        if (!checkcode.getData().equals(code)) {
            return new Result<>(ERROR_RESULT_CODE, "???????????????????????????");
        }
        return new Result<>();
    }

    /**
     * ??????????????????
     *
     * @param toEmail
     * @param userName
     * @return
     */
    private AppJsonEntity sendEmail(String toEmail, String userName, Long userId, String emailOld) {
        // ??????????????????
        String key = UUID.randomUUID().toString() + new Date().getTime();
        // ????????????key??????redis?????????????????????24??????
        Result<String> keyResult = userkgService.getSendSmsEmailKey(key, toEmail);
        MailModel mail = new MailModel();
        mail.setToEmails(toEmail);
        mail.setContent(getHtml(userName, keyResult.getData(), toEmail, userId, emailOld));
        emailService.sendEmail(mail);
        return AppJsonEntity.makeSuccessJsonEntity("???????????????");
    }

    /**
     * ??????????????????????????????
     *
     * @param userId
     * @return
     */
    private UserProfileColumnAppResponse buildUserProfileColumnResponsexForNormal(Long userId,
                                                                                  UserProfileOutModel profileOutModel, String inviteCode) {
        UserProfileOutModel userProfileOutModel = userProfileRMapper.selectBaseInfoByPrimaryKey(userId);
        UserProfileColumnAppResponse response = new UserProfileColumnAppResponse();
        response.setColumnAvatar(userProfileOutModel.getAvatar());
        response.setColumnName(userProfileOutModel.getUsername());
        response.setCreateUser(String.valueOf(userProfileOutModel.getUserId()));
        response.setColumnIntro(userProfileOutModel.getResume());
        response.setUserRole(USER_ROLE_NORMAL);
        response.setInviteCode(inviteCode);
        response.setConcernedStatus(profileOutModel.getConcernedStatus());
        response.setConcernUserStatus(profileOutModel.getConcernUserStatus());
        response = buildTags(userId, response);
        return buildUserBaseInfo(response, profileOutModel);
    }

    private UserProfileColumnAppResponse buildTags(Long userId, UserProfileColumnAppResponse response) {
        UserTagBuild userTag = userTagsUtil.buildTags(userId);
        response.setRealAuthedTag(userTag.getRealAuthedTag());
        response.setIdentityTag(userTag.getIdentityTag());
        response.setVipTag(userTag.getVipTag());
        return response;
    }

    private UserProfileColumnAppResponse buildUserBaseInfo(UserProfileColumnAppResponse response,
                                                           UserProfileOutModel profileOutModel) {
        response.setCountry(profileOutModel.getCountry());
        response.setProvince(profileOutModel.getProvince());
        response.setCity(profileOutModel.getCity());
        response.setCounty(profileOutModel.getCounty());
        response.setResume(profileOutModel.getResume());
        response.setSex(profileOutModel.getSex());
        return response;
    }

    /**
     * ??????????????????????????????
     *
     * @param comments
     * @param outcount
     * @param countArt
     * @param profileOutModel
     * @param kaSinModel
     * @return
     */
    private UserProfileColumnAppResponse buildUserProfileColumnResponsex(UserProfileOutModel comments,
                                                                         UserProfileOutModel outcount, long countArt, UserProfileOutModel profileOutModel,
                                                                         KgArticleStatisticsInModel kaSinModel, String inviteCode) {
        UserProfileColumnAppResponse response = new UserProfileColumnAppResponse();
        if (profileOutModel != null) {
            response.setColumnName(profileOutModel.getColumnName());
            response.setColumnAvatar(profileOutModel.getColumnAvatar());
            response.setColumnIntro(profileOutModel.getColumnIntro());
            if (comments != null) {
                response.setComments(comments.getComments());
            }
            if (outcount != null) {
                response.setPbowsenum(outcount.getPbowsenum());
            }
            response.setArtsum(countArt);
            response.setCreateUser(profileOutModel.getUserId().toString());
            long count = kgArticleStatisticsRMapper.getThumbupNum(kaSinModel);
            response.setThumbupNum(count);
            response.setUserRole(USER_ROLE_AUTHOR); // ??????
            response.setCountry(profileOutModel.getColumnCountry());
            response.setProvince(profileOutModel.getColumnProvince());
            response.setCity(profileOutModel.getColumnCity());
            response.setCounty(profileOutModel.getColumnCounty());
            response.setResume(profileOutModel.getColumnIntro());
            response.setSex(profileOutModel.getSex());
            response.setInviteCode(inviteCode);
            response.setConcernedStatus(profileOutModel.getConcernedStatus());
            response.setConcernUserStatus(profileOutModel.getConcernUserStatus());
            response = buildTags(profileOutModel.getUserId(), response);

        }
        return response;
    }

    private String getHtml(String userName, String key, String email, Long userId, String emailOld) {
        String address = config.getAddress();
        String url = config.getUrl();
        Map<String, Object> map = new HashMap<>();
        map.put("code", key);
        map.put("userEmail", email);
        map.put("userId", userId);
        map.put("emailOld", emailOld);
        String data = new String(Base64.getEncoder().encode(JSONObject.toJSONString(map).getBytes()));
        String link = address + url + "?data=" + data;
        return "<p><h4>" + userName + " ??????:  ????????????????????????????????????????????????????????????e-mail:</h4></p>"
                + "<p><a style=\"color: blue\" href=\"" + link + "\">" + link + "</a></p>";
    }
}
