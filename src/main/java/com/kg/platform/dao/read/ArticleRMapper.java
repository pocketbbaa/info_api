package com.kg.platform.dao.read;

import java.math.BigInteger;
import java.util.*;

import com.kg.platform.common.easyexcel.ArticleExportExcel;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.CountArticleNumOutModel;
import com.kg.platform.model.response.CommentArticle;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ArticleRMapper {

    ArticleOutModel selectByPrimaryKey(Long articleId);

    List<ArticleOutModel> selectArticleAll(ArticleInModel inModel);

    long selectCountArticle(ArticleInModel inModel);

    List<ArticleOutModel> selectAdvisory();

    List<ArticleOutModel> selectTopArticle();

    ArticleOutModel selectByIdDetails(ArticleInModel inModel);

    /**
     * 获取资讯详情，不包含title，des，text
     *
     * @param inModel
     * @return
     */
    ArticleOutModel selectBaseDetailsForApp(ArticleInModel inModel);

    /**
     * 资讯详情，只包含title,des,text
     *
     * @param inModel
     * @return
     */
    ArticleOutModel selectTextDetailsForApp(ArticleInModel inModel);

    List<ArticleOutModel> selectByuserprofileId(ArticleInModel inModel);

    ArticleOutModel selectArticleBase(ArticleInModel inModel);

    // 前三条
    List<ArticleOutModel> selectColumnid(ArticleInModel inModel);

    // 后三条
    List<ArticleOutModel> selectTagname(ArticleInModel inModel);

    // 详情页下篇文章
    List<ArticleOutModel> UndeArticle(ArticleInModel inModel);

    // 上一篇文章
    List<ArticleOutModel> OnArticle(ArticleInModel inModel);

    // 用户下的所有文章
    List<ArticleOutModel> getUserArticleAll(ArticleInModel inModel);

    // 二级频道页右侧 热门资讯
    List<ArticleOutModel> getChannelAll(ArticleInModel inModel);

    // 频道页热门文章
    List<ArticleOutModel> getChannelArt(ArticleInModel inModel);

    // 根据tag获取文章
    List<ArticleOutModel> getArticleByTag(ArticleInModel inModel);

    long countChannelArt(ArticleInModel inModel);

    // 统计每个一级栏目下的文章总量
    List<CountArticleNumOutModel> countFirstColumnForMQ();

    // 统计每个二级栏目下的文章总量
    List<CountArticleNumOutModel> countSecondColumnForMQ();

    // 统计每个二级栏目下的文章总量
    List<CountArticleNumOutModel> countSecondColumnForMQ2();

    // 搜索文章
    List<ArticleOutModel> getSearchArticle(ArticleInModel inModel);

    // 搜索文章APP
    List<ArticleOutModel> getSearchArticleForApp(ArticleInModel inModel);

    // 统计搜索
    long SearchCount(ArticleInModel inModel);

    // 查文章作者
    ArticleOutModel getArticleCreateuser(ArticleInModel inModel);

    // 查询文章详情
    ArticleOutModel getArticleContent(ArticleInModel inModel);

    // 查询最新爬取的60条外部新闻标题
    List<ArticleOutModel> getOutterNews(Map<String, Object> map);

    /**
     * 获取推荐文章列表
     *
     * @param inModel
     * @return
     */
    List<ArticleOutModel> selectArticleAppRecommend(ArticleInModel inModel);

    /**
     * v1.0.1推荐文章
     *
     * @param cInModel
     * @return
     */
    List<ArticleOutModel> selectRecommend(ArticleInModel cInModel);

    /**
     * 查询原创文章
     *
     * @return
     */
    ArticleOutModel getCreateArticle(ArticleInModel inModel);

    /**
     * 热门文章总数
     *
     * @param inModel
     * @return
     */
    long selectCountArticleRecomm(ArticleInModel inModel);

    List<ArticleOutModel> selectArticleRecomm(ArticleInModel inModel);

    // 查询未冻结并且发布时间在7天内的文章
    ArticleOutModel checkArticleUnfrezee(ArticleInModel inModel);

    //3天内浏览量最多的前5个视频
    List<ArticleOutModel> hotVideoList();

    //百科栏目下最新的15篇文章
    List<ArticleOutModel> encyclopediaList();

    //APP底部视频tab的视频列表
    List<ArticleOutModel> getVideoArt(ArticleInModel inModel);

    long countVideoArt();

    List getMoreArticleInfo(List<Long> userIds);

    List<ArticleOutModel> getArticleForSitemap(ArticleInModel inModel);

    long countArticleForSitemap(ArticleInModel inModel);


    /**
     * 获取hashcode
     *
     * @param start
     * @param limit
     * @return
     */
    List<BigInteger> getArticleSimHashCode(@Param("start") int start, @Param("limit") int limit);

    /**
     * 获取hashcode总数
     *
     * @return
     */
    int getSimHashTotal();

    Long countColumnArticleCnt(@Param("columnId") Integer columnId);

    List<ArticleExportExcel> getArticleDetailByApprovedDay(@Param("start") Date start, @Param("end") Date end);

    /**
     * 获取文章显示状态
     *
     * @param id
     * @return
     */
    Integer getArticleDisplayStatus(@Param("id") Long id);

    /**
     * 查询评论列表文章信息
     *
     * @param articleIds
     * @return
     */
    @MapKey("articleId")
    Map<Long, CommentArticle> commentArticleList(@Param("list") Set<Long> list);
}
