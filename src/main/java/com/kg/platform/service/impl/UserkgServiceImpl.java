package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.AccountDepositFlowRMapper;
import com.kg.platform.dao.read.UserActiveRMapper;
import com.kg.platform.dao.read.UserCertRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.ActivityInviteLogWMapper;
import com.kg.platform.dao.write.UserActiveWMapper;
import com.kg.platform.dao.write.UserRelationWMapper;
import com.kg.platform.dao.write.UserWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.*;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.ThirdPartyLoginRequest;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.*;
import com.kg.platform.service.admin.UserService;
import com.kg.platform.service.app.UserConcernService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserkgServiceImpl implements UserkgService {
    private static final Logger logger = LoggerFactory.getLogger(UserkgServiceImpl.class);

    @Autowired
    protected JedisUtils jedisUtils;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    UserActiveRMapper userActiveRMapper;

    @Inject
    UserActiveWMapper userActiveWMapper;

    @Inject
    UserCertRMapper userCertRMapper;

    @Inject
    AccountDepositFlowRMapper accountDepositFlowRMapper;

    @Inject
    private UserWMapper userWMapper;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private UserRelationWMapper userRelationWMapper;

    @Inject
    private TokenManager tokenManager;

    @Inject
    private ActivityInviteLogWMapper activityInviteLogWMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Inject
    private UserAccountService userAccountService;

    @Inject
    private IDGen idGenerater;

    @Inject
    private ThirdPartyService thirdPartyService;

    @Inject
    private UserTagsUtil userTagsUtil;

    @Inject
    private UserLogUtil userLogUtil;

    @Autowired
    private UserConcernService userConcernService;

    @Override
    public void updateInviteCode(Long userId, String inviteCode) {
        if (StringUtils.isBlank(inviteCode)) {
            return;
        }

        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(userId);
        userInModel.setInviteCode(inviteCode);
        userWMapper.updateByUserSelective(userInModel);
    }

    @Override
    public Result<UserkgResponse> checkLogin(UserkgRequest request, HttpServletRequest req) {
        logger.info("用户登录：UserkgRequest={}");

        UserInModel model = new UserInModel();
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        UserkgResponse response = new UserkgResponse();

        model.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
        UserkgOutModel outmodel = userRMapper.getUserInfo(model);

        if (outmodel != null) {

            UserkgRequest ur = new UserkgRequest();
            ur.setUserId(Long.parseLong(outmodel.getUserId()));
            Result<UserkgResponse> userResult = getUserDetails(ur);
            CheckUtils.checkRetInfo(userResult, true);

            logger.info("===kguser{}", JSON.toJSONString(userResult));

            UserkgResponse ukr = userResult.getData();
            checkLockStatus(ukr, null);

            UserkgOutModel uskgoutmodel = userRMapper.selectByPrimary(model);
            if (uskgoutmodel != null) {

                TokenModel tokenModel = tokenManager.createToken(Long.parseLong(uskgoutmodel.getUserId()));
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
                userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req.getIntHeader("os_version"),
                        response.getUserMobile());
                return new Result<UserkgResponse>(response);
            }
            return new Result<UserkgResponse>();

        }
        return new Result<UserkgResponse>();
    }

    @Override
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
        Result<UserkgResponse> userResult = getUserDetails(ur);
        CheckUtils.checkRetInfo(userResult, true);

        logger.info("===kguser{}", JSON.toJSONString(userResult));

        UserkgResponse ukr = userResult.getData();
        checkLockStatus(ukr, null);

        TokenModel tokenModel = tokenManager.createToken(Long.parseLong(uskgoutmodel.getUserId()));

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
        // response.setRefuseReason(uskgoutmodel.getRefuseReason());// 实名不通过原因
        // response.setCertificateStatus(uskgoutmodel.getCertificateStatus());//
        // 实名认证状态

        UserActiveInModel uAinModel = new UserActiveInModel();
        uAinModel.setUserId(Long.parseLong(uskgoutmodel.getUserId()));
        uAinModel.setLastloginIp(HttpUtil.getIpAddr(req));
        uAinModel.setLastloginTime(new Date());
        userActiveWMapper.updateUserSelective(uAinModel);

        AccountRequest accountReq = new AccountRequest();
        accountReq.setUserId(tokenModel.getUserId());

        Integer loginBonusStatus = (accountService.loginBonus(tokenModel.getUserId(),
                LoginTypeEnum.MOBILE_LOGIN.getStatus()) == false) ? 1 : 0;

        response.setLoginBonusStatus(loginBonusStatus);

        // 记录登录日志
        userLogUtil.recordLoginLog(Long.valueOf(response.getUserId()), req.getIntHeader("os_version"),
                response.getUserMobile());

        return new Result<UserkgResponse>(response);
    }

    @Override
    @Transactional
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
            String password = request.getUserPassword();
            if (StringUtils.isBlank(password)) {
                password = RandomUtils.genRandomPassword();
            } else {
                password = MD5Util.md5Hex(request.getUserPassword() + Constants.SALT);
            }
            model.setUserPassword(password);

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

            model.setRegisterOrigin(request.getRegisterOrigin());

            userWMapper.insertSelective(model);

            logger.info("只有APP·来的注册才会建立关联关系");
            if ((null != request.getRegisterOrigin()
                    && (request.getRegisterOrigin() == 1 || request.getRegisterOrigin() == 2 || Objects
                    .equals(RegisterOriginEnum.M.getOrigin(), request.getRegisterOrigin())))) { // 只有APP和M站·来的注册才会建立关联关系。
                if (StringUtils.isNotBlank(request.getInviteCode()) && checkInviteCode(request.getInviteCode())) { // 如果是通过邀请码注册，建立关联关系
                    UserkgOutModel kgUser = userRMapper.selectByInviteCode(request.getInviteCode());
                    if (!Check.NuNObject(kgUser)) {
                        UserRelation userRel = new UserRelation();
                        userRel.setRelId(idGenerater.nextId());
                        userRel.setUserId(Long.parseLong(kgUser.getUserId()));
                        userRel.setRelUserId(request.getUserId());
                        if (request.getActivityId() != null) {
                            userRel.setActivityId(request.getActivityId());
                        }
                        if (kgUser.getBonusStatus() == 1) {
                            userRel.setRelType(BonusRelTypeEnum.INVITE.getType());
                        } else {
                            userRel.setRelType(BonusRelTypeEnum.BINDING.getType());
                        }
                        userRelationWMapper.insertSelective(userRel);
                        beFans(kgUser, request.getUserId());
                    }
                }
            }
            logger.info("来源于活动则保存活动邀新记录");
            if (request.getActivityId() != null && StringUtils.isNotBlank(request.getInviteCode())
                    && checkInviteCode(request.getInviteCode())) {
                UserkgOutModel kgUser = userRMapper.selectByInviteCode(request.getInviteCode());
                ActivityInviteLogInModel activityInviteLogInModel = new ActivityInviteLogInModel();
                activityInviteLogInModel.setActivityId(request.getActivityId());
                activityInviteLogInModel.setUserId(Long.parseLong(kgUser.getUserId()));
                activityInviteLogInModel.setRelUserId(request.getUserId());
                activityInviteLogInModel.setActivityId(request.getActivityId());
                activityInviteLogInModel.setCreateTime(new Date());
                activityInviteLogInModel.setInviteId(idGenerater.nextId());
                activityInviteLogInModel.setStatus(0);
                activityInviteLogWMapper.insertSelective(activityInviteLogInModel);
            }
            AccountInModel accountIn = new AccountInModel();
            accountIn.setUserId(request.getUserId());
            accountIn.setBalance(new BigDecimal(0.000));
            accountIn.setType(0);
            accountIn.setUserMobile(request.getUserMobile());
            userAccountService.init(accountIn);

            return true;
        }

        return false;
    }

    /**
     * 若师傅为专栏作者，则自动关注师傅成为其粉丝
     *
     * @param kgUser 师傅
     * @param userId 用户ID
     */
    private void beFans(UserkgOutModel kgUser, Long userId) {
        logger.info("如果你的师傅是千氪专栏作者，自动关注 师傅->kgUser：" + JSONObject.toJSONString(kgUser) + "   用户->userId:" + userId);
        if (kgUser.getUserRole() == 1) {
            logger.info("你的师父不是专栏作者，不能自动成为其粉丝");
            return;
        }
        if (userConcernService.isConcerned(Long.valueOf(kgUser.getUserId()), userId)) {
            logger.info("你已经关注过，不需要再成为其粉丝");
            return;
        }
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId(String.valueOf(userId));
        UserConcernRequest request = new UserConcernRequest();
        request.setConcernUserId(kgUser.getUserId());
        userConcernService.concernUser(kguser, request);
        logger.info("自动关注成功 师傅->kgUser：" + JSONObject.toJSONString(kgUser) + "   用户->userId:" + userId);
    }

    @Override
    public Result<Integer> checkuserMobile(UserkgRequest request) {
        UserInModel model = new UserInModel(request.getUserEmail(), request.getUserMobile());
        int count = userRMapper.checkMobile(model);
        return new Result<Integer>(count);
    }

    @Override
    public Result<Integer> checkuserEmail(UserkgRequest request) {
        UserInModel model = new UserInModel(request.getUserEmail(), request.getUserMobile());
        int count = userRMapper.checkEmail(model);
        return new Result<Integer>(count);
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

    /**
     * 邮箱验证（必须符合邮箱的格式，如xxxxxxxx@*.com）
     *
     * @param
     * @return
     */
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

    /**
     * 验证手机号码
     */
    @Override
    public boolean validateMobile(UserkgRequest request) {
        if (request.getUserMobile() == null) {
            return true;
        } else {
            String regex = "[\\(\\)\\d+-]{7,20}";
            return request.getUserMobile().matches(regex);
        }

    }

    /**
     * 验证密码
     */
    @Override
    public boolean validatePwd(UserkgRequest request) {
        String regex = "^[\\x21-\\x7E]{6,20}$";
        return request.getUserPassword().matches(regex);
    }

    /**
     * 二次密码验证
     */
    public boolean validateconfirmPwd(UserkgRequest request) {
        if (null != request.getNewPwd()) {
            if (request.getNewPwd().equals(request.getConfirmPassword())) {
                return true;
            } else {
                return false;
            }

        }

        if (request.getUserPassword().equals(request.getConfirmPassword())) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean adduser(UserkgRequest request) {
        return false;
    }

    /**
     * 验证码放进redis
     */
    @Override
    public Result<String> getSendSmsEmailcode(String code, String verIfy) {
        this.jedisUtils.set(JedisKey.vatcodeKey(verIfy), code, JedisKey.THREE_MINUTE);
        String vatcode = jedisUtils.get(JedisKey.vatcodeKey(verIfy));
        return new Result<String>(vatcode);
    }

    /**
     * 将邮件key放入redis，过期时间24小时
     *
     * @param key
     * @param verIfy
     * @return
     */
    @Override
    public Result<String> getSendSmsEmailKey(String key, String verIfy) {
        this.jedisUtils.set(JedisKey.vatcodeKey(verIfy), key, JedisKey.ONE_DAY);
        String vatcode = jedisUtils.get(JedisKey.vatcodeKey(verIfy));
        return new Result<String>(vatcode);
    }

    @Override
    public Result<String> checkSmsEmailcode(String verIfy) {
        String vatcode = jedisUtils.get(JedisKey.vatcodeKey(verIfy));
        return new Result<String>(vatcode);
    }

    /**
     * 成功取出一次后失效
     *
     * @param verIfy
     * @return
     */
    @Override
    public Result<String> checkSmsEmailcodeAndDel(String verIfy) {
        String vatcode = jedisUtils.get(JedisKey.vatcodeKey(verIfy));
        if (StringUtils.isNotEmpty(vatcode)) {
            jedisUtils.del(JedisKey.vatcodeKey(verIfy));
        }
        return new Result<String>(vatcode);
    }

    /**
     * 验证是否是手机号码
     *
     * @param request
     * @return
     */
    public boolean checkMobile(UserkgRequest request) {
        String PHONE_PATTERN = "^\\d{11}|\\d{13}$";
        boolean isPhone = request.getVerIfy().matches(PHONE_PATTERN);
        return isPhone;
    }

    /**
     * 验证是否是邮箱
     *
     * @param request
     * @return
     */
    public boolean checkEmail(UserkgRequest request) {
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        boolean isEmail = request.getVerIfy().matches(regEx1);
        return isEmail;
    }

    public int checkTxpassLimit(String userIP) {
        if (null != this.jedisUtils.getSingle(JedisKey.limitTxPassIp(userIP))) {
            return 0;
        }

        Set<String> userKeys = jedisUtils.keys("*txPassIp*" + userIP + "*");

        logger.info("checkTxpassLimit=====" + userKeys);
        int times = 0;
        if (userKeys != null && userKeys.size() > 0) {
            times = userKeys.size() + 1;
        } else {
            times = 1;
        }

        if (times >= 5) {
            this.jedisUtils.setSingle(JedisKey.limitTxPassIp(userIP), times, JedisKey.ONE_HOUR * 3);
            return 0;
        }
        this.jedisUtils.setSingle(JedisKey.txPassIp(userIP + times), times, JedisKey.TEN_MINUTE);
        return times;
    }

    public int checkLoginLimit(String userIP, UserkgRequest request) {
        int times = 0;
        UserInModel model = new UserInModel();
        model.setUserMobile(request.getUserMobile());
        UserkgOutModel outmodel = userRMapper.getUserInfo(model);

        Set<String> userKeys = jedisUtils.keys("*loginIp*" + ((null == outmodel) ? "0" : outmodel.getUserId()) + "*");
        if (userKeys != null && userKeys.size() > 0) {
            times = userKeys.size() + 1;
        } else {
            times = 1;
        }

        logger.info("checkLoginLimit=====" + userKeys + JSON.toJSONString(outmodel));

        if (times >= 5) {
            if (null != outmodel) {
                Result<String> result = userService.lockUser(
                        StringUtils.convertString2LongList(outmodel.getUserId(), ","), Constants.ADMIN_USER_ID, 2, 5);
                CheckUtils.checkRetInfo(result);
            }
        }

        if (null != outmodel) {
            this.jedisUtils.setSingle(JedisKey.loginIp(outmodel.getUserId() + times), times, JedisKey.TEN_MINUTE);
        }

        return times > 5 ? 5 : times;
    }

    public synchronized boolean checkRegisterLimit(String userIP) {
        if (null != this.jedisUtils.getSingle(JedisKey.limitIp(userIP))) {
            return false;
        }

        Set<String> userKeys = jedisUtils.keys("*registerIp*" + userIP + "*");

        logger.info("checkRegisterLimit=====" + userKeys);
        int times = 1;
        if (userKeys != null && userKeys.size() > 0) {
            times = userKeys.size() + 1;
        }

        if (times > 500) {
            this.jedisUtils.setSingle(JedisKey.limitIp(userIP), times, JedisKey.TWO_HOURS);
            return false;
        }
        this.jedisUtils.setSingle(JedisKey.registerIp(userIP + times), times, JedisKey.ONE_HOUR);
        return true;
    }

    /**
     * 忘记密码返回用户信息
     */
    @Override
    public Result<UserkgResponse> selectUser(UserkgRequest request) {
        logger.info("忘记密码：UserkgRequest={}", JSON.toJSONString(request));
        UserInModel inModel = new UserInModel();
        inModel.setUserEmail(request.getUserEmail());
        inModel.setUserMobile(request.getUserMobile());
        UserkgOutModel outModel = userRMapper.selectUser(inModel);
        UserkgResponse response = new UserkgResponse();
        response.setUserId(outModel.getUserId().toString());
        return new Result<UserkgResponse>(response);
    }

    /**
     * 修改密码
     */
    @Override
    public boolean updatePssword(UserkgRequest request) {
        // logger.info("修改密码：UserkgRequest={}", JSON.toJSONString(request));
        UserInModel inModel = new UserInModel();
        inModel.setUserId(request.getUserId());
        inModel.setUserEmail(request.getUserEmail());
        inModel.setUserMobile(request.getUserMobile());
        // 判断是否是个人中心修改密码
        if (null != request.getNewPwd()) {
            inModel.setUserPassword(MD5Util.md5Hex(request.getNewPwd() + Constants.SALT));
        } else {
            inModel.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
        }

        return userWMapper.updatePssword(inModel) > 0;
    }

    @Override
    public void logOut(String userId) {
        tokenManager.deleteToken(Long.parseLong(userId));
    }

    public Result<UserkgResponse> checklong(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        model.setUserId(request.getUserId());
        model.setUserPassword(MD5Util.md5Hex(request.getUserPassword() + Constants.SALT));
        UserkgOutModel uskgoutmodel = userRMapper.selectByPrimary(model);
        UserkgResponse response = new UserkgResponse();
        if (null != uskgoutmodel) {
            response.setUserId(uskgoutmodel.getUserId().toString());
            response.setUserName(uskgoutmodel.getUserName());
            response.setUserEmail(uskgoutmodel.getUserEmail());
            response.setUserMobile(uskgoutmodel.getUserMobile());
            return new Result<UserkgResponse>(response);
        }

        return new Result<UserkgResponse>();
    }

    /**
     * 绑定手机和邮箱或修改
     */
    @Override
    public boolean centerUped(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setUserId(request.getUserId());
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        return userWMapper.centerUped(model) > 0;
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
            response.setUserId(inModel.getUserId().toString());
            response.setUserEmail(model.getUserEmail());
            response.setUserMobile(model.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            response.setMobIle(model.getUserMobile());
            response.setApplyRole(model.getApplyRole());
            response.setInviteCode(model.getInviteCode());
            response.setBonusStatus(model.getBonusStatus());
            response.setBonusFreezeReason(model.getBonusFreezeReason());
            response.setCreateDate(model.getCreateDate());
            response.setInviteStatus(model.getInviteStatus());
            response.setUserName(model.getUserName());
            response.setAccountFrom(1);
            if (model.getUserPassword().length() < 25) {
                response.setAccountFrom(0);
            }
            if (userCertOutModel == null) {
                response.setRealnameAuthed(-1);// 未实名
            } else {
                response.setRealnameAuthed(userCertOutModel.getStatus());
                String realName = userCertOutModel.getRealName();
                if (StringUtils.isNotEmpty(realName)) {
                    response.setRealName(realName.substring(0, 1) + com.kg.platform.common.utils.StringUtils.getChar(realName.length()));
                }
                String idcard = userCertOutModel.getIdcardNo();
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(idcard)) {
                    response.setIdcardNo(idcard.replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****************$2"));
                }
                response.setIdcardFront(userCertOutModel.getIdcardFront());
                response.setCertRefuseReason(userCertOutModel.getCertRefuseReason());
            }
            // 查询是否绑定微博微信
            ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
            thirdPartyLoginRequest.setUserId(Long.valueOf(response.getUserId()));
            List<ThirdPartyLoginOutModel> tps = thirdPartyService.checkThirdStatus(thirdPartyLoginRequest);
            response.setBindWeibo(0);
            response.setBindWeiXin(0);
            if (tps.size() > 0) {
                for (ThirdPartyLoginOutModel tp : tps) {
                    if (tp.getOpenType() == 0) {
                        response.setBindWeibo(1);
                        response.setWbName(tp.getNickName());
                    } else if (tp.getOpenType() == 1) {
                        response.setBindWeiXin(1);
                        response.setWxName(tp.getNickName());
                    }
                }
            }
            response.setTradepasswordSet(model.getTradepasswordSet());// 是否设置交易密码
            response.setDepositStatus(model.getDepositStatus());// 是否缴纳保证金

            response.setLockStatus(model.getLockStatus());
            response.setLockDate(model.getLockDate());
            response.setLockTime(model.getLockTime());
            response.setLockUnit(model.getLockUnit());
            response.setRefuseReason(model.getRefuseReason());
            response.setMobileArea(model.getMobileArea());
        }
        return new Result<UserkgResponse>(response);
    }

    /**
     * 我的专栏申请进度
     *
     * @param request
     * @return
     */
    @Override
    public Result<UserkgResponse> selectApply(UserkgRequest request) {
        logger.info("专栏申请进度：UserkgRequest={}", JSON.toJSONString(request));
        UserInModel inModel = new UserInModel();
        inModel.setUserId(request.getUserId());
        UserkgOutModel model = userRMapper.selectApply(inModel);
        UserkgResponse response = new UserkgResponse();
        response.setUserRole(Integer.parseInt(model.getUserRole().toString()));
        response.setAuditStatus(model.getAuditStatus());
        response.setApplyRole(model.getApplyRole());
        if (model.getApplyRole().equals("4")) {
            AccountDepositFlowIntModel adFintModel = new AccountDepositFlowIntModel();
            adFintModel.setUserId(request.getUserId());
            List<AccountDepositFlowOutModel> depositFlowOutModel = accountDepositFlowRMapper
                    .selectByUserAmount(adFintModel);
            BigDecimal depositAmount = new BigDecimal(propertyLoader.getProperty("common", "global.userDepositAmount"));
            response.setDepositStatus(model.getDepositStatus());
            response.setDepositAddress(propertyLoader.getProperty("common", "global.userDepositAddress"));
            response.setDepositAmount(depositAmount.toString());
            BigDecimal remainingAmount = BigDecimal.ZERO;

            if (depositFlowOutModel != null) {
                for (AccountDepositFlowOutModel depositFlow : depositFlowOutModel) {
                    remainingAmount = remainingAmount.add(depositFlow.getAccountAmount());
                }
            }
            depositAmount = depositAmount.subtract(remainingAmount);
            response.setRemainingAmount(NumberUtils.formatBigDecimal(depositAmount));
        }

        if (model.getAuditStatus() != 1) {
            UserActiveInModel activeInModel = new UserActiveInModel();
            activeInModel.setUserId(inModel.getUserId());
            UserActiveOutModel outModel = userActiveRMapper.selectByUserKey(activeInModel);
            response.setApplyColumnTime(DateUtils.formatYMDHM(outModel.getApplyColumnTime()));
            response.setRefuseReason(outModel.getRefuseReason());

        }
        return new Result<UserkgResponse>(response);
    }

    @Override
    public void checkLockStatus(UserkgResponse ukr, Long userId) {

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

                /*String errorStr = "您的账号因违规操作，已被系统锁定 " + lockTime + lockUnitStr
                        + " ，这期间您可以正常浏览并转发文章，但无法进行其他操作。如有疑问，请咨询客服：" + customerServicePhone;*/
                String errorStr = "您的账号已被锁定" + lockTime + lockUnitStr;

                //如果是APP的token 需要将token清掉 强制APP和WEB登出
                if (userId != null) {
                    tokenManager.deleteToken(userId);
                    tokenManager.deleteAppToken(userId);
                }

                throw new ApiMessageException(ExceptionEnum.LOCKERROR.getCode(), errorStr);
            } else {
                Result<String> result = userService.lockUser(StringUtils.convertString2LongList(ukr.getUserId(), ","),
                        Constants.ADMIN_USER_ID, null, null);
                CheckUtils.checkRetInfo(result);

            }

        }
    }

    @Override
    public Result<UserkgResponse> selectByUser(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setUserEmail(request.getUserEmail());
        model.setUserMobile(request.getUserMobile());
        model.setUserPassword(request.getUserPassword());
        model.setUserId(request.getUserId());
        UserkgOutModel uskgoutmodel = userRMapper.selectByUser(model);
        UserkgResponse response = new UserkgResponse();
        if (uskgoutmodel != null) {

            response.setUserId(uskgoutmodel.getUserId().toString());
            response.setUserName(uskgoutmodel.getUserName());
            response.setUserEmail(uskgoutmodel.getUserEmail());
            response.setUserMobile(uskgoutmodel.getUserMobile());
            response.setUserRole(Integer.parseInt(uskgoutmodel.getUserRole().toString()));
            response.setArticlesum(uskgoutmodel.getArticlesum());
            response.setRealnameAuthed(uskgoutmodel.getRealnameAuthed());
            response.setTradepasswordSet(uskgoutmodel.getTradepasswordSet());
            response.setAtskMobile(uskgoutmodel.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            response.setMobileArea(uskgoutmodel.getMobileArea());
            response.setRefuseReason(uskgoutmodel.getRefuseReason());// 实名不通过原因
            response.setCertificateStatus(uskgoutmodel.getCertificateStatus());// 实名认证状态

        }
        return new Result<UserkgResponse>(response);
    }

    @Override
    public boolean checkThirdPartyPhone(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setUserMobile(request.getUserMobile());
        model.setUserSource(request.getSourceType());
        return userRMapper.checkThirdPartyPhone(model) > 0;
    }

    @Override
    public UserkgOutModel getUserPhoneByOpenId(UserkgRequest request) {
        UserInModel userInModel = new UserInModel();
        userInModel.setOpenId(request.getOpenId() + "");
        userInModel.setUserMobile(request.getUserMobile());
        return userRMapper.getUserInfoByOpenId(userInModel);
    }

    @Override
    public boolean checkUserIsLock(UserInModel userInModel) {
        return userRMapper.checkUserIsLock(userInModel) > 0;
    }

    @Override
    public UserkgOutModel getUserInfo(UserkgRequest request) {
        UserInModel userInModel = new UserInModel();
        userInModel.setUserEmail(request.getUserEmail());
        userInModel.setUserMobile(request.getUserMobile());
        return userRMapper.getUserInfo(userInModel);
    }

    @Override
    public UserkgOutModel getUserProfiles(UserkgRequest request) {
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(request.getUserId());
        userInModel.setUserMobile(request.getUserMobile());
        logger.info("入参123" + JsonUtil.writeValueAsString(userInModel));
        return userRMapper.getUserProfiles(userInModel);
    }

    @Override
    @Transactional
    public String invite(UserkgRequest request) {
        UserInModel model = new UserInModel();
        model.setCreateDate(new Date());
        //随机生成密码
        model.setUserPassword(MD5Util.md5Hex(RandomUtils.genRandomPassword() + Constants.SALT));
        model.setUserMobile(request.getUserMobile());
        model.setRegisterOrigin(1);
        model.setUserSource(StringUtils.encryptionString(request.getUserMobile(), 4, 7));
        model.setUserId(request.getUserId());
        model.setMobileAuthed(request.getMobileAuthed());
        model.setRegisterOrigin(request.getRegisterOrigin());
        model.setRegisterIp(request.getRegisterIp());
        model.setUserSource(request.getUserSource());
        model.setUserLevel(UserLevelEnum.PRIMARY.getLevel());
        model.setUserRole(UserRoleEnum.NORMAL.getRole());
        model.setAuditStatus(AuditStatusEnum.PASS.getStatus());
        model.setLockStatus(LockStatusEnum.UNLOCK.getStatus());
        model.setInviteCode(getInviteCode()); // 生成自己的邀请码
        model.setRegisterOrigin(request.getRegisterOrigin());
        model.setMobileArea(request.getMobileArea());
        model.setUserName(StringUtils.encryptionString(request.getUserMobile(), 4, 7));
        AccountInModel accountIn = new AccountInModel();
        accountIn.setUserId(model.getUserId());
        accountIn.setType(0);
        accountIn.setBalance(new BigDecimal(0.000));
        accountIn.setUserMobile(request.getUserMobile());
        //添加用户
        userWMapper.insertSelective(model);
        //初始化用户资产表
        userAccountService.init(accountIn);
        return model.getInviteCode();
    }
}
