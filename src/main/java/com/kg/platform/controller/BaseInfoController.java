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
import com.kg.platform.model.request.BaseInfoRequest;
import com.kg.platform.model.response.BaseInfoResponse;
import com.kg.platform.service.BaseInfoService;

@Controller
@RequestMapping("baseinfo")
public class BaseInfoController extends ApiBaseController {

    @Inject
    BaseInfoService baseInfoService;

    /**
     * 首页右侧关于千氪
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getfriendlyAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = BaseInfoRequest.class)
    public JsonEntity getbaseinfoAll() {
        Result<List<BaseInfoResponse>> responses = baseInfoService.getbaseinfoAll();
        if (null != responses) {
            return JsonEntity.makeSuccessJsonEntity(responses);

        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据失败");

    }

    /*
     * 获取相关信息
     */
    @ResponseBody
    @RequestMapping("getbaseinfoType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = BaseInfoRequest.class)
    public JsonEntity getbaseinfoType(@RequestAttribute BaseInfoRequest request) {
        Result<BaseInfoResponse> response = baseInfoService.getbaseinfoType(request);
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeSuccessJsonEntity("", "获取失败");

    }

}
