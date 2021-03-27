package com.kg.platform.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.response.UserCollectResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.UsercollectService;

@Controller
@RequestMapping("usercollect")
public class UsercollectController extends ApiBaseController {

    @Inject
    UsercollectService usercollectService;

    @Inject
    AccountService accountService;

    /**
     * 收藏列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCollectAll")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public JsonEntity getCollectAll(@RequestAttribute UserCollectRequest request, PageModel<UserCollectResponse> page) {
        page.setCurrentPage(request.getCurrentPage());
        page = usercollectService.getCollectAll(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());

    }

    /**
     * 取消收藏
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteCollect")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public JsonEntity deleteCollect(@RequestAttribute UserCollectRequest request, PageModel<UserCollectResponse> page) {
        // boolean success =
        usercollectService.deleteByPrimaryKey(request);
        // if (success) {
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage());
        // }
        // return JsonEntity.makeExceptionJsonEntity("", "取消收藏失败");

    }

    /**
     * 保存点赞收藏分享浏览
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("addCollect")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public JsonEntity addCollect(@RequestAttribute UserCollectRequest request) {
        Integer operType = request.getOperType();
        request.setSource(1);
        // 校验用户是否已点赞或已收藏
        if (operType != null) {
            if (operType == 1 || operType == 2) {
                Result<Integer> result = usercollectService.getCollectByUserIdAndArticleId(request);
                if (result != null) {
                    if (result.isOk()) {
                        String message = "你已点过赞";
                        if (result.getData() == 1) {
                            message = "你已收藏";
                        }
                        return JsonEntity.makeExceptionJsonEntity("", message);
                    }
                }
            }
        }
        BigDecimal success = usercollectService.addCollect(request);
        return JsonEntity.makeSuccessJsonEntity(success);

    }

}
