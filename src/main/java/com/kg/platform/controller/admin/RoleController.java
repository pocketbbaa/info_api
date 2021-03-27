package com.kg.platform.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.RoleEditRequest;
import com.kg.platform.model.request.admin.RoleQueryRequest;
import com.kg.platform.model.response.admin.RoleQueryResponse;
import com.kg.platform.service.admin.RoleService;

@RequestMapping("admin/role")
@RestController("AdminRoleController")
public class RoleController extends AdminBaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "getRoleList", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getRoleList() {
        List<RoleQueryResponse> data = roleService.getRoleList();
        return JsonEntity.makeSuccessJsonEntity(data);
    }

    @BaseControllerNote(beanClazz = RoleEditRequest.class)
    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    public JsonEntity setStatus(@RequestAttribute RoleEditRequest request) {
        boolean success = roleService.setStatus(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "getRoleProfile", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = RoleQueryRequest.class)
    public JsonEntity getRoleProfile(@RequestAttribute RoleQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(roleService.getRoleProfile(request));
    }

    @RequestMapping(value = "setRoleProfile", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = RoleEditRequest.class)
    public JsonEntity setRoleProfile(@RequestAttribute RoleEditRequest request) {
        boolean success = roleService.setRoleProfile(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }
}
