package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.response.admin.PostQueryResponse;

public interface AdminPostRMapper {

    List<PostQueryResponse> selectAll();

}
