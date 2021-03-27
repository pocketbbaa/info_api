package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.Feedback;

public interface FeedbackRMapper {

    Feedback selectByPrimaryKey(Long feedbackId);

}
