package com.kg.platform.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.DataStatQueryRequest;
import com.kg.platform.model.response.admin.DataStatQueryResponse;
import com.kg.platform.service.admin.DataStatService;

@RestController("AdminDataStatController")
@RequestMapping("admin/datastat")
public class DataStatController extends AdminBaseController {

    @Autowired
    private DataStatService dataStatService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
    }

    @RequestMapping(value = "getDataStatChart", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = DataStatQueryRequest.class)
    public JsonEntity getDataStatChart(@RequestAttribute DataStatQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(dataStatService.getDataStatChart(request));
    }

    @RequestMapping(value = "getColumnUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = DataStatQueryRequest.class)
    public JsonEntity getColumnUserList(@RequestAttribute DataStatQueryRequest request) {
        List<DataStatQueryResponse> data = dataStatService.getColumnUserList(request);
        return JsonEntity.makeSuccessJsonEntity(data);
    }

    @RequestMapping(value = "getNormalUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = DataStatQueryRequest.class)
    public JsonEntity getNormalUserList(@RequestAttribute DataStatQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(dataStatService.getNormalUserList(request));
    }
}
