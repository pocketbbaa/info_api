package com.kg.platform.service.m.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.framework.idgen.IDGenerater;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.*;
import com.kg.platform.controller.UserkgController;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.enumeration.LoginTypeEnum;
import com.kg.platform.enumeration.RegisterOriginEnum;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.mongoTable.EconnoisseurRecordMongo;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.*;
import com.kg.platform.service.app.UserConcernService;
import com.kg.platform.service.app.UserKgAppService;
import com.kg.platform.service.m.MArticleService;
import com.kg.platform.service.m.MUserService;
import org.apache.commons.collections.map.HashedMap;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/19.
 */
@Service("mUserService")
public class MUserServiceImpl implements MUserService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserkgService userkgService;

    @Autowired
    private IDGenerater idGenerater;

    @Autowired
    private UserAccountService userAccountService;

    @Resource(name = "propertyLoader")
    private PropertyLoader propertyLoader;

    @Autowired
    private UserKgAppService userKgAppService;

    @Autowired
    private MArticleService mArticleService;

    @Autowired
    private UserkgController userkgController;

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserCommentService commentService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserConcernService userConcernService;

    @Autowired
    private UserRMapper userRMapper;

    @Inject
    private JedisUtils jedisUtils;

    @Override
    public MJsonEntity register(UserkgRequest request) {
        boolean success;
        // ???????????????
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
            if (checkcode.getData() == null) {
                return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "??????????????????");
            } else if (!checkcode.getData().equals(request.getCode())) {
                return MJsonEntity.makeExceptionJsonEntity("", "???????????????");
            }
        } else {
            return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        /**
         * ??????????????????????????????
         */
        if (StringUtils.isNotBlank(request.getUserMobile())) {
            if (userkgService.checkuserMobile(request).getData() > 0) {
                return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_USED);
            }
        }
        //?????????????????????????????????
        MJsonEntity check = checkEeconnoisseur(request.getUserMobile(), request.getUserIp());
        if (check != null) {
            return check;
        }

        if ("adduser".equals(request.getCall_method())) {

            request.setUserId(idGenerater.nextId());
            request.setRegisterIp(request.getUserIp());
            request.setMobileAuthed(true);
            request.setCreateDate(new Date());
            request.setRegisterOrigin(RegisterOriginEnum.M.getOrigin());
            request.setUserName(StringUtils.encryptionString(request.getUserMobile(), 4, 7));
            try {
                success = userkgService.checkAddUser(request);
            } catch (ApiMessageException e) {
                return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_ERROR);
            } catch (Exception e) {
                throw new BusinessException("", e.getMessage());
            }

            if (!success) {
                return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            } else {
                accountService.loginBonus(request.getUserId(), LoginTypeEnum.MOBILE_LOGIN.getStatus());
                return MJsonEntity.makeSuccessJsonEntity("");
            }

        } else {
            return MJsonEntity.makeExceptionJsonEntity("", "????????????");
        }
    }

    @Override
    public MJsonEntity sendSmsCode(UserkgRequest userkgRequest, UserkgMCodeRequest codeRequest) {
        userkgRequest.setCallerName(codeRequest.getCallerName());
        userkgRequest.setSession(codeRequest.getSession());
        userkgRequest.setToken(codeRequest.getToken());
        userkgRequest.setSig(codeRequest.getSig());
        userkgRequest.setPlatform(codeRequest.getPlatform());
        userkgRequest.setScene(codeRequest.getScene());
        JsonEntity result = userkgController.sendSmsEmailcode(userkgRequest, mArticleService.buildMHttpRequest(userkgRequest.getUserIp()));
        MJsonEntity response = new MJsonEntity();
        response.setResponseTime(result.getResponseTime());
        response.setResponseReq(result.getResponseReq());
        response.setData(result.getResponseBody());
        response.setMessage(result.getMessage());
        response.setCode(result.getCode());
        response.setAccountStr(result.getAccountStr());
        return response;
    }

    @Override
    public MJsonEntity sendSmsCodeLogin(UserkgRequest userkgRequest, UserkgMCodeRequest codeRequest) {
        JsonEntity result = userkgController.sendSmsEmailcodeMLogin(userkgRequest, mArticleService.buildMHttpRequest(userkgRequest.getUserIp()));
        MJsonEntity response = new MJsonEntity();
        response.setResponseTime(result.getResponseTime());
        response.setResponseReq(result.getResponseReq());
        response.setData(result.getResponseBody());
        response.setMessage(result.getMessage());
        response.setCode(result.getCode());
        response.setAccountStr(result.getAccountStr());
        return response;
    }


    @Override
    public MJsonEntity selectByuserprofileId(UserProfileRequest request) {
        request.setPort("M");
        Result result = userProfileService.selectByuserprofileId(request);
        return MJsonEntity.makeSuccessJsonEntity(result.getData());
    }

    @Override
    public MJsonEntity getByIdProfile(UserProfileRequest request) {
        Result<UserProfileResponse> result = userProfileService.getByIdProfile(request);
        return MJsonEntity.makeSuccessJsonEntity(result.getData());
    }

    @Override
    public MJsonEntity getOthersComment(UserCommentRequest request) {
        PageModel<UserCommentResponse> page = new PageModel<>();
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = commentService.getOthersComment(request, page);
        return MJsonEntity.makeSuccessJsonEntity(page);
    }

    @Override
    public MJsonEntity getFansList(UserConcernRequest request) {
        PageModel<UserConcernListResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        UserkgResponse user = new UserkgResponse();
        user.setUserId(request.getUserId().toString());
        AppJsonEntity appJsonEntity = userConcernService.fansList(user, pageModel);
        MJsonEntity mJsonEntity = new MJsonEntity();
        BeanUtils.copyProperties(appJsonEntity, mJsonEntity);
        return mJsonEntity;
    }


    private MJsonEntity checkEeconnoisseur(String mobile, String ip) {
        int serviceSwitch = 1;
        KgCommonSettingInModel kgCommonSettingInModel = new KgCommonSettingInModel();
        kgCommonSettingInModel.setSettingKey(ServiceSwitchResponse.SETTING_KEY);
        KgCommonSettingOutModel kgCommonSettingOutModel = kgCommonSettingRMapper.selectBySettingKey(kgCommonSettingInModel);
        if (kgCommonSettingOutModel != null) {
            ServiceSwitchResponse serviceSwitchResponse = JSON.parseObject(kgCommonSettingOutModel.getSettingValue(), ServiceSwitchResponse.class);
            serviceSwitch = serviceSwitchResponse.getCheckEeconnoisseur();
            logger.info("?????????????????????:{}", JSON.toJSONString(serviceSwitchResponse));
        }
        if (serviceSwitch == 0) {
            return null;
        }
        String apiUrl = propertyLoader.getProperty("common", "chuangLanCheck.api.url");
        String appId = propertyLoader.getProperty("common", "chuangLanCheck.appId");
        String appKey = propertyLoader.getProperty("common", "chuangLanCheck.appKey");
        Map<String, Object> param = new HashedMap();
        param.put("appId", appId);
        param.put("appKey", appKey);
        param.put("mobile", mobile);
        param.put("ip", ip);
        String result = HttpUtil.post(apiUrl, param);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        if ("200000".equals(code)) {
            String status = data.getString("status");
            if ("B1".equals(status) || "B2".equals(status)) {
                //?????????????????? ?????????MONGO ?????????
                EconnoisseurRecordMongo econnoisseurRecordMongo = new EconnoisseurRecordMongo();
                econnoisseurRecordMongo.setEconnoisseurMobile(mobile);
                econnoisseurRecordMongo.setEconnoisseurIp(ip);
                econnoisseurRecordMongo.setTime(String.valueOf(new Date().getTime()));
                MongoUtils.insertOne(MongoTables.ECONNOISSEUR_RECORD, new Document(Bean2MapUtil.bean2map(econnoisseurRecordMongo)));
                logger.error("????????????????????????mobile:{},ip:{}", mobile, ip);
                return MJsonEntity.makeExceptionJsonEntity("20008", "??????????????????????????????");
            }
            return null;
        } else if ("500005".equals(code)) {
            logger.error("?????????????????????????????????????????????????????????????????????");
            return null;
        } else {
            return null;
        }
    }

    @Override
    public MJsonEntity invite(UserkgRequest request) {
        if (null == request.getCode() || null == request.getUserMobile()) {
            return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(), ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (null == request.getMobileArea() || request.getMobileArea().equals("86")) {
            if (request.getUserMobile().startsWith("170") || request.getUserMobile().startsWith("171")) {
                return MJsonEntity.makeExceptionJsonEntity("", "???????????????????????????????????????");
            }
        }
        // ???????????????
        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
        if (null == checkcode) {
            return MJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        if (checkcode.getData() == null) {
            return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        }
        if (!checkcode.getData().equals(request.getCode())) {
            return MJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        UserInModel userInModel = new UserInModel();
        userInModel.setUserMobile(request.getUserMobile());
        UserkgOutModel model = userRMapper.selectUser(userInModel);
        if (null != model) {
            //????????????
            return MJsonEntity.makeSuccessJsonEntity("inviteCode", model.getInviteCode());
        } else {
            request.setMobileAuthed(true);
            request.setRegisterOrigin(RegisterOriginEnum.H5.getOrigin());
            request.setUserId(idGenerater.nextId());
            String inviteCode = userkgService.invite(request);
            //??????????????? ??????????????????
            if (null != inviteCode) {
                accountService.loginBonus(request.getUserId(), LoginTypeEnum.MOBILE_LOGIN.getStatus());
                return MJsonEntity.makeSuccessJsonEntity("inviteCode", inviteCode);
            } else {
                return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(), ExceptionEnum.USER_ADDFAILURE.getMessage());
            }
        }
    }


    /**
     * ????????????????????????????????????/??????????????????????????????
     *
     * @param request
     * @return
     */
    @Override
    public MJsonEntity checkLogin(UserkgRequest request) {
        AppJsonEntity appJsonEntity = userKgAppService.checkLoginForM(request);
        if (appJsonEntity.isSuccess()) {
            return MJsonEntity.makeSuccessJsonEntity(appJsonEntity.getData());
        }
        return MJsonEntity.makeExceptionJsonEntity(appJsonEntity.getCode(), appJsonEntity.getMessage());
    }
}
