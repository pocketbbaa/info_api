package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.enumeration.DateEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;
import com.kg.platform.service.FriendlylinkService;

/**
 * 合作伙伴
 * 
 * @author think
 *
 */
@Controller
@RequestMapping("friendlylink")
public class FriendlylinkController extends ApiBaseController {

    @Inject
    FriendlylinkService friendlylinkService;

    @Cacheable(category = "/friendlylink/getFriendlyAll", key = "getFriendlyAll", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("getfriendlyAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = FriendlyLinkRequest.class)
    public JsonEntity getFriendlyAll(@RequestAttribute FriendlyLinkRequest request) {

    	if(request.getType()==null){
    		return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
        Result<List<FriendlyLinkResponse>> listresponse = friendlylinkService.getFriendlyAll(request);
        if (null != listresponse.getData()) {
            return JsonEntity.makeSuccessJsonEntity(listresponse);
        }

        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误");

    }

}
