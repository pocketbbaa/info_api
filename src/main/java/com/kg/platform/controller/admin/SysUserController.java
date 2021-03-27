package com.kg.platform.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.SysPost;
import com.kg.platform.model.request.admin.SysUserEditRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.request.admin.SysUserUserRequest;
import com.kg.platform.model.response.admin.SysUserQueryResponse;
import com.kg.platform.model.response.admin.SysUserUserResponse;
import com.kg.platform.service.admin.SysUserService;

@RequestMapping("admin/sysuser")
@RestController("AdminSysUserController")
public class SysUserController extends AdminBaseController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "getPostList", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getPostList() {
        List<SysPost> list = sysUserService.getPostList();
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @RequestMapping(value = "getSysUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserQueryRequest.class)
    public JsonEntity getSysUserList(@RequestAttribute SysUserQueryRequest request) {
        PageModel<SysUserQueryResponse> pageModel = sysUserService.getSysUserList(request);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserQueryRequest.class)
    public JsonEntity resetPassword(@RequestAttribute SysUserQueryRequest request) {
        if (null != request.getUserId() && !StringUtils.isBlank(request.getPassword())) {
            String regex = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}$";
            if (request.getPassword().matches(regex) == false) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        "密码必须同时包含大写字母、小写字母和数字，并且长度在6-20位！");
            }
            boolean success = sysUserService.resetPassword(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserQueryRequest.class)
    public JsonEntity setStatus(@RequestAttribute SysUserQueryRequest request) {
        if (null != request.getUserId() && null != request.getStatus()) {
            boolean success = sysUserService.setStatus(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setKgUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserQueryRequest.class)
    public JsonEntity setKgUser(@RequestAttribute SysUserQueryRequest request) {
        if (null != request.getUserId() && null != request.getKgUserId()) {

            boolean success = sysUserService.setKgUser(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "unsetKgUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserUserRequest.class)
    public JsonEntity unsetKgUser(@RequestAttribute SysUserUserRequest request) {
        if (null != request.getRelId()) {
            boolean success = sysUserService.unsetKgUser(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "getRelUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserUserRequest.class)
    public JsonEntity getRelUser(@RequestAttribute SysUserUserRequest request) {
        if (null != request.getSysUserId()) {
            return JsonEntity.makeSuccessJsonEntity(sysUserService.getRelUser(request));

        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "addSysUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserEditRequest.class)
    public JsonEntity addSysUser(@RequestAttribute SysUserEditRequest request) {
        String regex = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}$";
        if (!StringUtils.isBlank(request.getPassword()) && request.getPassword().matches(regex) == false) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    "密码必须同时包含大写字母、小写字母和数字，并且长度在6-20位！");
        }
        boolean success = sysUserService.addSysUser(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "用户名重复");
        }
    }

    @RequestMapping(value = "getSysUserById", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SysUserQueryRequest.class)
    public JsonEntity getSysUserById(@RequestAttribute SysUserQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(sysUserService.getSysUserById(request));
    }

    @RequestMapping(value = "getSysUsers", method = RequestMethod.POST)
    public JsonEntity getSysUsers() {
        List<SysUserUserResponse> syusers = sysUserService.getSysUsers();
        return JsonEntity.makeSuccessJsonEntity(syusers);
    }
}
