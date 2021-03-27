package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.request.KeywordsRequest;
import com.kg.platform.model.response.KeywordsResponse;

public interface KeywordsService {


    Result<List<KeywordsResponse>> getHotSearch(KeywordsRequest request);

    Result<List<KeywordsResponse>> getKeywordsAll(KeywordsRequest request);

}
