package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.CommentBatchRequest;
import com.kg.platform.model.request.admin.CommentQueryRequest;
import com.kg.platform.model.response.admin.CommentQueryResponse;
import com.kg.platform.service.admin.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController("AdminCommentController")
@RequestMapping("admin/comment")
public class CommentController extends AdminBaseController {

    @Autowired
    private CommentService commentService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值

    }

    /**
     * 评论列表
     *
     * @param request
     * @return
     */
    @RequestMapping("getCommentList")
    @BaseControllerNote(beanClazz = CommentQueryRequest.class)
    public JsonEntity getCommentList(@RequestAttribute CommentQueryRequest request) {
        PageModel<CommentQueryResponse> pageModel = commentService.getCommentList(request);
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    /**
     * 评论显示设置
     *
     * @param request
     * @return
     */
    @RequestMapping("setDisplayStatus")
    @BaseControllerNote(beanClazz = CommentQueryRequest.class)
    public JsonEntity setDisplayStatus(@RequestAttribute CommentQueryRequest request) {
        if (request.getCommentId() != null && null != request.getDisplayStatus() && request.getFloorState() > 0) {
            boolean success = commentService.setDisplayStatus(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    /**
     * 审核评论
     *
     * @param request
     * @return
     */
    @RequestMapping("auditComment")
    @BaseControllerNote(beanClazz = CommentQueryRequest.class)
    public JsonEntity auditComment(@RequestAttribute CommentQueryRequest request) {
        logger.info("[审核评论] 入参 ： " + JSONObject.toJSONString(request));
        if (null != request.getCommentId() && null != request.getStatus() && request.getFloorState() > 0) {
            boolean success = commentService.auditComment(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    /**
     * 删除评论
     *
     * @param request
     * @return
     */
    @BaseControllerNote(beanClazz = CommentQueryRequest.class)
    @RequestMapping("deleteComment")
    public JsonEntity deleteComment(@RequestAttribute CommentQueryRequest request) {
        if (null != request.getCommentId()) {
            boolean success = commentService.deleteComment(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    /**
     * 批量删除评论
     *
     * @param request
     * @return
     */
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = CommentQueryRequest.class)
    @RequestMapping("deleteCommentBatch")
    public JsonEntity deleteCommentBatch(@RequestAttribute CommentQueryRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        try {
            commentService.deleteCommentBatch(request);
        } catch (Exception e) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
    }

    @BaseControllerNote(beanClazz = CommentQueryRequest.class)
    @RequestMapping("commentSet")
    public JsonEntity commentSet(@RequestAttribute CommentQueryRequest request) {
        if (null != request.getCommentSet()) {
            boolean success = commentService.commentSet(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

}
