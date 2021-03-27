package com.kg.platform.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.KeywordEditRequest;
import com.kg.platform.model.request.admin.KeywordQueryRequest;
import com.kg.platform.model.response.admin.KeywordQueryResponse;
import com.kg.platform.service.admin.KeywordService;

@RestController("AdminKeywordController")
@RequestMapping("admin/keyword")
public class KeywordController extends AdminBaseController {

    @Autowired
    private KeywordService keywordService;

    @RequestMapping(value = "getKeywordList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordQueryRequest.class)
    public JsonEntity getKeywordList(@RequestAttribute KeywordQueryRequest request) {
        PageModel<KeywordQueryResponse> pageModel = keywordService.getKeywordList(request);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @RequestMapping(value = "deleteKeyword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordEditRequest.class)
    public JsonEntity deleteKeyword(@RequestAttribute KeywordEditRequest request) {
        boolean success = keywordService.deleteKeyword(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "setOrder", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordEditRequest.class)
    public JsonEntity setOrder(@RequestAttribute KeywordEditRequest request) {
        boolean success = keywordService.setOrder(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordEditRequest.class)
    public JsonEntity setStatus(@RequestAttribute KeywordEditRequest request) {
        boolean success = keywordService.setStatus(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "setChannel", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordEditRequest.class)
    public JsonEntity setChannel(@RequestAttribute KeywordEditRequest request) {
        boolean success = keywordService.setChannel(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "addKeyword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = KeywordEditRequest.class)
    public JsonEntity addKeyword(@RequestAttribute KeywordEditRequest request) {
        boolean success = keywordService.addKeyword(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }
}
