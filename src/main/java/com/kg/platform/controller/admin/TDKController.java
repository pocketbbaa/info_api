package com.kg.platform.controller.admin;

import com.kg.platform.model.request.KgSeoTdkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.TDKEditRequest;
import com.kg.platform.service.admin.TDKService;

@RestController("AdminTDKController")
@RequestMapping("admin/tdk")
public class TDKController extends AdminBaseController {

    @Autowired
    private TDKService tdkService;

    @RequestMapping(value = "editHomeTDK", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TDKEditRequest.class)
    public JsonEntity editHomeTDK(@RequestAttribute TDKEditRequest request) {
        boolean success = tdkService.editHomeTDK(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

	@RequestMapping(value = "detailTdk", method = RequestMethod.POST)
	@BaseControllerNote(beanClazz = KgSeoTdkRequest.class)
	public JsonEntity detailTdk(@RequestAttribute KgSeoTdkRequest request) {
    	return tdkService.detailTdk(request);
	}

	@RequestMapping(value = "editTdk", method = RequestMethod.POST)
	@BaseControllerNote(beanClazz = KgSeoTdkRequest.class)
	public JsonEntity editTdk(@RequestAttribute KgSeoTdkRequest request) {
		return tdkService.editTdk(request);
	}
}
