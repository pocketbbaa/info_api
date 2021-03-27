package com.kg.platform.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.KgArticleStatisticsRequest;
import com.kg.platform.service.KgArticleStatisticsService;

@Controller
@RequestMapping("kgartStat")
public class KgArtStatController extends ApiBaseController {

    @Inject
    KgArticleStatisticsService kgArticleStatisticsService;

    /**
     * 增加点赞收藏浏览转发数量加一
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("updateArticleStatist")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgArticleStatisticsRequest.class)
    public JsonEntity updateArticleStatist(@RequestAttribute KgArticleStatisticsRequest request) {
        boolean success = kgArticleStatisticsService.updateByPrimaryKeySelective(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "操作成功");

    }

}
