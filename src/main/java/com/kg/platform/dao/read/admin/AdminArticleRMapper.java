package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.Article;
import com.kg.platform.model.request.admin.ArticleQueryRequest;
import com.kg.platform.model.response.admin.AdminArticleReportByArticleIdResponse;
import com.kg.platform.model.response.admin.AdminArticleReportResponse;
import com.kg.platform.model.response.admin.ArticleQueryResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminArticleRMapper {

    List<ArticleQueryResponse> selectByCondition(ArticleQueryRequest request);

    long selectCountByCondition(ArticleQueryRequest request);

    Article selectByPrimaryKey(Long articleId);

    List<ArticleQueryResponse> selectAutoPublishArticle();

    Double selectBonusValue(Long articleId);

    Integer getArticleAddedTvBonus(Long articleId);

    Integer getArticleAddedTxbBonus(Long articleId);

    ArticleQueryResponse getArticle(ArticleQueryRequest request);


    List<ArticleQueryResponse> getAllArticles(ArticleQueryRequest request);

    /**
     * 举报列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<AdminArticleReportResponse> toAuditList(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 举报列表数量
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Integer toAuditListCount(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 资讯编辑举报列表
     *
     * @param articleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<AdminArticleReportByArticleIdResponse> toAuditListByArticleId(@Param("articleId") Long articleId, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 资讯编辑举报列表数量
     *
     * @param articleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Integer toAuditListByArticleIdCount(@Param("articleId") Long articleId, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);


}
