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

    private static final Integer REGIST_ORIGIN = 3; //??????Web????????????

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
     * ??????
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
            // logger.info("???????????????" + JSON.toJSONString(request) + "??????");
            userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));

            this.jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
            this.jedisUtils.del(JedisKey.loginIp(remoteIp));

            return JsonEntity.makeSuccessJsonEntity(userkgResponse);
        } else {

            int loginTimes = userkgService.checkLoginLimit(remoteIp, request);

            logger.info("???????????????" + JSON.toJSONString(request) + "??????");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                    ExceptionEnum.USER_LONGFAILURE.getMessage() + "?????????" + (5 - loginTimes) + "??????????????????????????????5????????????????????????????????????");
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
                return JsonEntity.makeExceptionJsonEntity("", "???????????????");
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
            // "??????????????????????????????????????????????????????????????????000000?????????????????????????????????????????????????????????????????????");
            // }
            userkgResponse.getData().setNewUser(newUser);
            return JsonEntity.makeSuccessJsonEntity(userkgResponse);
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                    ExceptionEnum.USER_LONGFAILURE.getMessage());
        }

    }


    /**
     * ????????????
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
         * ??????????????????????????????????????? if???????????????????????????????????????
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

        // ???????????????
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "???????????????");
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
        return JsonEntity.makeExceptionJsonEntity("", "????????????");

    }

    /**
     * ??????????????????
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
     * ????????????
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
     * ????????????
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
     * ??????????????????
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
     * ???????????????   (??????)
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
                logger.info("???????????????----????????????");

                String remoteIp = HttpUtil.getIpAddr(req);
                if (!userkgService.checkRegisterLimit(remoteIp)) {
                    return JsonEntity.makeExceptionJsonEntity("", "???????????????????????????????????????");
                }

                if ("".equals(request.getVerIfy()) && null == request.getVerIfy()) {
                    return JsonEntity.makeExceptionJsonEntity("", "?????????????????????????????????????????????");
                }

                // ????????????????????????
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
                    return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
                }
                request.setUserMobile(request.getVerIfy());

                String msg = StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######", vatcode.getData());

                if ("86".equals(request.getMobileArea())) {
                    boolean isPhone = userkgService.checkMobile(request);
                    if (!isPhone) {
                        return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
                    }
                    logger.info("????????????=======" + msg);
                    sendSms.send(request.getUserMobile(), msg);
                } else {
                    logger.info("??????????????????=======" + msg);
                    sendSms.sendInternationalMessage(request.getUserMobile(), msg);
                }

                return JsonEntity.makeExceptionJsonEntity("10000", "???????????????");


            } else {
                logger.info("???????????????----????????????");

                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SLIDE_VALID_ERROR);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++????????????????????? ===" + ex.getMessage());

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
                return JsonEntity.makeExceptionJsonEntity("", "???????????????????????????????????????");
            }

            if ("".equals(request.getVerIfy()) && null == request.getVerIfy()) {
                return JsonEntity.makeExceptionJsonEntity("", "?????????????????????????????????????????????");
            }

            String code = RandomUtils.generationCode();
            Result<String> vatcode = userkgService.getSendSmsEmailcode(code, request.getVerIfy());
            if (request.getVerIfy() == null) {
                return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
            }
            request.setUserMobile(request.getVerIfy());

            String msg = StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######", vatcode.getData());

            boolean isPhone = userkgService.checkMobile(request);
            if (!isPhone) {
                return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
            }
            logger.info("????????????=======" + msg);
            sendSms.send(request.getUserMobile(), msg);

            return JsonEntity.makeExceptionJsonEntity("10000", "???????????????");

        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SLIDE_VALID_ERROR);
        }

    }

    /**
     * ????????????????????????????????????????????????
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
            return JsonEntity.makeExceptionJsonEntity("", "???????????????");
        }

        jedisUtils.del(JedisKey.vatcodeKey(request.getVerIfy()));

        return JsonEntity.makeSuccessJsonEntity("????????????");

    }

    /**
     * ??????????????????????????????????????????????????? (??????)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("chckCodePwd")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity chckCode(@RequestAttribute UserkgRequest request) {

        if (request.getCode() == null && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
        } // ???????????????
        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getVerIfy());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "???????????????");
            }
        }
        request.setUserPassword(request.getConfirmPassword());
        if (!userkgService.validatePwd(request)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_LENGTH_ERROR.getCode(), ExceptionEnum.PASSWORD_LENGTH_ERROR.getMessage());
        }
        if (!userkgService.validateconfirmPwd(request)) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????????????????");
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
     * ????????????????????????????????????
     *
     * @param requestse
     * @return
     */
    @ResponseBody
    @RequestMapping("updatePwdFromOuter")
    @BaseControllerNote(needCheckParameter = true, needCheckToken = true, beanClazz = UserkgRequest.class)
    public JsonEntity updatePwdFromOuter(@RequestAttribute UserkgRequest request) {
        if (request.getCode() == null && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
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
     * ????????????
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
     * ????????????????????????
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
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????????????????");
        }
        if (null == response.getData()) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
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
     * ????????????????????????/????????????????????????
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("centerUped")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity centerUped(@RequestAttribute UserkgRequest request) {

        if (null == request.getCode() && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
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
            return JsonEntity.makeExceptionJsonEntity("", "???????????????");
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
     * ????????????????????????????????????
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
        return JsonEntity.makeExceptionJsonEntity("", "??????????????????");

    }

    /**
     * ????????????????????????????????????
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
        return JsonEntity.makeSuccessJsonEntity("???????????????");

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
            // request.setPlatform(3);// ?????????????????????????????? 1???Android?????? 2???iOS?????? 3???PC????????????
            // request.setSession("xxx");// ?????????????????????????????????????????????
            // request.setSig("xxx");// ?????????????????????????????????????????????
            // request.setToken("zxd");// ?????????????????????????????????????????????
            // request.setScene("xxx");// ?????????????????????????????????????????????

            AfsCheckResponse response = client.getAcsResponse(request);
            if (response.getErrorCode() == 0 && response.getData() == true) {
                logger.info("???????????????----????????????");
            } else {
                logger.info("???????????????----????????????");
            }

            return JsonEntity.makeSuccessJsonEntity(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++????????????????????? ===" + ex.getMessage());
            throw new ApiMessageException("????????????????????????");
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
            request.setPlatform(3);// ?????????????????????????????? 1???Android?????? 2???iOS?????? 3???PC????????????
            request.setSession(session);// ?????????????????????????????????????????????
            request.setSig(sig);// ?????????????????????????????????????????????
            request.setToken(token);// ?????????????????????????????????????????????
            request.setScene(scene);// ?????????????????????????????????????????????

            AfsCheckResponse response = client.getAcsResponse(request);
            if (response.getErrorCode() == 0 && response.getData() == true) {
                logger.info("???????????????----????????????");
            } else {
                logger.info("???????????????----????????????");
            }

            return JsonEntity.makeSuccessJsonEntity(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("++????????????????????? ===" + ex.getMessage());
            throw new ApiMessageException("????????????????????????");
        }

    }

    /**
     * ????????????(???.)
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
