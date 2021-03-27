package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.response.admin.BonusQueryResponse;

public interface AdminBonusRMapper {

    List<BonusQueryResponse> selectAll(ArticleEditRequest request);

}
