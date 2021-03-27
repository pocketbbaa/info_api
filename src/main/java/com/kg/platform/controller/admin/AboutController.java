package com.kg.platform.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.BaseInfo;
import com.kg.platform.model.request.admin.BaseinfoStatusRequest;
import com.kg.platform.model.response.admin.BaseinfoQueryResponse;
import com.kg.platform.service.admin.AboutService;

@RestController("AdminAboutController")
@RequestMapping("admin/about")
public class AboutController extends AdminBaseController {

    @Autowired
    private AboutService aboutService;

    @RequestMapping(value = "getBaseinfoList", method = RequestMethod.POST)
    public JsonEntity getBaseinfoList() {
        List<BaseinfoQueryResponse> list = aboutService.getBaseinfoList();
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @RequestMapping(value = "setInfoStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = BaseinfoStatusRequest.class)
    public JsonEntity setInfoStatus(@RequestAttribute BaseinfoStatusRequest request) {
        if (null != request.getId() && null != request.getUpdateUser()) {
            boolean success = aboutService.setInfoStatus(request.getId(), request.getUpdateUser());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "deleteBaseinfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = BaseinfoStatusRequest.class)
    public JsonEntity deleteBaseinfo(@RequestAttribute BaseinfoStatusRequest request) {
        if (null != request.getId()) {
            boolean success = aboutService.deleteBaseinfo(request.getId());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "addBaseinfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = BaseInfo.class)
    public JsonEntity addBaseinfo(@RequestAttribute("request") BaseInfo baseInfo) {
        if (!StringUtils.isBlank(baseInfo.getInfoName()) && !StringUtils.isBlank(baseInfo.getInfoType())
                && !StringUtils.isBlank(baseInfo.getInfoDetail()) && null != baseInfo.getInfoStatus()
                && null != baseInfo.getInfoOrder() && null != baseInfo.getCreateUser()) {
            boolean success = aboutService.addBaseinfo(baseInfo);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "类别已存在！");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setInfoOrder", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = BaseinfoStatusRequest.class)
    public JsonEntity setInfoOrder(@RequestAttribute BaseinfoStatusRequest request) {
        if (null != request.getId() && null != request.getUpdateUser() && null != request.getInfoOrder()) {
            boolean success = aboutService.setInfoOrder(request.getId(), request.getInfoOrder(),
                    request.getUpdateUser());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }
}
