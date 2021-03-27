package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.response.admin.BaseinfoQueryResponse;

public interface AdminBaseinfoRMapper {

    List<BaseinfoQueryResponse> selectAll();

}
