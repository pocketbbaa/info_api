package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.model.request.admin.AdminPostEditRequest;
import com.kg.platform.model.response.admin.PostQueryResponse;
import com.kg.platform.model.response.admin.TreeQueryResponse;

public interface PostService {

    List<TreeQueryResponse> getAuthTree();

    boolean addPost(AdminPostEditRequest request);

    List<PostQueryResponse> getPostList();

    boolean setStatus(AdminPostEditRequest request);

}
