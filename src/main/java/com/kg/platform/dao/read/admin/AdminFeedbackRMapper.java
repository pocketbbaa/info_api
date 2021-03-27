package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.FeedbackQueryRequest;
import com.kg.platform.model.response.admin.FeedbackQueryResponse;

public interface AdminFeedbackRMapper {

    List<FeedbackQueryResponse> selectByCondition(FeedbackQueryRequest request);

    long selectCountByCondition(FeedbackQueryRequest request);

}
