package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.admin.KeywordEditRequest;
import com.kg.platform.model.request.admin.KeywordQueryRequest;
import com.kg.platform.model.response.admin.KeywordQueryResponse;

public interface KeywordService {

    PageModel<KeywordQueryResponse> getKeywordList(KeywordQueryRequest request);

    boolean deleteKeyword(KeywordEditRequest request);

    boolean setOrder(KeywordEditRequest request);

    boolean setStatus(KeywordEditRequest request);

    boolean setChannel(KeywordEditRequest request);

    boolean addKeyword(KeywordEditRequest request);
}
