package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.SiteimageResponse;
import com.kg.platform.service.SiteimageinService;

/**
 * 图片推荐位
 * 
 * @author think
 *
 */
@Controller
@RequestMapping("siteimageapi")
public class SiteimageController extends ApiBaseController {

    @Inject
    SiteimageinService siteimageinService;


    @ResponseBody
    @RequestMapping("/listsiteimage")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = SiteimageRequest.class)
    public JsonEntity listSiteimage(@RequestAttribute SiteimageRequest request) {
        logger.info(JSON.toJSONString(request) + request.getImage_pos() + request.getNavigator_pos()
                + request.getNavigator_pos());
        Result<List<SiteimageResponse>> rlist = siteimageinService.getAllColumn(request);
        if (rlist.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(rlist.getData());
        }
        return JsonEntity.makeSuccessJsonEntity("", "获取数据失败");

    }

}
