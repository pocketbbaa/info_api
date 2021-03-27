package com.kg.platform.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.kg.platform.enumeration.RegisterOriginEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.model.in.ThirdPartyLoginInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.out.ThirdPartyLoginOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.ThirdPartyLoginRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.request.admin.WeixinRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.ThirdPartyService;
import com.kg.platform.service.UserkgService;

/**
 * 第三方登录获取信息
 * 
 * @author 74190
 *
 */
@RestController
@RequestMapping("thirdParty")
public class ThirdPartyController extends ApiBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyController.class);

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Inject
    private UserkgService userkgService;

    @Inject
    private JedisUtils jedisUtils;

    @Inject
    private IDGen idGenerater;

    /**
     * 检查第三方用户用户是否注册
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "checkThirdUserInfo", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false, isLogin = false, beanClazz = WeixinRequest.class)
    public JsonEntity checkThirdUserInfo(@RequestAttribute WeixinRequest request, HttpServletRequest req)
            throws Exception {
        if (null == request || null == request.getCode() || null == request.getOpenType()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.AUTHERROR.getCode(),
                    ExceptionEnum.AUTHERROR.getMessage());
        }
        // 判断账户是否已经绑定账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setOpenId(request.getOpenId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        logger.info("判断账户是否已经绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(thirdPartyLoginRequest));
        ThirdPartyLoginOutModel thirdPartyLoginOutModel = thirdPartyService
                .checkBindStatusByOpenId(thirdPartyLoginRequest);
        if (thirdPartyLoginOutModel != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REBIND.getCode(),
                    ExceptionEnum.REBIND.getMessage());
        }
        logger.info("判断账户是否已经绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(thirdPartyLoginOutModel));
        ThirdPartyLoginInModel thirdPartyLoginInModel = new ThirdPartyLoginInModel();
        thirdPartyLoginInModel.setOpenType(request.getOpenType());
        thirdPartyLoginInModel.setCode(request.getCode());
        Result<UserkgResponse> out = thirdPartyService.checkThirdUserLogin(thirdPartyLoginInModel, req);
        if ("2".equals(out.getData().getBindStatus()) || "3".equals(out.getData().getBindStatus())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.AUTHERROR.getCode(),
                    ExceptionEnum.AUTHERROR.getMessage());
        }
        logger.info("检查绑定返回信息>>>>>>>>>>" + JsonUtil.writeValueAsString(out));
        return JsonEntity.makeSuccessJsonEntity(out);
    }

    /**
     * 微信微博关联注册进行绑定
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("bindRegistUser")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity bindRegistUser(@RequestAttribute UserkgRequest request, HttpServletRequest requestse)
            throws Exception {
        boolean success;
        logger.info("微信微博注册并账号进行绑定>>>>>>>>>>" + JsonUtil.writeValueAsString(request));
        if (null == request.getOpenId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (null == request.getOpenType()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        // 判断账户是否已经绑定账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setOpenId(request.getOpenId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        ThirdPartyLoginOutModel thirdPartyLoginOutModel = thirdPartyService
                .checkBindStatusByOpenId(thirdPartyLoginRequest);
        if (thirdPartyLoginOutModel != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REBIND.getCode(),
                    ExceptionEnum.REBIND.getMessage());
        }
        /**
         * 用户注册手机和邮箱二选其一 if判断邮箱或手机是否已经注册
         */
        if (null != request.getUserMobile()) {
            if (userkgService.checkuserMobile(request).getData() > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_USED.getCode(),
                        ExceptionEnum.MOBILE_USED.getMessage());
            }
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
        if (userkgService.validatePwd(request) != false && userkgService.validateconfirmPwd(request) != false) {
            request.setUserId(idGenerater.nextId());
            request.setRegisterIp(HttpUtil.getIpAddr(requestse));
            request.setMobileAuthed(true);
            request.setCreateDate(new Date());

            ThirdPartyLoginInModel thirdPartyLoginInModel = thirdPartyService.bindRegistUser(request, requestse);
            if (thirdPartyLoginInModel == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            }
            request.setUserName(thirdPartyLoginInModel.getNickName());
            request.setRegisterOrigin(RegisterOriginEnum.WEB.getOrigin());
            success = userkgService.checkAddUser(request);
            if (success == false) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            } else {
                Result<UserkgResponse> resp = thirdPartyService.mobileLogin(request, requestse);
                resp.getData().setNewUser(1);
                return JsonEntity.makeSuccessJsonEntity(resp);
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_CONFIRM.getCode(),
                    ExceptionEnum.PASSWORD_CONFIRM.getMessage());
        }
    }

    /**
     * 微信微博关联账号进行绑定
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("bindLoginUser")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity bindLoginUser(@RequestAttribute UserkgRequest request, HttpServletRequest req) throws Exception {
        logger.info("微信微博登录进行绑定>>>>>>>>>>" + JsonUtil.writeValueAsString(request));
        // 判断账户是否已经绑定账号
        // 判断账户是否已经绑定账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setOpenId(request.getOpenId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        logger.info("判断账户是否已经绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(thirdPartyLoginRequest));

        String remoteIp = HttpUtil.getIpAddr(req);
        Result<UserkgResponse> userkgResponse = userkgService.checkLogin(request, req);
        if (userkgResponse.getData() != null) {
            request.setUserId(Long.valueOf(userkgResponse.getData().getUserId()));
            thirdPartyLoginRequest.setUserId(request.getUserId());
            ThirdPartyLoginOutModel thirdPartyLoginOutModel = thirdPartyService
                    .checkBindStatusByOpenId(thirdPartyLoginRequest);
            if (thirdPartyLoginOutModel != null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_RE_BINGD_ERROR.getCode(),
                        ExceptionEnum.MOBILE_RE_BINGD_ERROR.getMessage());
            }

            Boolean isOk = thirdPartyService.bindLoginUser(request, req);
            if (isOk) {
                userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));
                this.jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));
                this.jedisUtils.del(JedisKey.loginIp(remoteIp));
                return JsonEntity.makeSuccessJsonEntity(userkgResponse);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_THIRDPAT_ERROR.getCode(),
                        ExceptionEnum.BINGD_THIRDPAT_ERROR.getMessage());
            }
        } else {
            int loginTimes = userkgService.checkLoginLimit(remoteIp, request);
            logger.info("用户登录：" + JSON.toJSONString(request) + "失败");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                    ExceptionEnum.USER_LONGFAILURE.getMessage() + "，还有" + (5 - loginTimes) + "次机会，连续输错超过5次，您的账号将被暂时锁定");
        }

    }

    /**
     * 微信微博手机号登录进行绑定
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("bindMobileLogin")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity bindMobileLogin(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        Boolean isok = true;
        logger.info("微信微博手机验证码登录进行绑定>>>>>>>>>>" + JsonUtil.writeValueAsString(request));
        // 判断账户是否已经绑定账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setOpenId(request.getOpenId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        logger.info("判断账户是否已经绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(thirdPartyLoginRequest));
        ThirdPartyLoginOutModel thirdPartyLoginOutModel = thirdPartyService
                .checkBindStatusByOpenId(thirdPartyLoginRequest);
        if (thirdPartyLoginOutModel != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REBIND.getCode(),
                    ExceptionEnum.REBIND.getMessage());
        }
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
            ThirdPartyLoginInModel thirdPartyLoginInModel = thirdPartyService.bindMobileLogin(request);
            if (thirdPartyLoginInModel == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            }
            request.setUserName(thirdPartyLoginInModel.getNickName());
            request.setRegisterOrigin(RegisterOriginEnum.WEB.getOrigin());
            isok = userkgService.checkAddUser(request);
            newUser = 1;
        } else {
            UserkgRequest re = new UserkgRequest();
            re.setUserMobile(request.getUserMobile());
            UserkgOutModel userkgOutModel = userkgService.getUserInfo(re);
            logger.info("userkgOutModel>>>>>>>>>>" + JsonUtil.writeValueAsString(userkgOutModel));
            if (userkgOutModel == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            }

            thirdPartyLoginRequest.setUserId(Long.valueOf(userkgOutModel.getUserId()));
            thirdPartyLoginOutModel = thirdPartyService.checkBindStatusByOpenId(thirdPartyLoginRequest);
            if (thirdPartyLoginOutModel != null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MOBILE_RE_BINGD_ERROR.getCode(),
                        ExceptionEnum.MOBILE_RE_BINGD_ERROR.getMessage());
            }

            request.setUserId(Long.valueOf(userkgOutModel.getUserId()));
            ThirdPartyLoginInModel thirdPartyLoginInModel = thirdPartyService.bindMobileLogin(request);
            if (thirdPartyLoginInModel == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_ADDFAILURE.getCode(),
                        ExceptionEnum.USER_ADDFAILURE.getMessage());
            }
            isok = true;
        }
        if (isok) {
            Result<UserkgResponse> userkgResponse = userkgService.mobileLogin(request, req);
            if (userkgResponse.getData() != null) {
                userkgResponse.getData().setInmagelink(propertyLoader.getProperty("platform", "global.imgServer"));
                userkgResponse.getData().setNewUser(newUser);
                return JsonEntity.makeSuccessJsonEntity(userkgResponse);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_LONGFAILURE.getCode(),
                        ExceptionEnum.USER_LONGFAILURE.getMessage());
            }

        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCESS_ERROR.getCode(),
                    ExceptionEnum.ACCESS_ERROR.getMessage());
        }
    }

    /**
     * 微信微博取消绑定
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("unbind")
    @BaseControllerNote(needCheckToken = true, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity unbind(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        // 判断是否绑定过账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setUserId(request.getUserId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        if (thirdPartyService.checkBindStatus(thirdPartyLoginRequest) == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.UN_BINGD_ERROR.getCode(),
                    ExceptionEnum.UN_BINGD_ERROR.getMessage());
        }
        if (null != request.getUserId()) {
            if (thirdPartyService.unbind(thirdPartyLoginRequest)) {
                return JsonEntity.makeSuccessJsonEntity("取消绑定成功");
            }
            ;
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCESS_ERROR.getCode(),
                ExceptionEnum.ACCESS_ERROR.getMessage());
    }

    /**
     * 通过userId绑定账号
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("bindUser")
    @BaseControllerNote(needCheckToken = true, isLogin = true, needCheckParameter = true, beanClazz = ThirdPartyLoginRequest.class)
    public JsonEntity bindUser(@RequestAttribute ThirdPartyLoginRequest request, HttpServletRequest req) {
        logger.info("通过userId绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(request));
        // 判断是否绑定过账号
        // 判断账户是否已经绑定账号
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setOpenId(request.getOpenId());
        thirdPartyLoginRequest.setOpenType(request.getOpenType());
        logger.info("判断账户是否已经绑定账号>>>>>>>>>>" + JsonUtil.writeValueAsString(thirdPartyLoginRequest));
        ThirdPartyLoginOutModel thirdPartyLoginOutModel = thirdPartyService
                .checkBindStatusByOpenId(thirdPartyLoginRequest);
        if (thirdPartyLoginOutModel != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REBIND.getCode(),
                    ExceptionEnum.REBIND.getMessage());
        }
        if (null != request.getUserId() && null != request.getOpenType() && null != request.getOpenId()) {
            UserkgRequest userkgRequest = new UserkgRequest();
            userkgRequest.setUserId(request.getUserId());
            userkgRequest.setOpenType(request.getOpenType());
            userkgRequest.setOpenId(request.getOpenId());
            if (thirdPartyService.bindLoginUser(userkgRequest, req)) {
                return JsonEntity.makeSuccessJsonEntity("绑定成功");
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_THIRDPAT_ERROR.getCode(),
                        ExceptionEnum.BINGD_THIRDPAT_ERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

}
