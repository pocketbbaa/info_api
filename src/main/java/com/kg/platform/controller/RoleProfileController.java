package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.RoleProfileRequest;
import com.kg.platform.model.response.RoleProfileResponse;
import com.kg.platform.service.RoleProfileService;

@Controller
@RequestMapping("roleProfile")
public class RoleProfileController extends ApiBaseController {
    @Inject
    RoleProfileService roleProfileService;

    @ResponseBody
    @RequestMapping("selectByRoleidKey")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = RoleProfileRequest.class)
    public JsonEntity selectByRoleidKey(@RequestAttribute RoleProfileRequest request) {
        Result<List<RoleProfileResponse>> resultResponse = roleProfileService.selectByRoleidKey(request);
        if (resultResponse.getData() == null) {
            JsonEntity.makeExceptionJsonEntity("", "获取数据有误");
        }
        return JsonEntity.makeSuccessJsonEntity(resultResponse);

    }

}
