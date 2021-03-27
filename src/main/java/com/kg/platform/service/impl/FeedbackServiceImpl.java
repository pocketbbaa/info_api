package com.kg.platform.service.impl;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.dao.write.FeedbackWMapper;
import com.kg.platform.model.in.FeedbackInModel;
import com.kg.platform.model.request.FeedbackRequest;
import com.kg.platform.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Inject
    FeedbackWMapper feedbackWMapper;

    @Inject
    private IDGen idGenerater;

    @Override
    public boolean addFeedback(FeedbackRequest request) {

        FeedbackInModel inModel = new FeedbackInModel();
        inModel.setFeedbackId(idGenerater.nextId());
        inModel.setFeedbackDetail(request.getFeedbackDetail());
        inModel.setFeedbackEmail(request.getFeedbackEmail());
        inModel.setFeedbackPhone(request.getFeedbackPhone());
        inModel.setFromUrl(request.getFromUrl());
        inModel.setCreateDate(new Date());
        inModel.setCreateUser(request.getCreateUser());
        inModel.setFeedbackStatus(false);
        return feedbackWMapper.insertFront(inModel) > 0;
    }

}
