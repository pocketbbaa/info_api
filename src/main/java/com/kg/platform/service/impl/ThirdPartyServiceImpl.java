package com.kg.platform.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.thirdLogin.weibo.WBUserInfo;
import com.kg.platform.common.thirdLogin.weibo.WbAuthInfo;
import com.kg.platform.common.thirdLogin.weibo.WeiBoUtil;
import com.kg.platform.common.thirdLogin.weixin.OAuthInfo;
import com.kg.platform.common.thirdLogin.weixin.WeixinUtil;
import com.kg.platform.common.thirdLogin.weixin.WxUserInfo;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.ThirdPartyLoginRMapper;
import com.kg.platform.dao.read.UserCertRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.ThirdPartyLoginWMapper;
import com.kg.platform.dao.write.UserActiveWMapper;
import com.kg.platform.dao.write.UserProfileWMapper;
import com.kg.platform.dao.write.UserRelationWMapper;
import com.kg.platform.dao.write.UserWMapper;
import com.kg.platform.enumeration.AuditStatusEnum;
import com.kg.platform.enumeration.BonusRelTypeEnum;
import com.kg.platform.enumeration.LockStatusEnum;
import com.kg.platform.enumeration.LockUnitEnum;
import com.kg.platform.enumeration.LoginTypeEnum;
import com.kg.platform.enumeration.UserLevelEnum;
import com.kg.platform.enumeration.UserRoleEnum;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.ThirdPartyLoginInModel;
import com.kg.platform.model.in.UserActiveInModel;
import com.kg.platform.model.in.UserCertInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.in.UserProfileInModel;
import com.kg.platform.model.out.CheckThirdUserOutModel;
import com.kg.platform.model.out.ThirdPartyLoginOutModel;
import com.kg.platform.model.out.UserCertOutModel;
import com.kg.platform.model.out.UserProfileOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.ThirdPartyLoginRequest;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.ThirdPartyService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UserAccountService;
import com.kg.platform.service.UserProfileService;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.admin.UserService;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyServiceImpl.class);

    @Inject
    JedisUtils jedisUtils;

    @Inject
    private IDGen idGenerater;

    @Inject
    ThirdPartyLoginWMapper thirdPartyLoginWMapper;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    private AccountService accountService;

    @Inject
    private UserkgService userkgService;

    @Inject
    private UserAccountService userAccountService;

    @Inject
    private TokenManager tokenManager;

    @Inject
    private UserProfileWMapper userProfileWMapper;

    @Inject
    UserCertRMapper userCertRMapper;

    @Inject
    ThirdPartyLoginRMapper thirdPartyLoginRMapper;

    @Inject
    UserWMapper userWMapper;

    @Inject
    UserRelationWMapper userRelationWMapper;

    @Inject
    UserActiveWMapper userActiveWMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Inject
    UserRMapper userRMapper;

    @Inject
    private UserLogUtil userLogUtil;

    @Override
    public OAuthInfo getAccessToken(String code) {
        OAuthInfo oAuthInfo = WeixinUtil.getAccessToken(code);
        if (oAuthInfo != null) {
            // 将返回的授权信息放入redis
            jedisUtils.set(JedisKey.wxInfoKey(oAuthInfo.getOpenId()), JsonUtil.writeValueAsString(oAuthInfo), 180);
            return oAuthInfo;
        }
        return null;
    }

    @Override
    public WxUserInfo getWxUserInfo(String openId) {
        // 从redis中获取accessToken
        String str = jedisUtils.get(JedisKey.wxInfoKey(openId));
        OAuthInfo oAuthInfo = new OAuthInfo();
        if (StringUtils.isNotEmpty(str)) {
            oAuthInfo = JsonUtil.readJson(str, OAuthInfo.class);
        }
        return WeixinUtil.getWxUser(oAuthInfo);
    }

    @Override
    public WbAuthInfo getWbAccessToken(String code) {
        WbAuthInfo wbAuthInfo = WeiBoUtil.getAccessToken(code);
        if (wbAuthInfo != null) {
            // 将返回的授权信息放入redis
            jedisUtils.set(JedisKey.wbInfoKey(wbAuthInfo.getUid()), JsonUtil.writeValueAsString(wbAuthInfo), 180);
            return wbAuthInfo;
        }
        return null;
    }

    @Override
    public WBUserInfo getWbUserInfo(String openId) {
        String str = jedisUtils.get(JedisKey.wbInfoKey(openId));
        WbAuthInfo wbAuthInfo = new WbAuthInfo();
        if (StringUtils.isNotEmpty(str)) {
            wbAuthInfo = JsonUtil.readJson(str, WbAuthInfo.class);
        }
        return WeiBoUtil.getWbUserInfo(wbAuthInfo);
    }

    @Override
    public boolean insertThirdPartyInfo(ThirdPartyLoginInModel thirdPartyLoginInModel) {
        thirdPartyLoginInModel.setThirdPartyId(idGenerater.nextId());
        thirdPartyLoginInModel.setBindTime(new Date());
        return thirdPartyLoginWMapper.insertSelective(thirdPartyLoginInModel) > 0;
    }

    @Override
    public Result<UserkgResponse> checkThirdUserLogin(ThirdPartyLoginInModel thirdPartyLoginInModel,
            HttpServletRequest req) {
        Result<UserkgResponse> resp = new Result<UserkgResponse>();
        CheckThirdUserOutModel c = new CheckThirdUserOutModel();
        // 判断是否绑定过手机号
        // 微博
        if (thirdPartyLoginInModel.getOpenType() == 0) {
            WbAuthInfo wb = WeiBoUtil.getAccessToken(thirdPartyLoginInModel.getCode());
            if (wb == null) {
                resp = new Result<UserkgResponse>();
                // 异常返回码 代表微博授权失败 跳转重新微博授权
                logger.info("异常返回码 代表微博授权失败 跳转重新微博授权");
                UserkgResponse userkgResponse = new UserkgResponse();
                userkgResponse.setBindStatus("2");
                resp.setData(userkgResponse);
                return resp;
            }
            WBUserInfo win = WeiBoUtil.getWbUserInfo(wb);
            c.setHeadImgurl(win.getProfile_image_url());
            c.setOpenId(wb.getUid());
            thirdPartyLoginInModel.setOpenId(wb.getUid());
            c.setNickName(win.getScreen_name());
            jedisUtils.set(JedisKey.wbInfoKey(wb.getUid()), JsonUtil.writeValueAsString(wb), 1000);
        } else {
            logger.info("获取code>>>>>>>>>>>" + thirdPartyLoginInModel.getCode());
            OAuthInfo oa = WeixinUtil.getAccessToken(thirdPartyLoginInModel.getCode());
            logger.info("获取AccessToken>>>>>>>>>>>" + JsonUtil.writeValueAsString(oa));
            if (oa == null) {
                resp = new Result<UserkgResponse>();
                // 异常返回码 代表微信授权失败 跳转重新微信授权
                logger.info("异常返回码 代表微信授权失败 跳转重新微信授权");
                UserkgResponse userkgResponse = new UserkgResponse();
                userkgResponse.setBindStatus("3");
                resp.setData(userkgResponse);
                return resp;
            }
            WxUserInfo wxUserInfo = WeixinUtil.getWxUser(oa);
            c.setHeadImgurl(wxUserInfo.getHeadImgurl());
            c.setNickName(wxUserInfo.getNickName());
            c.setOpenId(oa.getOpenId());
            thirdPartyLoginInModel.setOpenId(oa.getOpenId());
            thirdPartyLoginInModel.setRefreshAccessToken(oa.getRefreshToken());
            jedisUtils.set(JedisKey.wxInfoKey(oa.getOpenId()), JsonUtil.writeValueAsString(oa), 1000);
        }
        Boolean isBind = thirdPartyLoginRMapper.checkUserBind(thirdPartyLoginInModel.getOpenId() + "") > 0;
        if (isBind) {
            // 检查来源 1 代表来源于账户已登录关联账号
            if ("1".equals(thirdPartyLoginInModel.getSource())) {
                // 未注册情况
                UserkgResponse userkgResponse = new UserkgResponse();
                userkgResponse.setBindStatus("4");
                userkgResponse.setOpenId(c.getOpenId());
                resp.setData(userkgResponse);
            } else {
                // 返回登录信息
                UserInModel userInModel = new UserInModel();
                userInModel.setOpenId(thirdPartyLoginInModel.getOpenId() + "");
                UserkgOutModel userkgOutModel = userRMapper.getUserInfoByOpenId(userInModel);
                UserkgRequest userkgRequest = new UserkgRequest();
                userkgRequest.setUserMobile(userkgOutModel.getUserMobile());
                logger.info(">>>>>用户是手机号>>>>>>" + userkgOutModel.getUserMobile());
                resp = this.getLoginInfo(userkgRequest, req);
                logger.info(">>>>>返回查询信息>>>>>>" + resp);
                // 已注册情况
                resp.getData().setOpenId(userInModel.getOpenId());
                resp.getData().setBindStatus("1");
                resp.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));
            }
        } else {
            UserkgResponse userkgResponse = new UserkgResponse();
            userkgResponse.setHeadImgurl(c.getHeadImgurl());
            userkgResponse.setNickName(c.getNickName());
            // 未注册情况
            userkgResponse.setBindStatus("0");
            userkgResponse.setOpenId(c.getOpenId());
            resp.setData(userkgResponse);
        }

        return resp;
    }

    @Override
    public Result<UserkgResponse> getLoginInfo(UserkgRequest userkgRequest, HttpServletRequest req) {
        UserInModel model = new UserInModel();
        model.setUserMobile(userkgRequest.getUserMobile());
        UserkgOutModel uskgoutmodel = userRMapper.getUserInfo(model);
        if (uskgoutmodel == null) {
            throw new ApiMessageException("参数错误，请检查输入。");
        }

        logger.info("===uskgoutmodel{}", JSON.toJSONString(uskgoutmodel));

        UserkgResponse response = new UserkgResponse();

        UserkgRequest ur = new UserkgRequest();
        ur.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        Result<UserkgResponse> userResult = getUserDetails(ur);
        CheckUtils.checkRetInfo(userResult, true);

        logger.info("===kguser{}", JSON.toJSONString(userResult));

        UserkgResponse ukr = userResult.getData();
        checkLockStatus(ukr);

        TokenModel tokenModel = tokenManager.createToken(Long.parseLong(uskgoutmodel.getUserId()));
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
        // response.setRefuseReason(uskgoutmodel.getRefuseReason());// 实名不通过原因
        // response.setCertificateStatus(uskgoutmodel.getCertificateStatus());//
        // 实名认证状态

        UserActiveInModel uAinModel = new UserActiveInModel();
        uAinModel.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        uAinModel.setLastloginIp(HttpUtil.getIpAddr(req));
        uAinModel.setLastloginTime(new Date());
        userActiveWMapper.updateUserSelective(uAinModel);

        Integer loginBonusStatus = (accountService.loginBonus(tokenModel.getUserId(),
                LoginTypeEnum.MOBILE_LOGIN.getStatus()) == false) ? 1 : 0;

        response.setLoginBonusStatus(loginBonusStatus);

        //记录登录日志
        userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req.getIntHeader("os_version"), response.getUserMobile());
        return new Result<UserkgResponse>(response);
    }

    /**
     * 个人中心账号安全显示信息接口
     */
    @Override
    public Result<UserkgResponse> getUserDetails(UserkgRequest request) {
        // logger.info("个人中心账号安全：UserkgRequest={}", JSON.toJSONString(request));
        UserInModel inModel = new UserInModel();
        inModel.setUserId(request.getUserId());
        UserkgOutModel model = userRMapper.getUserDetails(inModel.getUserId());
        UserCertInModel userCertInModel = new UserCertInModel();
        userCertInModel.setUserId(String.valueOf(request.getUserId()));
        UserCertOutModel userCertOutModel = userCertRMapper.selectByUser(userCertInModel);

        UserkgResponse response = new UserkgResponse();
        if (model != null) {
            response.setUserRole(Integer.parseInt(model.getUserRole().toString()));
            response.setAuditStatus(model.getAuditStatus());
            response.setUserId(model.getUserId().toString());
            response.setUserEmail(model.getUserEmail());
            response.setUserMobile(model.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            response.setMobIle(model.getUserMobile());
            response.setApplyRole(model.getApplyRole());
            response.setInviteCode(model.getInviteCode());
            response.setBonusStatus(model.getBonusStatus());
            response.setBonusFreezeReason(model.getBonusFreezeReason());
            response.setInviteStatus(model.getInviteStatus());

            if (userCertOutModel == null) {
                response.setRealnameAuthed(-1);// 未实名
            } else {
                response.setRealnameAuthed(userCertOutModel.getStatus());
                response.setRealName(userCertOutModel.getRealName());
                String regex = "(\\w{3})(\\w+)(\\w{2})";
                String idcardNo = userCertOutModel.getIdcardNo().replaceAll(regex, "$1*************$3");
                response.setIdcardNo(idcardNo);
                response.setIdcardFront(userCertOutModel.getIdcardFront());
                response.setCertRefuseReason(userCertOutModel.getCertRefuseReason());
            }

            response.setTradepasswordSet(model.getTradepasswordSet());// 是否设置交易密码
            response.setDepositStatus(model.getDepositStatus());// 是否缴纳保证金

            response.setLockStatus(model.getLockStatus());
            response.setLockDate(model.getLockDate());
            response.setLockTime(model.getLockTime());
            response.setLockUnit(model.getLockUnit());
            response.setRefuseReason(model.getRefuseReason());
        }
        return new Result<UserkgResponse>(response);
    }

    @Override
    public void checkLockStatus(UserkgResponse ukr) {

        Integer lockStatus = ukr.getLockStatus();
        if (lockStatus != null && lockStatus.intValue() == LockStatusEnum.LOCKED.getStatus()) {
            Date lockDate = ukr.getLockDate();
            Integer lockTime = Integer.parseInt(ukr.getLockTime());
            Integer lockUnit = ukr.getLockUnit();
            String lockUnitStr = null;
            boolean locked = false;

            String customerServicePhone = propertyLoader.getProperty("common", "global.CustomerServicePhone");

            if (!Check.NuNObject(lockDate, lockTime, lockUnit)) {

                LockUnitEnum lockUnitEnum = LockUnitEnum.getLockStatusEnum(lockUnit.intValue());
                lockUnitStr = lockUnitEnum.getDisplay();

                switch (lockUnitEnum) {
                case YEAR:
                    lockDate = DateUtils.addYears(lockDate, lockTime);
                    break;
                case MONTH:
                    lockDate = DateUtils.addMonths(lockDate, lockTime);
                    break;
                case WEEK:
                    lockDate = DateUtils.addWeeks(lockDate, lockTime);
                    break;
                case DAY:
                    lockDate = DateUtils.addDays(lockDate, lockTime);
                    break;
                case HOUR:
                    lockDate = DateUtils.addHours(lockDate, lockTime);
                    break;
                case FOREVER:
                    locked = true;
                    break;
                default:
                    locked = false;
                }

            }

            Date now = new Date();
            if (locked || now.getTime() < lockDate.getTime()) {

                String errorStr = "您的账号因违规操作，已被系统锁定 " + lockTime + lockUnitStr
                        + " ，这期间您可以正常浏览并转发文章，但无法进行其他操作。如有疑问，请咨询客服：" + customerServicePhone;

                throw new ApiMessageException(ExceptionEnum.LOCKERROR.getCode(), errorStr);
            } else {
                Result<String> result = userService.lockUser(StringUtils.convertString2LongList(ukr.getUserId(), ","),
                        Constants.ADMIN_USER_ID, null, null);
                CheckUtils.checkRetInfo(result);

            }

        }
    }

    @Override
    @Transactional
    public ThirdPartyLoginInModel bindRegistUser(UserkgRequest request, HttpServletRequest requestse) {
        // 设置微博或者微信信息
        Boolean isok = true;
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setThirdPartyId(idGenerater.nextId());
        thirdPartyLoginInModel.setUserId(request.getUserId());
        thirdPartyLoginInModel.setBindTime(new Date());
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        // 设置第三方信息
        this.getAuthFromRedis(thirdPartyLoginInModel, request);
        // 插入第三方信息
        isok = thirdPartyLoginWMapper.insertSelective(thirdPartyLoginInModel) > 0;
        logger.info("// 插入profile");
        // 插入profile
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        inModel.setAvatar(request.getAvatar());
        inModel.setRoleId("1");
        isok = userProfileWMapper.insertSelective(inModel) > 0;
        if (isok) {
            return thirdPartyLoginInModel;
        }
        return null;
    }

    @Override
    @Transactional
    public ThirdPartyLoginInModel bindMobileLogin(UserkgRequest request) {
        Boolean isok = true;
        // 绑定第三方登录信息
        // 绑定账号
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        thirdPartyLoginInModel.setOpenId(request.getOpenId());
        ThirdPartyLoginOutModel th = thirdPartyLoginRMapper.checkBindStatusByOpenId(thirdPartyLoginInModel);
        if (th == null) {
            thirdPartyLoginInModel = new ThirdPartyLoginInModel();
            thirdPartyLoginInModel.setThirdPartyId(idGenerater.nextId());
            thirdPartyLoginInModel.setUserId(Long.valueOf(request.getUserId()));
            thirdPartyLoginInModel.setBindTime(new Date());
            thirdPartyLoginInModel.setOpenType(request.getOpenType());
            this.getAuthFromRedis(thirdPartyLoginInModel, request);
            thirdPartyLoginInModel.setAvatar(request.getAvatar());
            isok = thirdPartyLoginWMapper.insertSelective(thirdPartyLoginInModel) > 0;
        }
        // 查询是否注册过账号
        UserProfileRequest urq = new UserProfileRequest();
        urq.setUserId(request.getUserId());
        UserProfileOutModel upMode = userProfileService.getUserProfile(urq);
        if (upMode == null) {
            // 插入profile
            UserProfileRequest userProfileRequest = new UserProfileRequest();
            userProfileRequest.setUserId(request.getUserId());
            userProfileRequest.setAvatar(request.getAvatar());
            userProfileRequest.setRoleId(1);
            isok = userProfileService.initUserProfile(userProfileRequest);
        }
        if (isok) {
            return thirdPartyLoginInModel;
        }
        return null;
    }

    @Override
    public Boolean bindLoginUser(UserkgRequest request, HttpServletRequest requestse) {
        Boolean isOk = true;
        // 绑定账号
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setThirdPartyId(idGenerater.nextId());
        thirdPartyLoginInModel.setUserId(request.getUserId());
        thirdPartyLoginInModel.setBindTime(new Date());
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        this.getAuthFromRedis(thirdPartyLoginInModel, request);
        isOk = thirdPartyLoginWMapper.insertSelective(thirdPartyLoginInModel) > 0;
        return isOk;
    }

    @Override
    @Transactional
    public Boolean unbind(ThirdPartyLoginRequest request) {
        Boolean b = true;
        // 解除微博授权
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setUserId(request.getUserId());
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        ThirdPartyLoginOutModel outMode = thirdPartyLoginRMapper.getBindInfo(thirdPartyLoginInModel);
        WbAuthInfo wbAuthInfo = new WbAuthInfo();
        wbAuthInfo.setAccess_token(outMode.getAccessToken());
        if (request.getOpenType() == 0) {
            b = WeiBoUtil.unbindAccount(wbAuthInfo).equals("true");
        }
        // 清除绑定数据
        ThirdPartyLoginInModel inModel = new ThirdPartyLoginInModel();
        inModel.setOpenType(request.getOpenType());
        inModel.setUserId(request.getUserId());
        b = thirdPartyLoginWMapper.unbindAccount(inModel) > 0;
        return b;
    }

    @Override
    public List<ThirdPartyLoginOutModel> checkThirdStatus(ThirdPartyLoginRequest request) {
        return thirdPartyLoginRMapper.checkBindStatus(request.getUserId());
    }

    @Override
    public ThirdPartyLoginOutModel checkBindStatus(ThirdPartyLoginRequest request) {
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        thirdPartyLoginInModel.setUserId(request.getUserId());
        return thirdPartyLoginRMapper.getBindInfo(thirdPartyLoginInModel);
    }

    @Override
    public ThirdPartyLoginOutModel checkBindStatusByOpenId(ThirdPartyLoginRequest request) {
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setOpenId(request.getOpenId());
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        thirdPartyLoginInModel.setUserId(request.getUserId());
        return thirdPartyLoginRMapper.checkBindStatusByOpenId(thirdPartyLoginInModel);
    }

    @Override
    public ThirdPartyLoginInModel getAuthFromRedis(ThirdPartyLoginInModel thirdPartyLoginInModel,
            UserkgRequest request) {
        // 微博
        if (0 == request.getOpenType()) {
            String wbInfo = jedisUtils.get(JedisKey.wbInfoKey(request.getOpenId() + ""));
            if (StringUtils.isNotEmpty(wbInfo)) {
                WbAuthInfo wb = JsonUtil.readJson(wbInfo, WbAuthInfo.class);
                WBUserInfo wBUserInfo = WeiBoUtil.getWbUserInfo(wb);
                if (wBUserInfo == null || StringUtils.isEmpty(wBUserInfo.getScreen_name().trim())) {
                    request.setUserName(request.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                } else {
                    request.setUserName(wBUserInfo.getScreen_name());
                    request.setAvatar(wBUserInfo.getProfile_image_url());
                }
                if (wb != null) {
                    thirdPartyLoginInModel.setAccessToken(wb.getAccess_token());
                    thirdPartyLoginInModel.setOpenId(wb.getUid());
                    thirdPartyLoginInModel.setNickName(wBUserInfo.getScreen_name());
                }
            }
        }
        // 微信
        else if (1 == request.getOpenType()) {
            String wxInfo = jedisUtils.get(JedisKey.wxInfoKey(request.getOpenId() + ""));
            if (StringUtils.isNotEmpty(wxInfo)) {
                OAuthInfo oa = JsonUtil.readJson(wxInfo, OAuthInfo.class);
                WxUserInfo wxUserInfo = WeixinUtil.getWxUser(oa);
                if (wxUserInfo == null || StringUtils.isEmpty(wxUserInfo.getNickName().trim())) {
                    request.setUserName(request.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                } else {
                    request.setUserName(wxUserInfo.getNickName());
                    request.setAvatar(wxUserInfo.getHeadImgurl());
                }
                if (oa != null) {
                    thirdPartyLoginInModel.setAccessToken(oa.getAccessToken());
                    thirdPartyLoginInModel.setOpenId(oa.getOpenId());
                    thirdPartyLoginInModel.setNickName(wxUserInfo.getNickName());
                    thirdPartyLoginInModel.setRefreshAccessToken(oa.getRefreshToken());
                    // 设置微信过期时间
                    Calendar c = Calendar.getInstance();
                    c.setTime(oa.getAccessTime());
                    Long expiresIn = oa.getExpiresIn();
                    c.add(Calendar.SECOND, expiresIn.intValue());
                    thirdPartyLoginInModel.setExpireTime(c.getTime());
                }
            }

        }
        return thirdPartyLoginInModel;
    }

    @Override
    public boolean checkAddUser(UserkgRequest request) {
        // logger.info("用户注册：UserkgRequest={}", JSON.toJSONString(request));
        UserInModel model = new UserInModel();

        if (validateEmail(request) != false && validateMobile(request) != false) {
            if (null == request.getMobileArea() || request.getMobileArea().equals("86")) {
                if (request.getUserMobile().startsWith("170") || request.getUserMobile().startsWith("171")) {
                    throw new ApiMessageException("手机号格式错误，请重新输入");
                }
            }
            model.setUserId(request.getUserId());
            model.setUserName(request.getUserName());
            model.setUserMobile(request.getUserMobile());
            model.setUserEmail(request.getUserEmail());
            model.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));

            model.setMobileArea(request.getMobileArea());
            model.setUserRole(UserRoleEnum.NORMAL.getRole());
            model.setUserLevel(UserLevelEnum.PRIMARY.getLevel());

            model.setAuditStatus(AuditStatusEnum.PASS.getStatus());
            model.setLockStatus(LockStatusEnum.UNLOCK.getStatus());
            model.setThirdPartyId(request.getThirdPartyId());
            model.setCreateDate(request.getCreateDate());
            model.setRegisterIp(request.getRegisterIp());
            // model.setEmailAuthed(request.getEmailAuthed());
            model.setMobileAuthed(request.getMobileAuthed());
            // model.setUserOrder(request.getUserOrder());
            // model.setHotUser(request.getHotUser());
            // model.setApplyRole(request.getApply_role());

            model.setInviteCode(getInviteCode()); // 生成自己的邀请码

            model.setUserSource(request.getUserSource());

            userWMapper.insertSelective(model);

            if (StringUtils.isNotBlank(request.getInviteCode()) && checkInviteCode(request.getInviteCode())) { // 如果是通过邀请码注册，建立关联关系
                UserkgOutModel kgUser = userRMapper.selectByInviteCode(request.getInviteCode());
                if (!Check.NuNObject(kgUser)) {
                    UserRelation userRel = new UserRelation();
                    userRel.setRelId(idGenerater.nextId());
                    userRel.setUserId(Long.parseLong(kgUser.getUserId()));
                    userRel.setRelUserId(request.getUserId());
                    if (kgUser.getBonusStatus().intValue() == 1) {
                        userRel.setRelType(BonusRelTypeEnum.INVITE.getType());
                    } else {
                        userRel.setRelType(BonusRelTypeEnum.BINDING.getType());
                    }
                    userRelationWMapper.insertSelective(userRel);
                }
            }

            AccountInModel accountIn = new AccountInModel();
            accountIn.setUserId(request.getUserId());
            accountIn.setBalance(new BigDecimal(0.000));
            userAccountService.init(accountIn);

            return true;
        }

        return false;
    }

    @Override
    public boolean validateEmail(UserkgRequest request) {
        if (request.getUserMobile() == null) {
            if ("".equals(request.getUserEmail()))
                return true;

            if (request.getUserEmail() == null)
                return false;

            int emailFlag = request.getUserEmail().indexOf('@');
            int pointFlag = request.getUserEmail().lastIndexOf('.');

            if (emailFlag == -1 || pointFlag == -1 || request.getUserEmail().length() < 7)
                return false;

            String subName = request.getUserEmail().substring(pointFlag, request.getUserEmail().length());

            if (!subName.equals(".com"))
                return false;
            if (pointFlag - emailFlag > 1)
                return true;

        }
        return true;
    }

    @Override
    public boolean validateMobile(UserkgRequest request) {
        if (request.getUserMobile() == null) {
            return true;
        } else {
            String regex = "[\\(\\)\\d+-]{7,20}";
            return request.getUserMobile().matches(regex);
        }
    }

    @Override
    public boolean checkInviteCode(String inviteCode) {
        if (StringUtils.isBlank(inviteCode)) {
            return false;
        }
        UserInModel model = new UserInModel();
        model.setInviteCode(inviteCode);
        int count = userRMapper.checkInviteCode(model);
        return count > 0;
    }

    @Override
    public String getInviteCode() {
        String myCode = RandomUtils.getRandomString(6);
        while (checkInviteCode(myCode)) {
            myCode = RandomUtils.getRandomString(6);
        }
        return myCode;
    }

    @Override
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
        Result<UserkgResponse> userResult = getUserDetails(ur);
        CheckUtils.checkRetInfo(userResult, true);

        logger.info("===kguser{}", JSON.toJSONString(userResult));

        UserkgResponse ukr = userResult.getData();
        checkLockStatus(ukr);

        TokenModel tokenModel = tokenManager.createToken(Long.parseLong(uskgoutmodel.getUserId()));
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
        // response.setRefuseReason(uskgoutmodel.getRefuseReason());// 实名不通过原因
        // response.setCertificateStatus(uskgoutmodel.getCertificateStatus());//
        // 实名认证状态

        UserActiveInModel uAinModel = new UserActiveInModel();
        uAinModel.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        uAinModel.setLastloginIp(HttpUtil.getIpAddr(req));
        uAinModel.setLastloginTime(new Date());
        userActiveWMapper.updateUserSelective(uAinModel);

        Integer loginBonusStatus = (accountService.loginBonus(tokenModel.getUserId(),
                LoginTypeEnum.MOBILE_LOGIN.getStatus()) == false) ? 1 : 0;

        response.setLoginBonusStatus(loginBonusStatus);

        //记录登录日志
        userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req.getIntHeader("os_version"), response.getUserMobile());
        return new Result<UserkgResponse>(response);
    }
}
