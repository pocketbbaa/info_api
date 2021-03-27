package com.kg.platform.controller;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.enumeration.CommentAuditStatusEnum;
import com.kg.platform.enumeration.CommentStatusEnum;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.model.response.UserCommentResponse;
import com.kg.platform.service.SensitiveWordsService;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("userComment")
public class UserCommentController extends ApiBaseController {

    @Inject
    UserCommentService commentService;

    @Inject
    SiteinfoService siteinfoService;

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    /**
     * 他人主页评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getothersComment")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JsonEntity getOthersComment(@RequestAttribute UserCommentRequest request,
                                       PageModel<UserCommentResponse> page) {

        if (null != request.getUserId()) {
            page.setCurrentPage(request.getCurrentPage());
            page = commentService.getOthersComment(request, page);

            return JsonEntity.makeSuccessJsonEntity(page);

        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    /**
     * 我的评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCommentAll")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JsonEntity getCommentAll(@RequestAttribute UserCommentRequest request, PageModel<UserCommentResponse> page) {
        if (null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        page.setCurrentPage(request.getCurrentPage());
        page = commentService.getCommentAll(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());

    }

    /**
     * 文章评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCommentArtAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JsonEntity getCollectArtAll(@RequestAttribute UserCommentRequest request,
                                       PageModel<UserCommentResponse> page) {
        if (null == request.getArticleId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        page.setCurrentPage(request.getCurrentPage());
        if (request.getPageSize() > 0) {
            page.setPageSize(request.getPageSize());
        }
        page = commentService.getCommentArtAll(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());

    }

    /**
     * 保存评论
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("addComment")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JsonEntity addComment(@RequestAttribute UserCommentRequest request) {
        Result<SiteinfoResponse> response = siteinfoService.selectSiteInfo();

        if (response.getData().getCommentAudit().intValue() == CommentAuditStatusEnum.NEED.getStatus()) {
            request.setCommentStatus(CommentStatusEnum.WAIT.getStatus());
        } else {
            request.setCommentStatus(CommentStatusEnum.AUDIT.getStatus());
        }

        if (null == request.getCommentDesc() || request.getCommentDesc().length() > 500) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "评论内容不能超过500个字");
        }

        Result<SensitiveFilter> filterResult = sensitiveWordsService.getSensitiveFilter();
        if (CheckUtils.checkRetInfo(filterResult, true)) {
            SensitiveFilter filter = filterResult.getData();

            String words = filter.filter(request.getCommentDesc());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_COMMENT_ERROR.getCode(), words);
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        if (null == request.getUserId() || null == request.getArticleId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        boolean success = commentService.addComment(request);
        if (success) {
            if (request.getCommentStatus() == CommentStatusEnum.WAIT.getStatus()) {
                return JsonEntity.makeExceptionJsonEntity("10030", "评论成功，需要审核，请耐心等待");
            }
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "失败");

    }

    /**
     * 删除评论
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteComment")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public JsonEntity deleteComment(@RequestAttribute UserCommentRequest request) {

        if (null != request.getCommentId()) {

            commentService.deleteComment(request);

            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity("", "失败");
        }

    }

}
