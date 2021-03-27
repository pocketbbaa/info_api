package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.admin.FeedbackQueryRequest;
import com.kg.platform.model.response.admin.FeedbackQueryResponse;

public interface FeedbackService {

    PageModel<FeedbackQueryResponse> getFeedbackList(FeedbackQueryRequest request);

    boolean deleteFeedback(FeedbackQueryRequest request);

    boolean setStatus(FeedbackQueryRequest request);

    boolean sendEmail(FeedbackQueryRequest request);

    boolean replay(FeedbackQueryRequest request);
}
