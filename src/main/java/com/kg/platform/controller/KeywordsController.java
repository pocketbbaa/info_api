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
import com.kg.platform.model.request.KeywordsRequest;
import com.kg.platform.model.response.KeywordsResponse;
import com.kg.platform.service.KeywordsService;

/**
 * 热门关键词
 * 
 * @author think
 *
 */
@Controller
@RequestMapping("keywords")
public class KeywordsController extends ApiBaseController {

    @Inject
    KeywordsService keywordsService;

    @ResponseBody
    @RequestMapping("getkeywordsAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KeywordsRequest.class)
    public JsonEntity getKeywordsAll(@RequestAttribute KeywordsRequest request) {
        Result<List<KeywordsResponse>> listresponse = keywordsService.getKeywordsAll(request);
        if (null != listresponse.getData()) {
            return JsonEntity.makeSuccessJsonEntity(listresponse);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误");

    }
}
