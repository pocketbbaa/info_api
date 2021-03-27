package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.dao.entity.UserProfile;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.UserRelationRMapper;
import com.kg.platform.dao.write.UserActiveWMapper;
import com.kg.platform.dao.write.UserProfileWMapper;
import com.kg.platform.dao.write.UserRelationWMapper;
import com.kg.platform.dao.write.UserWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.UserActiveInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.in.UserProfileInModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.PushService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.app.UserConcernService;
import com.kg.platform.service.app.UserKgAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/20.
 */
@Service
public class UserKgAppServiceImpl implements UserKgAppService {
    private Logger logger = LoggerFactory.getLogger(UserKgAppServiceImpl.class);

    @Inject
    private UserkgService userkgService;

    @Inject
    private IDGen idGenerater;

    @Inject
    private UserProfileWMapper userProfileWMapper;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    private SendSms sendSms;

    @Inject
    private TokenManager tokenManager;

    @Inject
    private JedisUtils jedisUtils;

    @Inject
    private UserWMapper userWMapper;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private MQProduct mqProduct;

    @Inject
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Inject
    private UserRelationRMapper userRelationRMapper;

    @Inject
    private UserRelationWMapper userRelationWMapper;

    @Inject
    private UserTagsUtil userTagsUtil;

    @Inject
    private UserActiveWMapper userActiveWMapper;

    @Inject
    private AccountService accountService;

    @Inject
    private UserLogUtil userLogUtil;

    @Autowired
    private UserConcernService userConcernService;

    @Autowired
    private PushService pushService;

    @Override
    public void pushBindInfo(HttpServletRequest servletRequest, String userId) {
        String deviceId = servletRequest.getHeader("device_id");
        int osVersion = servletRequest.getIntHeader("os_version");
        pushService.pushBindInfo(deviceId, osVersion, userId);
    }

    public void mqPush(HttpServletRequest servletRequest, String userId, int type, String tags) {
        pushService.tagBind(userId, servletRequest.getHeader("device_id"),
                servletRequest.getIntHeader("os_version"), type, tags);
    }

