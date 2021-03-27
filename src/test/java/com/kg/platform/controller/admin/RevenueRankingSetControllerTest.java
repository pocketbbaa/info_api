package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.RevenueRankingSetRequest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RevenueRankingSetControllerTest extends BaseTest {

    @Autowired
    private RevenueRankingSetController revenueRankingSetController;

    @Test
    public void getSet() {

        JsonEntity json = revenueRankingSetController.getSet();
        System.out.println(JSONObject.toJSONString(json));
    }


    @Test
    public void set() {
        SysUser sysUser = new SysUser();
        sysUser.setSysUserName("chen");
        sysUser.setSysUserId(415641561561561L);
        sysUser.setUserRealname("xiaoming");
        RevenueRankingSetRequest request = new RevenueRankingSetRequest();
        request.setShow(0);
        JsonEntity json = revenueRankingSetController.updateSet(request,sysUser);
        System.out.println(JSONObject.toJSONString(json));
    }

}
