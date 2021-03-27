package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.DataStatQueryRequest;
import com.kg.platform.model.response.admin.DataStatQueryResponse;

public interface AdminDataStatRMapper {

    List<DataStatQueryResponse> selectNormalUser(DataStatQueryRequest request);

    List<DataStatQueryResponse> selectColumnUser(DataStatQueryRequest request);

    List<DataStatQueryResponse> selectNormalUserList(DataStatQueryRequest request);

    List<DataStatQueryResponse> selectColumnUserList(DataStatQueryRequest request);

}
