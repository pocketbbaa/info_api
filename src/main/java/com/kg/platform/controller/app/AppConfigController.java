package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.service.app.AppConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
@Controller
@RequestMapping("/kgApp/appConfig")
public class AppConfigController {
    @Inject
    private AppConfigService appConfigService;

    /**
     * 获取客服联系方式、底部导航图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAppConfig")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = true, beanClazz = AppVersionManageRequest.class)
    public JSONObject getAppConfig(@RequestAttribute AppVersionManageRequest request,HttpServletRequest servletRequest){
        Map response = appConfigService.getAppConfig(request,servletRequest);
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(response));
    }

    @ResponseBody
    @RequestMapping("/getInviteRule")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, isLogin = true)
    public JSONObject getInviteRule(){
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(appConfigService.getInviteRule()));
    }

	@ResponseBody
	@RequestMapping("/getSuspendIcon")
	@BaseControllerNote(needCheckToken = false, needCheckParameter = false, isLogin = true)
	public JSONObject getSuspendIcon(HttpServletRequest servletRequest){
		return AppJsonEntity.jsonFromObject(appConfigService.getSuspendIcon(servletRequest));
	}
}
