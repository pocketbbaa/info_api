package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.jaq.model.v20161123.AfsCheckRequest;
import com.aliyuncs.jaq.model.v20161123.AfsCheckResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.RandomUtils;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.enumeration.RegisterOriginEnum;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserConcernListResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.EmailService;
import com.kg.platform.service.UserAccountService;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.app.UserConcernService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("userkg")
public class UserkgController extends ApiBaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserkgController.class);

    private static final Integer REGIST_ORIGIN = 3; //千氪Web注册来源

    @Inject
    private UserkgService userkgService;

    @Inject
    private IDGen idGenerater;

    @Inject
    private EmailService emailService;

    @Inject
    private JedisUtils jedisUtils;

    @Inject
    private SendSms sendSms;

    @Inject
    private UserAccountService userAccountService;

    @Autowired
    private UserConcernService userConcernService;

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkLogin")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity checkLogin(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        if (StringUtils.isBlank(request.getUserMobile())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        String remoteIp = HttpUtil.getIpAddr(req);

        Result<UserkgResponse> userkgResponse = userkgService.checkLogin(request, req);
        if (userkgResponse.getData() != null) {
            // logger.info("用户登录：" + JSON.toJSONString(request) + "成功");
            userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));

            this.jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
            this.jedisUtils.del(JedisKey.loginIp(remoteIp));

            return JsonEntity.makeSuccessJsonEntity(userkgResponse);
        } else {

            int loginTimes = userkgService.checkLoginLimit(remoteIp, request);

            logger.info("用户登录：" + JSON.toJSONString(request) + "失败");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                    ExceptionEnum.USER_LONGFAILURE.getMessage() + "，还有" + (5 - loginTimes) + "次机会，连续输错超过5次，您的账号将被暂时锁定");
        }

    }

    @ResponseBody
    @RequestMapping("mobileLogin")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity mobileLogin(@RequestAttribute UserkgRequest request, HttpServletRequest req) {

        if (null != request.getCode() && null != request.getUserMobile()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        }

        UserInModel model = new UserInModel();
        model.setUserMobile(request.getUserMobile());

        Integer newUser = 0;
        Result<Integer> userOut = userkgService.checkuserMobile(request);
        if (null == userOut || userOut.getData().intValue() == 0) {
            request.setUserId(idGenerater.nextId());
            request.setRegisterIp(HttpUtil.getIpAddr(req));
            request.setMobileAuthed(true);
            request.setCreateDate(new Date());
            // request.setUserPassword("000000");
            // request.setUserPassword(RandomUtils.genRandomPassword());
            request.setUserName(request.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            request.setRegisterOrigin(RegisterOriginEnum.WEB.getOrigin());
            userkgService.checkAddUser(request);
            newUser = 1;
        }

        Result<UserkgResponse> userkgResponse = userkgService.mobileLogin(request, req);
        if (userkgResponse.getData() != null) {
            userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));

            jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
            // if (null == userOut || userOut.getData().intValue() == 0) {
            // sendSms.send(request.getUserMobile(),
            // "【千氪财经】欢迎来到千氪财经，您的初始密码是000000，请尽快登录千氪，修改密码！以免账户受到损失！");
            // }
            userkgResponse.getData().setNewUser(newUser);
            return JsonEntity.makeSuccessJsonEntity(userkgResponse);
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                    ExceptionEnum.USER_LONGFAILURE.getMessage());
        }

    }


    /**
     * 用户注册
     *
     * @param request
     * @param requestse
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("adduser")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity Adduserkg(@RequestAttribute UserkgRequest request, HttpServletRequest requestse)
            throws Exception {
        boolean success;
        /**
         * 用户注册手机和邮箱二选其一 if判断邮箱或手机是否已经注册
         */
        if (null != request.getUserMobile()) {
            if (userkgService.checkuserMobile(request).getData() > 0) {

                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_USED.getCode(),
                        ExceptionEnum.MOBILE_USED.getMessage());
            }
        } else if (null != request.getUserEmail()) {
            if (userkgService.checkuserEmail(request).getData() > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.EMAIL_USED.getCode(),
                        ExceptionEnum.EMAIL_USED.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        if (null == request.getUserName()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        // 验证码验证
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        if ("adduser".equals(request.getCall_method())) {
            if (userkgService.validatePwd(request) != false) {
                if (userkgService.validateconfirmPwd(request) != false) {
                    request.setUserId(idGenerater.nextId());
                    request.setRegisterIp(HttpUtil.getIpAddr(requestse));
                    request.setMobileAuthed(true);
                    request.setCreateDate(new Date());
                    request.setRegisterOrigin(RegisterOriginEnum.WEB.getOrigin());
                    success = userkgService.checkAddUser(request);
                    if (!success) {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                                ExceptionEnum.USER_ADDFAILURE.getMessage());
                    } else {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                                ExceptionEnum.SUCCESS.getMessage());
                    }
                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_CONFIRM.getCode(),
                            ExceptionEnum.PASSWORD_CONFIRM.getMessage());
                }
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_LENGTH_ERROR.getCode(),
                        ExceptionEnum.PASSWORD_LENGTH_ERROR.getMessage());
            }

        }
        return JsonEntity.makeExceptionJsonEntity("", "方法有误");

    }

    /**
     * 手机号码验证
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkuserMobile")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity checkuserMobile(@RequestAttribute UserkgRequest request) {
        if (userkgService.validateMobile(request) == false) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_FORMAT.getCode(),
                    ExceptionEnum.MOBILE_FORMAT.getMessage());
        } else {
            Result<Integer> count = userkgService.checkuserMobile(request);
            if (count.getData() > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_USED.getCode(),
                        ExceptionEnum.MOBILE_USED.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            }
        }

    }

    /**
     * 邮箱验证
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkuserEmail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity checkuserEmail(@RequestAttribute UserkgRequest request) {

        if (userkgService.validateEmail(request) == false) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.EMAIL_FORMAT.getCode(),
                    ExceptionEnum.EMAIL_FORMAT.getMessage());
        } else {
            Result<Integer> count = userkgService.checkuserEmail(request);
            if (count.getData() > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.EMAIL_USED.getCode(),
                        ExceptionEnum.EMAIL_USED.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            }

        }

    }

    /**
     * 密码验证
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkuserPwd")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity checkuserPwd(@RequestAttribute UserkgRequest request) {
        if (userkgService.validatePwd(request) == false) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_FORMAT.getCode(),
                    ExceptionEnum.PASSWORD_FORMAT.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }

    }

    /**
     * 二次密码验证
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkconfirmPwdPwd")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity checkuserconfirmPwd(@RequestAttribute UserkgRequest request) {
        if (userkgService.validateconfirmPwd(request) == false) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_CONFIRM.getCode(),
                    ExceptionEnum.PASSWORD_CONFIRM.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }

    }

    /*
     * 发送验证码   (通用)
     */
    @ResponseBody
    @RequestMapping("sendSmsEmailcode")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity sendSmsEmailcode(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        try {
            String regionId = propertyLoader.getProperty("platform", "ali.regionId");
            String accessKeyId = propertyLoader.getProperty("platform", "ali.accessKeyId");
            String secret = propertyLoader.getProperty("platform", "ali.secret");

            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient(profile);

            DefaultProfile.addEndpoint(regionId, regionId, "Jaq", "jaq.aliyuncs.com");

            AfsCheckResponse response = client.getAcsResponse(request);
            if (response.getErrorCode() == 0 && response.getData() == true) {
                logger.info("阿里云验证----验签通过");

                String remoteIp = HttpUtil.getIpAddr(req);
                if (!userkgService.checkRegisterLimit(remoteIp)) {
                    return JsonEntity.makeExceptionJsonEntity("", "您的请求太频繁，请稍后重试");
                }

                if ("".equals(request.getVerIfy()) && null == request.getVerIfy()) {
                    return JsonEntity.makeExceptionJsonEntity("", "请输入要发送验证码的手机号码！");
                }

                // 判断是否重复注册
                if ("1".equals(request.getValiDation())) {
                    UserkgRequest userkgRequest = new UserkgRequest();
                    userkgRequest.setUserMobile(request.getVerIfy());
                    if (userkgService.checkuserMobile(userkgRequest).getData() > 0) {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_USED.getCode(),
                                ExceptionEnum.MOBILE_USED.getMessage());
                    }
                }

                String code = RandomUtils.generationCode();
                Result<String> vatcode = userkgService.getSendSmsEmailcode(code, request.getVerIfy());
                if (request.getVerIfy() == null) {
                    return JsonEntity.makeExceptionJsonEntity("", "手机号不存在");
                }
                request.setUserMobile(request.getVerIfy());

                String msg = StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######", vatcode.getData());

                if ("86".equals(request.getMobileArea())) {
                    boolean isPhone = userkgService.checkMobile(request);
                    if (!isPhone) {
                        return JsonEntity.makeExceptionJsonEntity("", "手机号不存在");
                    }
                    logger.info("发送短信=======" + msg);
                    sendSms.send(request.getUserMobile(), msg);
                } else {
                    logger.info("发送国际短信=======" + msg);
                    sendSms.sendInternationalMessage(request.getUserMobile(), msg);
                }

                return JsonEntity.makeExceptionJsonEntity("10000", "短信已发出");


            } else {
                logger.info("阿里云验证----验签失败");

                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SLIDE_VALID_ERROR);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++阿里云验证异常 ===" + ex.getMessage());

            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SLIDE_VALID_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping("sendSmsEmailcodeMLogin")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity sendSmsEmailcodeMLogin(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        try {

            String remoteIp = HttpUtil.getIpAddr(req);
            if (!userkgService.checkRegisterLimit(remoteIp)) {
                return JsonEntity.makeExceptionJsonEntity("", "您的请求太频繁，请稍后重试");
            }

            if ("".equals(request.getVerIfy()) && null == request.getVerIfy()) {
                return JsonEntity.makeExceptionJsonEntity("", "请输入要发送验证码的手机号码！");
            }

            String code = RandomUtils.generationCode();
            Result<String> vatcode = userkgService.getSendSmsEmailcode(code, request.getVerIfy());
            if (request.getVerIfy() == null) {
                return JsonEntity.makeExceptionJsonEntity("", "手机号不存在");
            }
            request.setUserMobile(request.getVerIfy());

            String msg = StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######", vatcode.getData());

            boolean isPhone = userkgService.checkMobile(request);
            if (!isPhone) {
                return JsonEntity.makeExceptionJsonEntity("", "手机号不存在");
            }
            logger.info("发送短信=======" + msg);
            sendSms.send(request.getUserMobile(), msg);

            return JsonEntity.makeExceptionJsonEntity("10000", "短信已发出");

        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SLIDE_VALID_ERROR);
        }

    }

    /**
     * 验证验证码和手机或者邮箱是否匹配
     */
    @ResponseBody
    @RequestMapping("chckSmsEmailCode")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity chckSmsEmailCode(@RequestAttribute UserkgRequest request) {

        if (null == request.getUserMobile() || "".equals(request.getUserMobile())) {
            request.setVerIfy(request.getUserEmail());
        } else {
            request.setVerIfy(request.getUserMobile());
        }

        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
        if (checkcode.getData() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        } else if (!checkcode.getData().equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
        }

        jedisUtils.del(JedisKey.vatcodeKey(request.getVerIfy()));

        return JsonEntity.makeSuccessJsonEntity("验证成功");

    }

    /**
     * 登录页忘记密码，验证短信和修改密码 (全局)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("chckCodePwd")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity chckCode(@RequestAttribute UserkgRequest request) {

        if (request.getCode() == null && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        } // 验证码验证
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
            }
        }
        request.setUserPassword(request.getConfirmPassword());
        if (!userkgService.validatePwd(request)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_LENGTH_ERROR.getCode(), ExceptionEnum.PASSWORD_LENGTH_ERROR.getMessage());
        }
        if (!userkgService.validateconfirmPwd(request)) {
            return JsonEntity.makeExceptionJsonEntity("", "确认密码和密码不匹配");
        }
        if (request.getVerIfy() != null) {
            boolean isPhone = userkgService.checkMobile(request);
            if (isPhone) {
                request.setUserMobile(request.getVerIfy());
            }
            boolean isEmail = userkgService.checkEmail(request);
            if (isEmail) {
                request.setUserEmail(request.getVerIfy());
            }
        }

        boolean success = userkgService.updatePssword(request);

        jedisUtils.del(JedisKey.vatcodeKey(request.getVerIfy()));

        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {

            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }

    }

    /**
     * 站外用户个人中心修改密码
     *
     * @param requestse
     * @return
     */
    @ResponseBody
    @RequestMapping("updatePwdFromOuter")
    @BaseControllerNote(needCheckParameter = true, needCheckToken = true, beanClazz = UserkgRequest.class)
    public JsonEntity updatePwdFromOuter(@RequestAttribute UserkgRequest request) {
        if (request.getCode() == null && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            }
        }
        request.setUserPassword(request.getConfirmPassword());
        if (!userkgService.validatePwd(request)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_LENGTH_ERROR.getCode(), ExceptionEnum.PASSWORD_LENGTH_ERROR.getMessage());
        }
        if (!userkgService.validateconfirmPwd(request)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAM_VALID_ERROR.getCode(),
                    ExceptionEnum.PARAM_VALID_ERROR.getMessage());
        }
        request.setUserMobile(request.getVerIfy());
        boolean success = userkgService.updatePssword(request);
        jedisUtils.del(JedisKey.vatcodeKey(request.getVerIfy()));
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    /**
     * 注销登录
     *
     * @param requestse
     * @return
     */
    @ResponseBody
    @RequestMapping("logOut")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity logOut(@RequestAttribute UserkgRequest request) {
        userkgService.logOut(request.getUserId().toString());
        return JsonEntity.makeSuccessJsonEntity(request.getUserId());
    }

    /**
     * 个人中心修改密码
     *
     * @param requestse
     * @return
     */
    @ResponseBody
    @RequestMapping("centerUpdatePd")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity CenterUpdatePd(@RequestAttribute UserkgRequest request) {

        Result<UserkgResponse> response = userkgService.checklong(request);
        request.setUserPassword(request.getConfirmPassword());
        if (!userkgService.validatePwd(request)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_LENGTH_ERROR.getCode(), ExceptionEnum.PASSWORD_LENGTH_ERROR.getMessage());
        }
        if (!userkgService.validateconfirmPwd(request)) {
            return JsonEntity.makeExceptionJsonEntity("", "确认密码和密码不匹配");
        }
        if (null == response.getData()) {
            return JsonEntity.makeExceptionJsonEntity("", "密码输入有误");
        }
        request.setUserId(Long.valueOf(response.getData().getUserId()).longValue());
        boolean success = userkgService.updatePssword(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }

    }

    /**
     * 绑定手机绑定邮箱/修改邮箱修改手机
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("centerUped")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity centerUped(@RequestAttribute UserkgRequest request) {

        if (null == request.getCode() && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }

        Result<String> checkcode = null;

        if (request.getUserEmail() == null) {
            checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
        } else {
            checkcode = userkgService.checkSmsEmailcode(request.getUserEmail());
        }

        if (checkcode.getData() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        } else if (!checkcode.getData().equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
        }
        if ("newVerIfy".equals(request.getCall_method()) || "Onebinding".equals(request.getCall_method())) {
            if (request.getVerIfy() != null) {
                boolean isPhone = userkgService.checkMobile(request);
                if (isPhone == true) {
                    request.setUserMobile(request.getVerIfy());
                }
                boolean isEmail = userkgService.checkEmail(request);
                if (isEmail == true) {
                    request.setUserEmail(request.getVerIfy());
                }
            }
            boolean success = userkgService.centerUped(request);
            if (success == false) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        }

        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage());

    }

    /**
     * 个人中心账号安全信息接口
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getuserdetails")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity getUserDetails(@RequestAttribute UserkgRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        Result<UserkgResponse> response = userkgService.getUserDetails(request);
        if (response.getData() != null) {
            response.getData().setRefuseReason(response.getData().getCertRefuseReason());
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeExceptionJsonEntity("", "数据获取有误");

    }

    /**
     * 设置阅读奖励判断实名认证
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getCertificate")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity getCertificate(@RequestAttribute UserkgRequest request) {

        Result<UserkgResponse> result = userkgService.selectByUser(request);
        if (result.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(result.getData());
        }
        return JsonEntity.makeSuccessJsonEntity("已实名认证");

    }

    @ResponseBody
    @RequestMapping("afsCheck")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AfsCheckRequest.class)
    public JsonEntity afsCheck(@RequestAttribute AfsCheckRequest request) {

        try {
            String regionId = propertyLoader.getProperty("platform", "ali.regionId");
            String accessKeyId = propertyLoader.getProperty("platform", "ali.accessKeyId");
            String secret = propertyLoader.getProperty("platform", "ali.secret");

            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient(profile);

            DefaultProfile.addEndpoint(regionId, regionId, "Jaq", "jaq.aliyuncs.com");

            // // AfsCheckRequest request = new AfsCheckRequest();
            // request.setPlatform(3);// 必填参数，请求来源： 1：Android端； 2：iOS端； 3：PC端及其他
            // request.setSession("xxx");// 必填参数，从前端获取，不可更改
            // request.setSig("xxx");// 必填参数，从前端获取，不可更改
            // request.setToken("zxd");// 必填参数，从前端获取，不可更改
            // request.setScene("xxx");// 必填参数，从前端获取，不可更改

            AfsCheckResponse response = client.getAcsResponse(request);
            if (response.getErrorCode() == 0 && response.getData() == true) {
                logger.info("阿里云验证----验签通过");
            } else {
                logger.info("阿里云验证----验签失败");
            }

            return JsonEntity.makeSuccessJsonEntity(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++阿里云验证异常 ===" + ex.getMessage());
            throw new ApiMessageException("错误，请检查输入");
        }

    }

    @RequestMapping(value = "af", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public JsonEntity af(@RequestParam("session") String session, @RequestParam("sig") String sig,
                         @RequestParam("token") String token, @RequestParam("scene") String scene) {

        try {
            String regionId = propertyLoader.getProperty("platform", "ali.regionId");
            String accessKeyId = propertyLoader.getProperty("platform", "ali.accessKeyId");
            String secret = propertyLoader.getProperty("platform", "ali.secret");

            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient(profile);

            DefaultProfile.addEndpoint(regionId, regionId, "Jaq", "jaq.aliyuncs.com");

            AfsCheckRequest request = new AfsCheckRequest();
            request.setPlatform(3);// 必填参数，请求来源： 1：Android端； 2：iOS端； 3：PC端及其他
            request.setSession(session);// 必填参数，从前端获取，不可更改
            request.setSig(sig);// 必填参数，从前端获取，不可更改
            request.setToken(token);// 必填参数，从前端获取，不可更改
            request.setScene(scene);// 必填参数，从前端获取，不可更改

            AfsCheckResponse response = client.getAcsResponse(request);
            if (response.getErrorCode() == 0 && response.getData() == true) {
                logger.info("阿里云验证----验签通过");
            } else {
                logger.info("阿里云验证----验签失败");
            }

            return JsonEntity.makeSuccessJsonEntity(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++阿里云验证异常 ===" + ex.getMessage());
            throw new ApiMessageException("错误，请检查输入");
        }

    }

    /**
     * 搜索用户(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("searchAuthor")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = false, beanClazz = UserConcernRequest.class)
    public JsonEntity searchAuthor(@RequestAttribute UserConcernRequest request, PageModel<UserConcernListResponse> page) {
        PageModel<UserConcernListResponse> pageModel = userConcernService.searchAuthorList(request, page);
        String searchStr = request.getSearchStr();
        if (StringUtils.isEmpty(searchStr)) {
            return JsonEntity.makeSuccessJsonEntity(pageModel);
        }
        List<UserConcernListResponse> listResponseList = page.getData();
        if (CollectionUtils.isNotEmpty(listResponseList)) {
            listResponseList.forEach(userConcernListResponse -> {
                String userName = userConcernListResponse.getUserName();
                if (StringUtils.isNotEmpty(userName)) {
                    int start = userName.indexOf(searchStr);
                    int end = start + searchStr.length();
                    StringBuilder stringBuilder = new StringBuilder(userName);
                    stringBuilder.insert(end, "</span>");
                    stringBuilder.insert(start, "<span class=\"txt_highlight\">");
                    userConcernListResponse.setUserName(stringBuilder.toString());
                }
            });
        }
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    public static void main(String[] args) {

        String userName = "BTC123";
        String searchStr = "b";
        int start = userName.indexOf(searchStr);
        int end = start + searchStr.length();
        StringBuilder stringBuilder = new StringBuilder(userName);
        stringBuilder.insert(end, "</span>");
        stringBuilder.insert(start, "<span class=\"txt_highlight\">");

        System.out.println(stringBuilder.toString());

    }

}
