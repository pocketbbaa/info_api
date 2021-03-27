package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.ad.dubboservice.AdverService;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.CoinBgModel;
import com.kg.platform.model.request.*;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.AdverResponse;
import com.kg.platform.model.response.PersonalAdvResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppActivityService;
import com.kg.platform.service.app.AppAdverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/18.
 */
@Controller
@RequestMapping("/kgApp/adver")
public class AppAdverController {


    @Inject
    private AppAdverService appAdverService;

    @Inject
    private CoinBgModel coinBgModel;

    /**
     * app启动页广告
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStartPageAdv")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject getStartPageAdv(HttpServletRequest servletRequest){
        SiteimageRequest siteimageAppRequest=new SiteimageRequest();
        siteimageAppRequest.setNavigator_pos(7);
        siteimageAppRequest.setImage_pos(1);
        siteimageAppRequest.setPageSize(1);
        siteimageAppRequest.setCurrentPage(1);
        siteimageAppRequest.setDisPlayPort(2);
        siteimageAppRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        AppJsonEntity data = appAdverService.getStartPageAdv(siteimageAppRequest);
        if(data==null){
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.MO_START_PAGE.getCode(),ExceptionEnum.MO_START_PAGE.getMessage()));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(data.getData()));
    }


    /**
     * app个人中心广告
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPersonalAdv")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject getPersonalAdv(HttpServletRequest servletRequest){
        SiteimageRequest siteimageAppRequest=new SiteimageRequest();
        siteimageAppRequest.setNavigator_pos(3);
        siteimageAppRequest.setImage_pos(31);
        siteimageAppRequest.setPageSize(5);
        siteimageAppRequest.setCurrentPage(1);
        siteimageAppRequest.setDisPlayPort(2);
        siteimageAppRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        List<PersonalAdvResponse> s= appAdverService.getPersonalAdv(siteimageAppRequest);
        JSONObject resultJson = AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(s));
        resultJson.put("compassUrl",coinBgModel.getCompassUrl());
        return resultJson;
    }
}
