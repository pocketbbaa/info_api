package com.kg.platform.controller;

import javax.inject.Inject;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.enumeration.DateEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.SiteinfoRequest;
import com.kg.platform.model.response.SiteBaseInfoResponse;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.SiteinfoService;

@Controller
@RequestMapping("siteinfo")
public class SiteinfoController extends ApiBaseController {

    @Inject
    SiteinfoService siteinfoService;

    /**
     * 站点基本信息
     * 
     * @return
     */
    @Cacheable(category = "/siteinfo/selecttdk", key = "selectSiteInfo", expire = 2, dateType = DateEnum.MINUTES)
    @ResponseBody
    @RequestMapping("selecttdk")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = SiteinfoRequest.class)
    public JsonEntity selectTdk() {
        Result<SiteinfoResponse> response = siteinfoService.selectSiteInfo();
        if (null != response) {
            SiteinfoResponse sres = response.getData();
            SiteBaseInfoResponse res = new SiteBaseInfoResponse();
            res.setContactEmail(sres.getContactEmail());
            res.setContactPhone(sres.getContactPhone());
            res.setSiteCopyright(sres.getSiteCopyright());
            res.setSiteDesc(sres.getSiteDesc());
            res.setSiteIcon(sres.getSiteIcon());
            res.setSiteKeyword(sres.getSiteKeyword());
            res.setSiteLicense(sres.getSiteLicense());
            res.setSiteLogo(sres.getSiteLogo());
            res.setSiteTitle(sres.getSiteTitle());

            return JsonEntity.makeSuccessJsonEntity(res);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());

    }

}
