package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.in.PlainTextVO;
import com.kg.platform.model.request.ColumnRequest;
import com.kg.platform.model.response.ColumnResponse;

public interface ColumnService {

    Result<List<ColumnResponse>> getAllColumn(ColumnRequest Request);

    Result<List<ColumnResponse>> getSecondaryAll();

    Result<List<ColumnResponse>> getSecondColumn(ColumnRequest request);

    // 查栏目信息
    Result<ColumnResponse> selectSeo(ColumnRequest request);

    JsonEntity checkSensitiveWord(PlainTextVO request);
}
