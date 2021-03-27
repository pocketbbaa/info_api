package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.DuiBaRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppDuiBaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/10/13.
 */
@Controller
@RequestMapping("/kgApp/duiBa")
public class AppDuiBaController {

	@Autowired
	private AppDuiBaService appDuiBaService;

	@ResponseBody
	@RequestMapping("/generatorDuiBaUrl")
	@BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = DuiBaRequest.class)
	public JSONObject generatorDuiBaUrl(@RequestAttribute UserkgResponse kguser,@RequestAttribute(required = false) DuiBaRequest request){
		return AppJsonEntity.jsonFromObject(appDuiBaService.generatorDuiBaUrl(kguser,request));
	}
}
