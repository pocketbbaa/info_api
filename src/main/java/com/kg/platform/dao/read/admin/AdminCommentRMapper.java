package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.CommentQueryRequest;
import com.kg.platform.model.response.admin.CommentQueryResponse;

public interface AdminCommentRMapper {

    List<CommentQueryResponse> selectByCondition(CommentQueryRequest request);

    long selectCountByCondition(CommentQueryRequest request);

    CommentQueryResponse selectCommentInfo(CommentQueryRequest request);

}
