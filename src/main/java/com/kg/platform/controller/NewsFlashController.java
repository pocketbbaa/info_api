package com.kg.platform.controller;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;
import com.kg.platform.service.NewsFlashService;
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
 * Created by Administrator on 2018/3/13.
 */
@Controller
@RequestMapping("newsFlash")
public class NewsFlashController {
    @Inject
    private NewsFlashService newsFlashService;
    /**
     * 24h快讯页
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashListByType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity getNewsFlashListByType(@RequestAttribute KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page, HttpServletRequest servletRequest) {
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        Map<String,Object> listResponse = newsFlashService.getNewsFlashListByType(request,page,servletRequest);
        return JsonEntity.makeSuccessJsonEntity(listResponse);
    }

    /**
     * 首页24h快讯
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity getNewsFlashList() {
        Map<String,Object> listResponse = newsFlashService.getNewsFlashList();
        return JsonEntity.makeSuccessJsonEntity(listResponse);
    }

    /**
     * 24h快讯详情
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity getNewsFlashDetail(@RequestAttribute KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page) {
        Result<KgNewsFlashResponse> listResponse = newsFlashService.getNewsFlashDetail(request);
        if(listResponse.getData()==null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        List<KgNewsFlashResponse> list = new ArrayList<>();
        list.add(listResponse.getData());
        PageModel<KgNewsFlashResponse> pageModel = new PageModel<>();
        pageModel.setData(list);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    /**
     * 24h快讯未读数量
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNumberUnread")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class,isLogin = true)
    public JsonEntity getNumberUnread(@RequestAttribute KgNewsFlashRequest request) {
        if(StringUtils.isEmpty(request.getCreateDate())||request.getNewsflashType()==null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
        Result<Long> unreadNumber = newsFlashService.getNumberUnread(request);
        return JsonEntity.makeSuccessJsonEntity(unreadNumber);
    }


    /**
     * 用于前端第一次建立会话等待推送数据的空档期
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("websocketNewsFlash")
    @BaseControllerNote(needCheckToken = false,isLogin = true)
    public JsonEntity websocketNewsFlash() {
        Result<List<KgNewsFlashResponse>> result = newsFlashService.websocketNewsFlash();
        return JsonEntity.makeSuccessJsonEntity(result);
    }
}
