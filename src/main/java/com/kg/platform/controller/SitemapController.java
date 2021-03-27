package com.kg.platform.controller;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.service.SiteMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/8/23.
 */
@Controller
public class SitemapController {

    @Autowired
    private SiteMapService siteMapService;

    @RequestMapping("/sitemap{number}.xml")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, isLogin = true)
    public void test(HttpServletResponse response, @PathVariable Integer number){
        siteMapService.getSitemapXml(response,number);
    }
}
