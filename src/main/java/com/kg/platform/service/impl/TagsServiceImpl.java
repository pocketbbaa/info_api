package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.TagsRMapper;
import com.kg.platform.model.in.TagsInModel;
import com.kg.platform.model.out.TagsOutModel;
import com.kg.platform.model.request.TagsRequest;
import com.kg.platform.model.response.TagsResponse;
import com.kg.platform.service.TagsService;

@Service
public class TagsServiceImpl implements TagsService {
    private static final Logger logger = LoggerFactory.getLogger(TagsServiceImpl.class);

    @Inject
    TagsRMapper tagsRMapper;

    @Override
    public TagsOutModel getTags(TagsRequest request) {
        logger.info("查询tag入参：{}", JSON.toJSONString(request));
        TagsInModel inModel = new TagsInModel();
        inModel.setTagName(request.getTagName());
        tagsRMapper.getTags(inModel);
        return null;
    }

    @Override
    public Result<List<TagsResponse>> listTages() {
        List<TagsOutModel> list = tagsRMapper.listTags();
        List<TagsResponse> responses = new ArrayList<TagsResponse>();
        for (TagsOutModel tagsOutModel : list) {
            TagsResponse response = new TagsResponse();
            response.setTagId(tagsOutModel.getTagId());
            response.setTagName(tagsOutModel.getTagName());
            responses.add(response);
        }
        return new Result<List<TagsResponse>>(responses);
    }

}
