package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.model.request.admin.DataStatQueryRequest;
import com.kg.platform.model.response.admin.DataStatQueryResponse;

public interface DataStatService {

    /**
     * 统计报表
     * 
     * @param request
     * @return
     */
    List<DataStatQueryResponse> getDataStatChart(DataStatQueryRequest request);

    List<DataStatQueryResponse> getNormalUserList(DataStatQueryRequest request);

    List<DataStatQueryResponse> getColumnUserList(DataStatQueryRequest request);

}
