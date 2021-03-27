package com.kg.platform.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.FeedbackRequest;
import com.kg.platform.service.FeedbackService;

@Controller
@RequestMapping("feedback")
public class FeedbackController extends ApiBaseController {

    @Inject
    FeedbackService feedbackService;

    @ResponseBody
    @RequestMapping("addFeedback")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = FeedbackRequest.class)
    public JsonEntity addFeedback(@RequestAttribute FeedbackRequest request) {
        boolean success = feedbackService.addFeedback(request);
        if (success) {
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "保存失败");

    }

}
