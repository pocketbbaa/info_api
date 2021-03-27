package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.CommentQueryRequest;
import com.kg.platform.model.request.admin.ProfileVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class CommentControllerTest {

    @Autowired
    private CommentController commentController;

    @Autowired
    private ColumnController columnController;

    @Test
    public void deleteCommentBatch(){
        CommentQueryRequest request = new CommentQueryRequest();
        request.setCommentIds("9,10");
        JsonEntity jsonEntity = commentController.deleteCommentBatch(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void setRuleAttr(){
        ProfileVO profileVO = new ProfileVO();
        profileVO.setCnt(10);
        profileVO.setType("personage_column");
        columnController.setProfileAttr(profileVO);
    }

    @Test
    public void ghetRuleAttr(){
        System.out.println(JSON.toJSONString(columnController.getProfileAttr()));
    }


    @Test
    public void getCommentList(){
        CommentQueryRequest request = new CommentQueryRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        JsonEntity jsonEntity = commentController.getCommentList(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }
}
