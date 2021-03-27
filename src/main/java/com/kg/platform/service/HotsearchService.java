package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.response.HotsearchResponse;

public interface HotsearchService {

    Result<List<HotsearchResponse>> selectHotAll();

}
