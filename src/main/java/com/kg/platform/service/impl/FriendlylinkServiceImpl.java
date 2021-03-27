package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.FriendlyLinkRMapper;
import com.kg.platform.model.in.FriendlyLinkInModel;
import com.kg.platform.model.out.FriendlyLinkOutModel;
import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;
import com.kg.platform.service.FriendlylinkService;

@Service
public class FriendlylinkServiceImpl implements FriendlylinkService {
    private static final Logger logger = LoggerFactory.getLogger(FriendlylinkServiceImpl.class);

    @Inject
    FriendlyLinkRMapper friendlyLinkRMapper;

    /**
     * 合作伙伴或者友链的获取
     */
    @Override
    public Result<List<FriendlyLinkResponse>> getFriendlyAll(FriendlyLinkRequest request) {
        logger.info("合作伙伴前端入参：{}", JSON.toJSONString(request));
        FriendlyLinkInModel inModel = new FriendlyLinkInModel();
        inModel.setSecondChannel(request.getSecondChannel());
        inModel.setType(request.getType());
        List<FriendlyLinkOutModel> linkOutModels = friendlyLinkRMapper.getFriendlyAll(inModel);
        List<FriendlyLinkResponse> listrResponse = new ArrayList<FriendlyLinkResponse>();
        for (FriendlyLinkOutModel friendlyLinkOutModel : linkOutModels) {
            FriendlyLinkResponse response = new FriendlyLinkResponse();
            response.setLinkAddress(friendlyLinkOutModel.getLinkAddress());
            response.setLinkId(friendlyLinkOutModel.getLinkId());
            response.setLinkIcon(friendlyLinkOutModel.getLinkIcon());
            response.setLinkName(friendlyLinkOutModel.getLinkName());
            listrResponse.add(response);
        }
        return new Result<>(listrResponse);
    }

}
