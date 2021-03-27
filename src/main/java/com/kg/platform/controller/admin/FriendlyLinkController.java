package com.kg.platform.controller.admin;

import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.AdminBaseRequest;
import com.kg.platform.model.request.admin.FriendlyLinkEditRequest;
import com.kg.platform.model.response.admin.FriendlyLinkQueryResponse;
import com.kg.platform.service.admin.FriendlyLinkService;

@RestController("AdminFriendlyLinkController")
@RequestMapping("admin/friendly")
public class FriendlyLinkController extends AdminBaseController {

    @Autowired
    private FriendlyLinkService friendlyLinkService;

    @RequestMapping(value = "getLinkList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminBaseRequest.class)
    public JsonEntity getLinkList(@RequestAttribute FriendlyLinkRequest request) {
    	if(request.getCurrentPage()==null||request.getPageSize()==null||request.getType()==null){
    		return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
        PageModel<FriendlyLinkResponse> pageModel = friendlyLinkService.getLinkList(request);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @RequestMapping(value = "deleteLink", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FriendlyLinkRequest.class)
    public JsonEntity deleteLink(@RequestAttribute FriendlyLinkRequest request) {
        boolean success = friendlyLinkService.deleteLink(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "setOrder", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FriendlyLinkRequest.class)
    public JsonEntity setOrder(@RequestAttribute FriendlyLinkRequest request) {
        boolean success = friendlyLinkService.setOrder(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "batchSetStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FriendlyLinkRequest.class)
    public JsonEntity batchSetStatus(@RequestAttribute FriendlyLinkRequest request) {
    	if(CollectionUtils.isEmpty(request.getLinkIdList())|| request.getLinkStatus()==null){
    		return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
        boolean success = friendlyLinkService.batchSetStatus(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "setChannel", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FriendlyLinkEditRequest.class)
    public JsonEntity setChannel(@RequestAttribute FriendlyLinkEditRequest request) {
        boolean success = friendlyLinkService.setChannel(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "addOrUpdateLink", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = FriendlyLinkRequest.class)
    public JsonEntity addOrUpdateLink(@RequestAttribute FriendlyLinkRequest request) {
        boolean success = friendlyLinkService.addOrUpdateLink(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }
}
