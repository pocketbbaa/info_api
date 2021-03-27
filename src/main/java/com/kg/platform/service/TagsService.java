package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.out.TagsOutModel;
import com.kg.platform.model.request.TagsRequest;
import com.kg.platform.model.response.TagsResponse;

public interface TagsService {

    TagsOutModel getTags(TagsRequest request);

    Result<List<TagsResponse>> listTages();

}
