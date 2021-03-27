package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.HotsearchRequest;
import com.kg.platform.model.response.HotsearchResponse;
import com.kg.platform.service.HotsearchService;

@Controller
@RequestMapping("hotsearch")
public class HotsearchController extends ApiBaseController {

    @Inject
    HotsearchService hotsearchService;

    /**
     * 获取热搜词
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("selectHotAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = HotsearchRequest.class)
    public JsonEntity selectHotAll() {
        Result<List<HotsearchResponse>> response = hotsearchService.selectHotAll();
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }

        return JsonEntity.makeExceptionJsonEntity("", "获取数据失败");

    }

}
