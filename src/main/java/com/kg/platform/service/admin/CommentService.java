package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.admin.CommentQueryRequest;
import com.kg.platform.model.response.admin.CommentQueryResponse;

public interface CommentService {

    PageModel<CommentQueryResponse> getCommentList(CommentQueryRequest request);

    boolean setDisplayStatus(CommentQueryRequest request);

    boolean auditComment(CommentQueryRequest request);

    boolean deleteComment(CommentQueryRequest request);

    boolean commentSet(CommentQueryRequest request);

    /**
     * 批量删除评论 逻辑删除
     *
     * @param request
     * @return
     */
    void deleteCommentBatch(CommentQueryRequest request) throws Exception;

}