    @Override
    public AppJsonEntity appRegister(UserkgRequest request, HttpServletRequest servletRequest) {
        if (StringUtils.isBlank(request.getUserMobile())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        boolean success;

        // 验证码验证
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
            if (checkcode.getData() == null) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码不正确");
            } else if (!checkcode.getData().equals(request.getCode())) {
                return AppJsonEntity.makeExceptionJsonEntity("", "验证码有误");
            }
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        /**
         * 用户手机是否已经注册
         */
        if (null != request.getUserMobile()) {
            if (userkgService.checkuserMobile(request).getData() > 0) {
                UserInModel inModel = new UserInModel();
                inModel.setUserMobile(request.getUserMobile());
                UserkgOutModel outModel = userRMapper.getUserInfo(inModel);
                TokenModel tokenModel = tokenManager.createAppToken(Long.valueOf(outModel.getUserId()));
                Map<String, Object> map = new HashMap<>();
                UserkgResponse response = new UserkgResponse();
                response.setUserName(outModel.getUserName());
                response.setAvatar(outModel.getAvatar());
                response.setToken(tokenModel.getUserId() + "_" + tokenModel.getToken());
                UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(outModel.getUserId()), outModel);
                response.setIdentityTag(userTagBuild.getIdentityTag());
                response.setRealAuthedTag(userTagBuild.getRealAuthedTag());
                response.setVipTag(userTagBuild.getVipTag());
                map.put("kgUser", response);
                map.put("mloginBonusStatus", propertyLoader.getProperty("common", "global.mloginBonusSTATUS"));

                // 记录登录日志
                userLogUtil.recordLoginLog(Long.valueOf(outModel.getUserId()),
                        servletRequest.getIntHeader("os_version"), outModel.getUserMobile());

                // 告诉MQ进行设备绑定推送
                pushBindInfo(servletRequest, outModel.getUserId());

                return new AppJsonEntity(ExceptionEnum.MOBILE_USED.getCode(), ExceptionEnum.MOBILE_USED.getMessage(),
                        map);
            }
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        // 未完善注册信息，不需校验密码
        request.setUserName(StringUtils.encryptionString(request.getUserMobile(), 4, 7));
        // request.setUserPassword(RandomUtils.genRandomPassword());

        if ("adduser".equals(request.getCall_method())) {

            request.setUserId(idGenerater.nextId());
            request.setRegisterIp(HttpUtil.getIpAddr(servletRequest));
            request.setMobileAuthed(true);
            request.setCreateDate(new Date());
            request.setRegisterOrigin(servletRequest.getIntHeader("os_version"));

            success = userkgService.checkAddUser(request);
            if (!success) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            } else {
                //生成用户后（属于注册行为：将注册渠道写入)
                appChannel(request.getUserId(), servletRequest);

                Result<UserkgResponse> responseResult = mobileLogin(request, servletRequest);
                if (responseResult.getData() != null) {
                    TokenModel tokenModel = tokenManager.createAppToken(request.getUserId());
                    Map<String, Object> map = new HashMap<>();
                    UserkgResponse response = new UserkgResponse();
                    response.setToken(tokenModel.getUserId() + "_" + tokenModel.getToken());
                    response.setUserName(request.getUserName());
                    UserkgResponse userkgResponse = responseResult.getData();
                    response.setIdentityTag(userkgResponse.getIdentityTag());
                    response.setRealAuthedTag(userkgResponse.getRealAuthedTag());
                    response.setVipTag(userkgResponse.getVipTag());
                    map.put("kgUser", response);
                    map.put("mloginBonusSTATUS", propertyLoader.getProperty("common", "global.mloginBonusSTATUS"));
                    // 告诉MQ进行设备绑定推送
                    pushBindInfo(servletRequest, request.getUserId().toString());
                    return AppJsonEntity.makeSuccessJsonEntity(map);
                }
            }
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "方法有误");
    }

    /**
     * 发送短信验证码
     *
     * @param request
     * @param req
     * @return
     */
    @Override
    public AppJsonEntity appSendSmsCode(UserkgRequest request, HttpServletRequest req) {
        String remoteIp = HttpUtil.getIpAddr(req);
        if (!userkgService.checkRegisterLimit(remoteIp)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "您的请求太频繁，请稍后重试");
        }

        if ("".equals(request.getVerIfy()) && null == request.getVerIfy()) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入要发送验证码的手机号码！");
        }

        String code = RandomUtils.generationCode();
        Result<String> vatcode = userkgService.getSendSmsEmailcode(code, request.getVerIfy());
        if (StringUtils.isEmpty(request.getVerIfy())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "手机号不存在");
        }

        request.setUserMobile(request.getVerIfy());
        if (StringUtils.isEmpty(request.getMobileArea())) {
            UserInModel inModel = new UserInModel();
            inModel.setUserMobile(request.getVerIfy());
            UserkgOutModel outModel = userRMapper.getUserInfo(inModel);
            if (outModel != null) {
                request.setMobileArea(outModel.getMobileArea());
            }
        }
        String msg = org.apache.commons.lang3.StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######", vatcode.getData());

        if ("86".equals(request.getMobileArea())) {
            boolean isPhone = userkgService.checkMobile(request);
            if (!isPhone) {
                return AppJsonEntity.makeExceptionJsonEntity("", "手机号不存在");
            }
            logger.info("发送短信=======" + msg);
            sendSms.send(request.getUserMobile(), msg);
        } else {
            logger.info("发送短信=======" + msg);
            sendSms.sendInternationalMessage(request.getUserMobile(), msg);
        }

        return AppJsonEntity.makeExceptionJsonEntity("10000", "短信已发出");
    }

    /**
     * 验证验证码和手机或者邮箱是否匹配
     */
    @Override
    public AppJsonEntity chckSmsEmailCode(UserkgRequest request) {
        if (null == request.getUserMobile() || "".equals(request.getUserMobile())) {
            request.setVerIfy(request.getUserEmail());
        } else {
            request.setVerIfy(request.getUserMobile());
        }

        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码不正确");
        } else if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity("", "验证码有误");
        }

        jedisUtils.del(JedisKey.vatcodeKey(request.getVerIfy()));
        return AppJsonEntity.makeSuccessJsonEntity("验证成功");
    }

    /**
     * 注册后完善资料
     *
     * @param request
     * @param kguser
     * @return
     */
    @Override
    @Transactional
    public AppJsonEntity completeProfile(UserkgRequest request, UserkgResponse kguser, HttpServletRequest req) {

        if (StringUtils.isNotBlank(request.getUserMobile()) && StringUtils.isNotBlank(request.getCode())
                && StringUtils.isNotBlank(request.getUserPassword())
                && StringUtils.isNotBlank(request.getConfirmPassword())) {
            // 由于修改密码，校验短信验证码 避免绕过
            UserkgRequest userkgRequest = new UserkgRequest();
            userkgRequest.setUserMobile(request.getUserMobile());
            userkgRequest.setCode(request.getCode());
            AppJsonEntity result = chckSmsEmailCode(userkgRequest);
            if (!"10000".equals(result.getCode())) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            }
            // 校验两次密码是否一致
            if (userkgService.validatePwd(request) != true) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_FORMAT.getCode(),
                        ExceptionEnum.PASSWORD_FORMAT.getMessage());
            }
            if (userkgService.validateconfirmPwd(request) != true) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_CONFIRM.getCode(),
                        ExceptionEnum.PASSWORD_CONFIRM.getMessage());
            }
            // 必要参数不为空
            UserInModel userInModel = new UserInModel();
            userInModel.setUserId(Long.parseLong(kguser.getUserId()));
            userInModel.setUserName(request.getUserName());
            userInModel.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
            int state1 = userWMapper.updateByUserSelective(userInModel);
            if (StringUtils.isNotBlank(request.getAvatar()) && state1 > 0) {
                // 头像不为空 修改头像
                UserProfileInModel inModel = new UserProfileInModel();
                inModel.setUserId(Long.parseLong(kguser.getUserId()));
                inModel.setAvatar(request.getAvatar());
                int state2 = userProfileWMapper.updateProfile(inModel);
                if (state2 == 0) {
                    throw new BusinessException(ExceptionEnum.SYS_EXCEPTION.getCode(),
                            ExceptionEnum.SYS_EXCEPTION.getMessage());
                }
            }
            // 如果是通过邀请码注册，建立关联关系
            if (StringUtils.isNotBlank(request.getInviteCode())
                    && userkgService.checkInviteCode(request.getInviteCode())) {
                UserkgOutModel kgUser = userRMapper.selectByInviteCode(request.getInviteCode());
                if (!Check.NuNObject(kgUser)) {
                    UserRelation userRel = new UserRelation();
                    userRel.setRelId(idGenerater.nextId());
                    userRel.setUserId(Long.parseLong(kgUser.getUserId()));
                    userRel.setRelUserId(Long.valueOf(kguser.getUserId()));
                    if (request.getActivityId() != null) {
                        userRel.setActivityId(request.getActivityId());
                    }
                    if (kgUser.getBonusStatus() == 1) {
                        userRel.setRelType(BonusRelTypeEnum.INVITE.getType());
                    } else {
                        userRel.setRelType(BonusRelTypeEnum.BINDING.getType());
                    }
                    int check = userRelationRMapper.ifRel(userRel);
                    if (check == 0) {
                        userRelationWMapper.insertSelective(userRel);
                    }
                    beFans(kgUser, Long.valueOf(kguser.getUserId()));
                }
            }
            return AppJsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

    }

    /**
     * 若师傅为专栏作者，则自动关注师傅成为其粉丝
     *
     * @param kgUser 师傅
     * @param userId 用户ID
     */
    private void beFans(UserkgOutModel kgUser, Long userId) {
        logger.info("如果你的师傅是千氪专栏作者，自动关注 师傅->kgUser：" + JSONObject.toJSONString(kgUser) + "   用户->userId:" + userId);
        if (kgUser.getUserRole() != 1 && !userConcernService.isConcerned(Long.valueOf(kgUser.getUserId()), userId)) {
            UserkgResponse kguser = new UserkgResponse();
            kguser.setUserId(String.valueOf(userId));
            UserConcernRequest request = new UserConcernRequest();
            request.setConcernUserId(kgUser.getUserId());
            userConcernService.concernUser(kguser, request);
        }
    }

    public boolean appChannel(Long userId, HttpServletRequest servletRequest) {
        UserProfile in = new UserProfile();
        in.setUserId(userId);
        String channel = servletRequest.getHeader("channel");
        int os = servletRequest.getIntHeader("os_version");
        if (os == 1) {
            //IOS渠道
            IosChannelEnum[] iosChannelEnums = IosChannelEnum.values();
            for (IosChannelEnum iosChannelEnum : iosChannelEnums) {
                if (iosChannelEnum.getChannelName().equals(channel)) {
                    in.setRegistChannel(iosChannelEnum.getChannelKey());
                    break;
                }
            }

        }
        if (os == 2) {
            //安卓渠道
            AndoridChannelEnum[] andoridChannelEnums = AndoridChannelEnum.values();
            for (AndoridChannelEnum andoridChannelEnum : andoridChannelEnums) {
                if (andoridChannelEnum.getChannelName().equals(channel)) {
                    in.setRegistChannel(andoridChannelEnum.getChannelKey());
                    break;
                }
            }
        }
        logger.info("APP渠道包来源为------" + in.getRegistChannel());
        if (in.getRegistChannel() != null) {
            return userProfileWMapper.updateByPrimaryKeySelective(in) > 0 ? true : false;
        }
        return false;
    }

    /**
     * 用户使用手机号验证码登录/使用账号密码方式登录
     *
     * @param request
     * @return
     */
    @Override
    public AppJsonEntity checkLogin(UserkgRequest request, HttpServletRequest req) {
        if (StringUtils.isBlank(request.getUserMobile())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        if (StringUtils.isNotBlank(request.getCall_method())) {
            // 方式不为空
            if (request.getCall_method().equals("0")) {
                // 验证码方式登录

                // 校验验证码
                UserkgRequest userkgRequest = new UserkgRequest();
                userkgRequest.setUserMobile(request.getUserMobile());
                userkgRequest.setCode(request.getCode());
                AppJsonEntity result = chckSmsEmailCode(userkgRequest);
                if (!"10000".equals(result.getCode())) {
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码不正确");
                }
                // 验证通过
                Result<Integer> userOut = userkgService.checkuserMobile(request);
                if (null == userOut || userOut.getData() == 0) {

                    request.setUserId(idGenerater.nextId());
                    request.setRegisterIp(HttpUtil.getIpAddr(req));
                    request.setMobileAuthed(true);
                    request.setCreateDate(new Date());
                    // request.setUserPassword(RandomUtils.genRandomPassword());
                    request.setUserName(request.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    request.setRegisterOrigin(req.getIntHeader("os_version"));
                    boolean state = userkgService.checkAddUser(request);
                    //新用户直接生成（属于注册行为：将注册渠道写入）
                    if (state) {
                        appChannel(request.getUserId(), req);
                    }

                }

                Result<UserkgResponse> userkgResponse = mobileLogin(request, req);
                if (userkgResponse.getData() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("kgUser", userkgResponse.getData());
                    // 告诉MQ进行设备绑定推送
                    pushBindInfo(req, userkgResponse.getData().getUserId());
                    if (userOut != null && userOut.getData() != 0) {
                        // 用户是老用户，查看新闻推送开关状态 进行相应的tag操作
                        KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper
                                .selectByPrimaryKey(Long.valueOf(userkgResponse.getData().getUserId()));
                        if (outModel == null || outModel.getHotNewsSwitch() == 1) {
                            // 打开状态
                            mqPush(req, null, PushTypeEnum.BINDING_TAG.getCode(), "newsTagIfBind");
                        } else if (outModel.getHotNewsSwitch() == 0) {
                            // 关闭状态
                            mqPush(req, null, PushTypeEnum.UNBINDING_TAG.getCode(), "newsTagIfBind");
                        }
                    }
                    return AppJsonEntity.makeSuccessJsonEntity(map);
                }
            }
            if (request.getCall_method().equals("1")) {
                // 账号密码方式登录
                String remoteIp = HttpUtil.getIpAddr(req);


                Result<UserkgResponse> userkgResponse = checkAppLogin(request, req);
                if (userkgResponse.getData() != null) {
                    // logger.info("用户登录：" + JSON.toJSONString(request) + "成功");
                    userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));

                    this.jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
                    this.jedisUtils.del(JedisKey.loginIp(remoteIp));
                    Map<String, Object> map = new HashMap<>();
                    map.put("kgUser", userkgResponse.getData());
                    // 告诉MQ进行设备绑定推送
                    pushBindInfo(req, userkgResponse.getData().getUserId());
                    // 用户是老用户，查看新闻推送开关状态 进行相应的tag操作
                    KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper
                            .selectByPrimaryKey(Long.valueOf(userkgResponse.getData().getUserId()));
                    if (outModel == null || outModel.getHotNewsSwitch() == 1) {
                        // 打开状态
                        mqPush(req, null, 6, "newsTagIfBind");
                    } else if (outModel.getHotNewsSwitch() == 0) {
                        // 关闭状态
                        mqPush(req, null, 7, "newsTagIfBind");
                    }
                    return AppJsonEntity.makeSuccessJsonEntity(map);
                } else {

                    int loginTimes = userkgService.checkLoginLimit(remoteIp, request);

                    logger.info("用户登录：" + JSON.toJSONString(request) + "失败");
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                            ExceptionEnum.USER_LONGFAILURE.getMessage() + "，还有" + (5 - loginTimes)
                                    + "次机会，连续输错超过5次，您的账号将被暂时锁定");
                }
            }
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(),
                    ExceptionEnum.SYS_EXCEPTION.getMessage());
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @Override
    public AppJsonEntity checkLoginForM(UserkgRequest request) {
        if (StringUtils.isBlank(request.getUserMobile())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        if (StringUtils.isNotBlank(request.getCall_method())) {
            // 方式不为空
            if (request.getCall_method().equals("0")) {
                // 验证码方式登录

                // 校验验证码
                UserkgRequest userkgRequest = new UserkgRequest();
                userkgRequest.setUserMobile(request.getUserMobile());
                userkgRequest.setCode(request.getCode());
                AppJsonEntity result = chckSmsEmailCode(userkgRequest);
                if (!"10000".equals(result.getCode())) {
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码不正确");
                }
                // 验证通过
                Result<Integer> userOut = userkgService.checkuserMobile(request);
                if (null == userOut || userOut.getData() == 0) {

                    request.setUserId(idGenerater.nextId());
                    request.setRegisterIp(request.getUserIp());
                    request.setMobileAuthed(true);
                    request.setCreateDate(new Date());
                    // request.setUserPassword(RandomUtils.genRandomPassword());
                    request.setUserName(request.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    request.setRegisterOrigin(request.getOsVersion());
                    boolean state = userkgService.checkAddUser(request);

                }

                Result<UserkgResponse> userkgResponse = mobileLogin(request, null);
                if (userkgResponse.getData() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("kgUser", userkgResponse.getData());
                    return AppJsonEntity.makeSuccessJsonEntity(map);
                }
            }
            if (request.getCall_method().equals("1")) {
                // 账号密码方式登录
                String remoteIp = request.getUserIp();


                Result<UserkgResponse> userkgResponse = checkAppLogin(request, null);
                if (userkgResponse.getData() != null) {
                    // logger.info("用户登录：" + JSON.toJSONString(request) + "成功");
                    userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));

                    this.jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
                    this.jedisUtils.del(JedisKey.loginIp(remoteIp));
                    Map<String, Object> map = new HashMap<>();
                    map.put("kgUser", userkgResponse.getData());
                    return AppJsonEntity.makeSuccessJsonEntity(map);
                } else {

                    int loginTimes = userkgService.checkLoginLimit(remoteIp, request);

                    logger.info("用户登录：" + JSON.toJSONString(request) + "失败");
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                            ExceptionEnum.USER_LONGFAILURE.getMessage() + "，还有" + (5 - loginTimes)
                                    + "次机会，连续输错超过5次，您的账号将被暂时锁定");
                }
            }
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(),
                    ExceptionEnum.SYS_EXCEPTION.getMessage());
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    private Result<UserkgResponse> checkAppLogin(UserkgRequest request, HttpServletRequest req) {
        UserInModel model = new UserInModel();
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        UserkgResponse response = new UserkgResponse();

        model.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
        UserkgOutModel outmodel = userRMapper.getUserInfo(model);

        if (outmodel != null) {

            UserkgRequest ur = new UserkgRequest();
            ur.setUserId(Long.parseLong(outmodel.getUserId()));
            Result<UserkgResponse> userResult = userkgService.getUserDetails(ur);
            CheckUtils.checkRetInfo(userResult, true);

            UserkgResponse ukr = userResult.getData();
            userkgService.checkLockStatus(ukr, null);

            UserkgOutModel uskgoutmodel = userRMapper.selectByPrimary(model);
            if (uskgoutmodel != null) {

                TokenModel tokenModel = tokenManager.createAppToken(Long.parseLong(uskgoutmodel.getUserId()));
                UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(uskgoutmodel.getUserId()),
                        uskgoutmodel);
                // 设置返回参数
                response.setTokenModel(tokenModel);
                response.setToken(tokenModel.getUserId() + "_" + tokenModel.getToken());
                response.setUserId(uskgoutmodel.getUserId());
                response.setUserName(uskgoutmodel.getUserName());
                response.setUserEmail(uskgoutmodel.getUserEmail());
                response.setUserMobile(uskgoutmodel.getUserMobile());
                response.setUserRole(uskgoutmodel.getUserRole());
                response.setArticlesum(uskgoutmodel.getArticlesum());
                response.setRealnameAuthed(uskgoutmodel.getRealnameAuthed());
                response.setTradepasswordSet(uskgoutmodel.getTradepasswordSet());
                response.setAtskMobile(uskgoutmodel.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                response.setMobileArea(uskgoutmodel.getMobileArea());
                response.setRefuseReason(uskgoutmodel.getRefuseReason());// 实名不通过原因
                response.setCertificateStatus(uskgoutmodel.getCertificateStatus());// 实名认证状态
                response.setAvatar(uskgoutmodel.getAvatar());
                response.setIdentityTag(userTagBuild.getIdentityTag());
                response.setRealAuthedTag(userTagBuild.getRealAuthedTag());
                response.setVipTag(userTagBuild.getVipTag());

                UserActiveInModel uAinModel = new UserActiveInModel();
                uAinModel.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
                uAinModel.setLastloginIp(HttpUtil.getIpAddr(req));
                uAinModel.setLastloginTime(new Date());
                userActiveWMapper.updateUserSelective(uAinModel);

                AccountRequest accountReq = new AccountRequest();
                accountReq.setUserId(tokenModel.getUserId());
                Integer loginBonusStatus = 1;// (accountService.loginBonus(accountReq)
                // == false) ? 1 : 0;

                response.setLoginBonusStatus(loginBonusStatus);

                // 记录登录日志
                userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req != null ? req.getIntHeader("os_version") : 7,
                        response.getUserMobile());
                return new Result<>(response);
            }
            return new Result<>();

        }
        return new Result<>();
    }

    @Transactional
    public Result<UserkgResponse> mobileLogin(UserkgRequest request, HttpServletRequest req) {
        logger.info("手机验证码登录：UserkgRequest={}");

        UserInModel model = new UserInModel();
        model.setUserMobile(request.getUserMobile());

        UserkgOutModel uskgoutmodel = userRMapper.getUserInfo(model);
        if (uskgoutmodel == null) {
            throw new ApiMessageException("参数错误，请检查输入。");
        }

        logger.info("===uskgoutmodel{}", JSON.toJSONString(uskgoutmodel));

        UserkgResponse response = new UserkgResponse();

        UserkgRequest ur = new UserkgRequest();
        ur.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        Result<UserkgResponse> userResult = userkgService.getUserDetails(ur);
        CheckUtils.checkRetInfo(userResult, true);

        logger.info("===kguser{}", JSON.toJSONString(userResult));

        UserkgResponse ukr = userResult.getData();
        userkgService.checkLockStatus(ukr, null);

        TokenModel tokenModel = tokenManager.createAppToken(Long.parseLong(uskgoutmodel.getUserId()));

        UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(uskgoutmodel.getUserId()), uskgoutmodel);
        // 设置返回参数
        response.setTokenModel(tokenModel);
        response.setToken(tokenModel.getUserId() + "_" + tokenModel.getToken());
        response.setUserId(uskgoutmodel.getUserId());
        response.setUserName(uskgoutmodel.getUserName());
        response.setUserEmail(uskgoutmodel.getUserEmail());
        response.setUserMobile(uskgoutmodel.getUserMobile());
        response.setUserRole(uskgoutmodel.getUserRole());
        response.setArticlesum(uskgoutmodel.getArticlesum());
        response.setRealnameAuthed(uskgoutmodel.getRealnameAuthed());
        response.setTradepasswordSet(uskgoutmodel.getTradepasswordSet());
        response.setAtskMobile(uskgoutmodel.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        response.setMobileArea(uskgoutmodel.getMobileArea());
        response.setAvatar(uskgoutmodel.getAvatar());
        response.setInviteCode(uskgoutmodel.getInviteCode());
        response.setIdentityTag(userTagBuild.getIdentityTag());
        response.setRealAuthedTag(userTagBuild.getRealAuthedTag());
        response.setVipTag(userTagBuild.getVipTag());

        UserActiveInModel uAinModel = new UserActiveInModel();
        uAinModel.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        uAinModel.setLastloginIp(HttpUtil.getIpAddr(req));
        uAinModel.setLastloginTime(new Date());
        userActiveWMapper.updateUserSelective(uAinModel);

        AccountRequest accountReq = new AccountRequest();
        accountReq.setUserId(tokenModel.getUserId());

        Integer loginBonusStatus = (!accountService.loginBonus(tokenModel.getUserId(),
                LoginTypeEnum.MOBILE_LOGIN.getStatus())) ? 1 : 0;

        response.setLoginBonusStatus(loginBonusStatus);

        // 记录登录日志
        userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req != null ? req.getIntHeader("os_version") : 7,
                response.getUserMobile());
        return new Result<UserkgResponse>(response);
    }


}
