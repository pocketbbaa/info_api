package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Article;
import com.kg.platform.model.in.ArticleInModel;
import org.apache.ibatis.annotations.Param;

public interface ArticleWMapper {
    int deleteByPrimaryKey(Long articleId);

    int insert(Article record);

    int insertSelective(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    int AddArticle(ArticleInModel inModel);

    int updateBySelective(ArticleInModel inModel);

    int updateArticle(ArticleInModel inModel);

    /**
     * 处理举报
     *
     * @param articleId
     * @return
     */
    Integer disposeReport(@Param("articleId") Long articleId);
}
