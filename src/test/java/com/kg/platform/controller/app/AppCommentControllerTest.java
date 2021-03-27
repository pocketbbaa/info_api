package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.CommentListResponse;
import com.kg.platform.model.response.CommentReplyResponse;
import com.kg.platform.model.response.CommentResponse;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class AppCommentControllerTest extends BaseTest {

    @Autowired
    private AppCommentController appCommentController;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Test
    public void test1() {

        UserCommentRequest userCommentRequest = new UserCommentRequest();
        userCommentRequest.setArticleId("442332089929572352");
        userCommentRequest.setCurrentPage(1);
        userCommentRequest.setPageSize(10);

        PageModel<CommentResponse> page = new PageModel<>();

        JSONObject jsonEntity = appCommentController.getCommentList(userCommentRequest, page);

        System.err.println(JSON.toJSON(jsonEntity));
    }

    @Test
    public void getCommentReplyDetail() {
        UserCommentRequest request = new UserCommentRequest();
        PageModel<CommentReplyResponse> page = new PageModel<>();
        request.setCommentId("456134403983323136");
        request.setArticleId("502923671707459584");
        request.setCurrentPage(1);
        request.setPageSize(10);
        JSONObject appJsonEntity = appCommentController.getCommentReplyDetail(request, page);
        System.err.println(JSON.toJSON(appJsonEntity));
    }

    @Test
    public void getCommentDetail() {
        UserCommentRequest request = new UserCommentRequest();
        request.setCommentId("456134403983323136");
        JSONObject jsonObject = appCommentController.getCommentDetail(request);
        System.err.println(JSON.toJSON(jsonObject));
    }


    @Test
    public void commentReply() {
        UserCommentRequest request = new UserCommentRequest();
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("425957073499594752");

        request.setArticleId("509695888747270144");
        request.setCommentDesc("我的子回复我的子回复我的子回复我的子回复我的");
        request.setToUserId("434346240138944512");
        request.setParentCommentId("456134403983323136");

        System.err.println(request.getCommentDesc().length());

        AppJsonEntity jsonEntity = appCommentController.commentReply(request, kguser);
        System.err.println(JSON.toJSON(jsonEntity));
    }

    @Test
    public void delete() {
        UserCommentRequest request = new UserCommentRequest();
        UserkgResponse kguser = new UserkgResponse();
        request.setCommentId("3");
        request.setFloorState(2);
        kguser.setUserId("432940624954662914");
        AppJsonEntity jsonEntity = appCommentController.delete(request, kguser);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void myCommentList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("494253919090962432");
        UserCommentRequest request = new UserCommentRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        PageModel<CommentListResponse> page = new PageModel<>();
        JSONObject jsonObject = appCommentController.myCommentList(kguser, request, page);
        System.err.println(JSON.toJSONString(jsonObject));
    }

    @Test
    public void userCommentList() {
        UserCommentRequest request = new UserCommentRequest();
        request.setUserId("494253919090962432");
        request.setCurrentPage(1);
        request.setPageSize(20);
        PageModel<CommentListResponse> page = new PageModel<>();
        JSONObject jsonObject = appCommentController.userCommentList(request, page);
        System.err.println(JSON.toJSONString(jsonObject));
    }


}
