package com.kg.platform.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.FeedbackQueryRequest;
import com.kg.platform.model.response.admin.FeedbackQueryResponse;
import com.kg.platform.service.admin.FeedbackService;

@RestController("AdminFeedbackController")
@RequestMapping("admin/feedback")
public class FeedbackController extends AdminBaseController {

    @Autowired
    private FeedbackService feedbackService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值

    }

    @RequestMapping("getFeedbackList")
    @BaseControllerNote(beanClazz = FeedbackQueryRequest.class)
    public JsonEntity getFeedbackList(@RequestAttribute FeedbackQueryRequest request) {
        PageModel<FeedbackQueryResponse> pageModel = feedbackService.getFeedbackList(request);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @RequestMapping("deleteFeedback")
    @BaseControllerNote(beanClazz = FeedbackQueryRequest.class)
    public JsonEntity deleteFeedback(@RequestAttribute FeedbackQueryRequest request) {
        boolean success = feedbackService.deleteFeedback(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @BaseControllerNote(beanClazz = FeedbackQueryRequest.class)
    @RequestMapping("setStatus")
    public JsonEntity setStatus(@RequestAttribute FeedbackQueryRequest request) {
        boolean success = feedbackService.setStatus(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @BaseControllerNote(beanClazz = FeedbackQueryRequest.class)
    @RequestMapping("replay")
    public JsonEntity replay(@RequestAttribute FeedbackQueryRequest request) {
        boolean success = feedbackService.replay(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FeedbackQueryRequest.class)
    public JsonEntity sendEmail(@RequestAttribute FeedbackQueryRequest request) {
        boolean success = feedbackService.sendEmail(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }
}
