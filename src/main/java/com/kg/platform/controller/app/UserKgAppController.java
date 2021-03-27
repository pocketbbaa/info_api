package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.UserKgAppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/3/20.
 */
@Controller
@RequestMapping("/kgApp")
public class UserKgAppController {
    @Inject
    private UserKgAppService userKgAppService;

    /**
     * 提交用戶的注册信息
     * @param request
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/login/register")
    @BaseControllerNote(needCheckToken = false, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JSONObject register(@RequestAttribute UserkgRequest request, HttpServletRequest servletRequest){
        return AppJsonEntity.jsonFromObject(userKgAppService.appRegister(request,servletRequest));
    }

    /**
     * 发送短信验证码
     * @param request
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping("/login/sendSmsCode")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JSONObject sendSmsCode(@RequestAttribute UserkgRequest request,HttpServletRequest req){
        return AppJsonEntity.jsonFromObject(userKgAppService.appSendSmsCode(request,req));
    }
    /**
     * 验证验证码和手机或者邮箱是否匹配
     */
    @ResponseBody
    @RequestMapping("/login/chckSmsEmailCode")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JSONObject chckSmsEmailCode(@RequestAttribute UserkgRequest request) {
        return AppJsonEntity.jsonFromObject(userKgAppService.chckSmsEmailCode(request));
    }

    /**
     * 注册后完善资料
     * @param request
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/login/completeProfile")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JSONObject completeProfile(@RequestAttribute UserkgRequest request, @RequestAttribute UserkgResponse kguser, HttpServletRequest req) {
        return AppJsonEntity.jsonFromObject(userKgAppService.completeProfile(request, kguser, req));
    }

    /**
     * 用户使用手机号验证码登录/使用账号密码方式登录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/login/checkLogin")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JSONObject checkLogin(@RequestAttribute UserkgRequest request, HttpServletRequest req) {
        return AppJsonEntity.jsonFromObject(userKgAppService.checkLogin(request,req));
    }

}
