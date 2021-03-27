package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.admin.DataStatQueryRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataStatControllerTest extends BaseTest {


    @Autowired
    private DataStatController dataStatController;

    @Test
    public void test() {
        DataStatQueryRequest request = new DataStatQueryRequest();
        request.setStartDate(DateUtils.parseDate("2018-10-16 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        request.setEndDate(DateUtils.parseDate("2018-10-19 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        JsonEntity jsonEntity = dataStatController.getNormalUserList(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }


}
