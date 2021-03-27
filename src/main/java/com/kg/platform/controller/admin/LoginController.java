package com.kg.platform.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.admin.LoginRequest;
import com.kg.platform.model.request.admin.UserQueryRequest;
import com.kg.platform.model.response.admin.LoginResponse;
import com.kg.platform.service.admin.LoginService;

@RestController("AdminLoginController")
@RequestMapping("admin/login")
public class LoginController extends AdminBaseController {

    @Autowired
    private LoginService loginService;

    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = true, beanClazz = LoginRequest.class)
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonEntity login(@RequestAttribute LoginRequest request) {
        if (!StringUtils.isBlank(request.getUsername()) && !StringUtils.isBlank(request.getPassword())) {
            String password = request.getPassword();
            String regex = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}$";
            if (password.matches(regex) == false) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        "密码必须同时包含大写字母、小写字母和数字，并且长度在6-20位！");
            }
            LoginResponse model = loginService.login(request.getUsername(), request.getPassword());
            if (null != model) {
                if (null != model.getToken()) {
                    return JsonEntity.makeSuccessJsonEntity(model);
                }
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), model.getRealname());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "用户名或密码错误！");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "logOut", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = LoginRequest.class)
    public JsonEntity logOUt(@RequestAttribute LoginRequest request) {
        loginService.logOut(request.getUserId());
        return JsonEntity.makeSuccessJsonEntity(request.getUserId());
    }

    @RequestMapping(value = "getSysMenu", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getSysMenu(@RequestAttribute UserQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(loginService.getSysMenu(request));
    }
}
