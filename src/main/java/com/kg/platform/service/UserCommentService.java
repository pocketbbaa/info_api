package com.kg.platform.service;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.CommentListResponse;
import com.kg.platform.model.response.CommentReplyResponse;
import com.kg.platform.model.response.CommentResponse;
import com.kg.platform.model.response.UserCommentResponse;

public interface UserCommentService {

    PageModel<UserCommentResponse> getCommentAll(UserCommentRequest request, PageModel<UserCommentResponse> page);

    PageModel<UserCommentResponse> getCommentArtAll(UserCommentRequest request, PageModel<UserCommentResponse> page);

    boolean addComment(UserCommentRequest request);

    boolean deleteComment(UserCommentRequest request);

    PageModel<UserCommentResponse> getOthersComment(UserCommentRequest request, PageModel<UserCommentResponse> page);

    /**
     * 资讯详情，评论列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<CommentResponse> getCommentList(UserCommentRequest request, PageModel<CommentResponse> page);

    /**
     * 根据articleID获取作者ID
     *
     * @param articleId
     * @return
     */
    Long getAuthorId(Long articleId);

    /**
     * 评论详情，子回复列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<CommentReplyResponse> getCommentReplyDetail(UserCommentRequest request, PageModel<CommentReplyResponse> page);

    /**
     * 发表子回复
     *
     * @param request
     * @param userId
     * @return
     */
    AppJsonEntity commentReply(UserCommentRequest request, String userId);

    /**
     * 用户删除评论
     *
     * @param request
     * @param userId
     * @return
     */
    AppJsonEntity delete(UserCommentRequest request, String userId);

    /**
     * 我的评论列表
     *
     * @param request
     * @param userId
     * @param page
     * @return
     */
    AppJsonEntity myCommentList(UserCommentRequest request, String userId, PageModel<CommentListResponse> page);

    /**
     * 他人评论列表
     *
     * @param userId
     * @param page
     * @return
     */
    AppJsonEntity userCommentList(String userId, PageModel<CommentListResponse> page);

    /**
     * 根据评论ID获取评论详情
     *
     * @param request
     * @return
     */
    AppJsonEntity getCommentDetail(UserCommentRequest request);
}
