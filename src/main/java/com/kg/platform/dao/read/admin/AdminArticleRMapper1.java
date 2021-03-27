package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.Article;

public interface AdminArticleRMapper1 {

    Article selectByPrimaryKey(Long articleId);

    Article selectPushInfo(Long articleId);

    Integer selectColumnId(Long articleId);

}
