package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.response.admin.BusinessTypeQueryResponse;

public interface AdminBusinessTypeRMapper {

    List<BusinessTypeQueryResponse> selectAll();

}
