package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.response.admin.TreeQueryResponse;

public interface AdminTreeRMapper {

    List<TreeQueryResponse> selectColumn();

    List<TreeQueryResponse> selectAuth();

}
