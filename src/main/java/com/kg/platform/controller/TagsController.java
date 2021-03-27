package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.response.TagsResponse;
import com.kg.platform.service.TagsService;

@Controller
@RequestMapping("tags")
public class TagsController extends ApiBaseController {

    @Inject
    TagsService tagsService;

    @ResponseBody
    @RequestMapping("listTags")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity listTags() {
        Result<List<TagsResponse>> result = tagsService.listTages();
        if (result.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(result);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据错误");

    }

}
