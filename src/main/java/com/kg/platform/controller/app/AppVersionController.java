package com.kg.platform.controller.app;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.service.app.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/kgApp/version")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 获取最后更新版本
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AppVersionManageRequest.class)
    public AppJsonEntity getLastVersion(@RequestAttribute AppVersionManageRequest request) {
        if (request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        return appVersionService.getLastVersion(request);
    }

    /**
     * 根据版本号获取版本
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVersion", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AppVersionManageRequest.class)
    public AppJsonEntity getVersion(@RequestAttribute AppVersionManageRequest request, HttpServletRequest servletRequest) {
        if (request == null || StringUtils.isEmpty(request.getVersionNum()) || request.getSystemType() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        return appVersionService.getVersion(request);
    }

}
