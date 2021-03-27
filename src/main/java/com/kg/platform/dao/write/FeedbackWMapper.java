package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Feedback;
import com.kg.platform.model.in.FeedbackInModel;

public interface FeedbackWMapper {
    int deleteByPrimaryKey(Long feedbackId);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    int insertFront(FeedbackInModel inModel);

    int insertFrontForApp(FeedbackInModel inModel);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKey(Feedback record);

}
