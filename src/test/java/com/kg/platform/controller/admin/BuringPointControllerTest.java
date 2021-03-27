package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.BuryingPointRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Administrator on 2018/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class BuringPointControllerTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BuryingPointController buryingPointController;

    @Test
    public void buryingPointListWithType() throws Exception {
        BuryingPointRequest request = new BuryingPointRequest();
        request.setDataType(1);
        request.setCurrentPage(1);
        logger.info(JSON.toJSONString(buryingPointController.buryingPointListWithType(request,new PageModel())));
    }

}

