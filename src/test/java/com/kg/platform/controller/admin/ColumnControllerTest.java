package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.ColumnEditRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/8/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class ColumnControllerTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ColumnController columnController;
    @Test
    public void getColumnList() throws Exception {
        JsonEntity jsonEntity = columnController.getColumnList();
        logger.info("测试结果:{}", JSON.toJSONString(jsonEntity));
    }

    @Test
    public void deleteColumn(){
        ColumnEditRequest columnEditRequest = new ColumnEditRequest();
        columnEditRequest.setColumnId(250);
        System.out.println(JSON.toJSONString(columnController.deleteColumn(columnEditRequest)));
    }

    @Test
    public void addColumn(){
        ColumnEditRequest columnEditRequest = new ColumnEditRequest();
        columnEditRequest.setName("chen");
        columnEditRequest.setParentId(346);
        columnEditRequest.setUrlname("gfhewuws");
        columnEditRequest.setDisplayStatus(false);
        columnEditRequest.setNavigatorDisplay(0);
        columnController.addColumn(columnEditRequest,new SysUser());
    }

}