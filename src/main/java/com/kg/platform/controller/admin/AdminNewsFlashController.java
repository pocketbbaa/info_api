package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;
import com.kg.platform.model.response.PushNewsFlashResponse;
import com.kg.platform.service.NewsFlashService;
import com.kg.platform.service.admin.AdminNewsFlashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/13.
 */
@Controller
@RequestMapping("/admin/newsFlash")
public class AdminNewsFlashController {
    @Autowired
    private AdminNewsFlashService adminNewsFlashService;
    /**
     * 24h快讯页
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashListByCondition")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity getNewsFlashListByCondition(@RequestAttribute KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page) {
        Result<PageModel<KgNewsFlashResponse>> result = adminNewsFlashService.getNewsFlashListByCondition(request,page);
        return JsonEntity.makeSuccessJsonEntity(result.getData());
    }


    /**
     * 24h快讯详情
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("detailNewsFlash")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity detailNewsFlash(@RequestAttribute KgNewsFlashRequest request) {
        if(request.getNewsflashId()==null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Result<KgNewsFlashResponse> result = adminNewsFlashService.detailNewsFlash(request);
        return JsonEntity.makeSuccessJsonEntity(result.getData());
    }

    /**
     * 新增快讯
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("addNewsFlash")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity addNewsFlash(@RequestAttribute KgNewsFlashRequest request) {
        return adminNewsFlashService.addNewsFlash(request);
    }

    /**
     * 删除快讯
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("delNewsFlash")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity delNewsFlash(@RequestAttribute KgNewsFlashRequest request) {
        return adminNewsFlashService.delNewsFlash(request);
    }

    /**
     * 修改快讯
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("updateNewsFlash")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgNewsFlashRequest.class)
    public JsonEntity updateNewsFlash(@RequestAttribute KgNewsFlashRequest request) {
        return adminNewsFlashService.updateNewsFlash(request);
    }

    /**
     * 获取快讯顶部栏目导航列表
     * @return
     */
    @ResponseBody
    @RequestMapping("getNewsFlashTopMenus")
    @BaseControllerNote(needCheckToken = false,needCheckParameter = false)
    public JsonEntity getNewsFlashTopMenus(){
        return adminNewsFlashService.getNewsFlashTopMenus();
    }

    @ResponseBody
    @RequestMapping("/getPushNewsFlashInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, isLogin = true)
    public JsonEntity getPushNewsFlashInfo() {
        return  adminNewsFlashService.getPushAticleInfo();
    }
}
