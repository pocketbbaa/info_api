package com.kg.platform.controller;


import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.model.request.SiteimageRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class SiteimageControllerTest {


    @Autowired
    SiteimageController siteimageController;

    @Test
    public void listSiteimage() {
        SiteimageRequest request=new SiteimageRequest();
        request.setImage_pos(11);
        request.setNavigator_pos(1);
        JsonEntity s = siteimageController.listSiteimage(request);
        System.out.println(">>>>>>>>>>>>>>>>>"+JsonUtil.writeValueAsString(s));
    }
}