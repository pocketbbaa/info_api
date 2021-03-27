package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.KeywordQueryRequest;
import com.kg.platform.model.response.admin.KeywordQueryResponse;

public interface AdminKeywordRMapper {

    List<KeywordQueryResponse> selectByCondition(KeywordQueryRequest request);

    long selectCountByCondition(KeywordQueryRequest request);

}
