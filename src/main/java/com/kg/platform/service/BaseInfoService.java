package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.request.BaseInfoRequest;
import com.kg.platform.model.response.BaseInfoResponse;

public interface BaseInfoService {

    Result<List<BaseInfoResponse>> getbaseinfoAll();

    Result<BaseInfoResponse> getbaseinfoType(BaseInfoRequest request);

}
