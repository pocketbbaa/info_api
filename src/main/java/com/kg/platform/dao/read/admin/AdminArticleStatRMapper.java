package com.kg.platform.dao.read.admin;

import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.response.admin.ArticleStatQueryResponse;

public interface AdminArticleStatRMapper {

    ArticleStatQueryResponse selectAll(ArticleEditRequest request);

}
