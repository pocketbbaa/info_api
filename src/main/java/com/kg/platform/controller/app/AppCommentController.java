package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.*;
import com.kg.platform.service.UserCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * 评论相关
 */
@Controller
@RequestMapping("/kgApp/comment")
public class AppCommentController {

    private static final Logger log = LoggerFactory.getLogger(AppCommentController.class);

    @Autowired
    private UserCommentService commentService;

    /**
     * 资讯详情，评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/getList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JSONObject getCommentList(@RequestAttribute UserCommentRequest request,
                                     PageModel<CommentResponse> page) {
        if (StringUtils.isEmpty(request.getArticleId())) {
            log.info("getList 入参有误 :" + JSON.toJSONString(request));
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = commentService.getCommentList(request, page);
        if (page != null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }

    /**
     * 根据评论ID获取评论详情
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCommentDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JSONObject getCommentDetail(@RequestAttribute UserCommentRequest request) {
        if (StringUtils.isEmpty(request.getCommentId())) {
            log.info("getCommentDetail 入参有误 :" + JSON.toJSONString(request));
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }
        return AppJsonEntity.jsonFromObject(commentService.getCommentDetail(request));
    }


    /**
     * 评论详情
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCommentReplyDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JSONObject getCommentReplyDetail(@RequestAttribute UserCommentRequest request,
                                            PageModel<CommentReplyResponse> page) {
        if (StringUtils.isEmpty(request.getCommentId()) || StringUtils.isEmpty(request.getArticleId())) {
            log.info("getCommentReplyDetail 入参有误 :" + JSON.toJSONString(request));
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = commentService.getCommentReplyDetail(request, page);
        if (page != null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));

    }

    /**
     * 发表子回复
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("commentReply")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public AppJsonEntity commentReply(@RequestAttribute UserCommentRequest request, @RequestAttribute UserkgResponse kguser) {
        return commentService.commentReply(request, kguser.getUserId());
    }

    /**
     * 删除评论
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public AppJsonEntity delete(@RequestAttribute UserCommentRequest request, @RequestAttribute UserkgResponse kguser) {
        return commentService.delete(request, kguser.getUserId());
    }


    /**
     * 我的评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("myCommentList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JSONObject myCommentList(@RequestAttribute UserkgResponse kguser, @RequestAttribute UserCommentRequest request,
                                    PageModel<CommentListResponse> page) {
        if (StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR));
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page.setData(new ArrayList<>());
        return AppJsonEntity.jsonFromObject(commentService.myCommentList(request, kguser.getUserId(), page));
    }

    /**
     * 他人评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("userCommentList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JSONObject userCommentList(@RequestAttribute UserCommentRequest request,
                                      PageModel<CommentListResponse> page) {
        if (request == null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }
        if (StringUtils.isEmpty(request.getUserId())) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR));
        }

        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page.setData(new ArrayList<>());
        return AppJsonEntity.jsonFromObject(commentService.userCommentList(request.getUserId(), page));
    }

}
