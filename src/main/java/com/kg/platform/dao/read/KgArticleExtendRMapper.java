package com.kg.platform.dao.read;

import com.kg.platform.model.out.KgArticleExtendOutModel;

public interface KgArticleExtendRMapper {


    KgArticleExtendOutModel selectByPrimaryKey(Long articleId);

}