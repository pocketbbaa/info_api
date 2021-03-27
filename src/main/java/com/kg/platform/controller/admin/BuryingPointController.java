package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.BuryingPointRequest;
import com.kg.platform.service.admin.BuryingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/7/20.
 */
@Controller
@RequestMapping("/admin/buryingPoint")
public class BuryingPointController {
    @Autowired
    private BuryingPointService buryingPointService;

    @ResponseBody
    @RequestMapping("buryingPointListWithType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true,beanClazz = BuryingPointRequest.class)
    public JsonEntity buryingPointListWithType(@RequestAttribute BuryingPointRequest request, PageModel page){
        page.setCurrentPage(request.getCurrentPage());
        if (request.getPageSize() != null) {
            page.setPageSize(request.getPageSize());
        }
        if(request.getDataType()==null||request.getCurrentPage()==null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        return JsonEntity.makeSuccessJsonEntity(buryingPointService.buryingPointListWithType(request,page));
    }

    @ResponseBody
    @RequestMapping("excelOfBuryingPointList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true,beanClazz = BuryingPointRequest.class)
    public void excelOfBuryingPointList( @RequestAttribute BuryingPointRequest request, HttpServletResponse response, HttpServletRequest servletRequest){
        buryingPointService.excelOfBuryingPointList(request,response,servletRequest);
    }
}
