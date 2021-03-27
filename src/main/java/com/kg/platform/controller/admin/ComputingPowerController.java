package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.model.request.admin.CloudOrderRequest;
import com.kg.platform.service.admin.ComputingPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/2/22.
 */
@RestController("admin/computingPower")
public class ComputingPowerController {


	@Autowired
	private ComputingPowerService computingPowerService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

	}

	/**
	 * 算力收益管理列表
	 *
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = CloudOrderRequest.class)
	public JsonEntity list(@RequestAttribute CloudOrderRequest request) {
		if(request.getCurrentPage()==null || request.getPageSize()==null){
			return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
		return computingPowerService.list(request);
	}

	/**
	 * 算力收益管理详情
	 *
	 * @return
	 */
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	@BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = CloudOrderRequest.class)
	public JsonEntity detail(@RequestAttribute CloudOrderRequest request) {
		if(request.getId()==null){
			return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
		return computingPowerService.detail(request);
	}

	/**
	 * 算力收益订单修改
	 *
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = CloudOrderRequest.class)
	public JsonEntity update(@RequestAttribute CloudOrderRequest request) {
		if(request.getId()==null){
			return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
		return computingPowerService.update(request);
	}

}
