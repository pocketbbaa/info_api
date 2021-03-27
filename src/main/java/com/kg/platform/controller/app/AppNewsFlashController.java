package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;
import com.kg.platform.service.NewsFlashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/23.
 */
@Controller
@RequestMapping("/kgApp/newsFlash")
public class AppNewsFlashController {
    @Autowired
    private NewsFlashService newsFlashService;

    @ResponseBody
    @RequestMapping("getNewsFlashListByType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JSONObject getNewsFlashListByType(@RequestAttribute KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page,HttpServletRequest servletRequest){
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        Map<String,Object> listResponse = newsFlashService.getNewsFlashListByType(request,page,servletRequest);
        PageModel pageModel = (PageModel<KgNewsFlashResponse>)listResponse.get("page");
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(pageModel.getData()));
    }

    /**
     * 24h快讯详情
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JSONObject getNewsFlashDetail(@RequestAttribute KgNewsFlashRequest request) {
        Result<KgNewsFlashResponse> listResponse = newsFlashService.getNewsFlashDetail(request);
        if(listResponse.getData()==null){
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(listResponse.getData()));
    }

    @ResponseBody
    @RequestMapping("getNewsFlashTopMenus")
    @BaseControllerNote(needCheckToken = false,needCheckParameter = false)
    public JSONObject getNewsFlashTopMenus(){
        return AppJsonEntity.jsonFromObject(newsFlashService.getNewsFlashTopMenus());
    }
}
