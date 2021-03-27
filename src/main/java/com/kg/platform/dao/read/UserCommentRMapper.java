package com.kg.platform.dao.read;

import com.kg.platform.model.CommentCntModel;
import com.kg.platform.model.in.UserCommentInModel;
import com.kg.platform.model.out.CommentReplyOutModel;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.response.CommentReplyCount;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserCommentRMapper {

    UserCommentOutModel selectByPrimaryKey(Long commentId);

    List<UserCommentOutModel> getCommentAll(UserCommentInModel inModel);

    int getCommentCount(UserCommentInModel inModel);

    int getUserCommentCount(UserCommentInModel inModel);

    List<UserCommentOutModel> getCommentArtAll(UserCommentInModel inModel);

    int getCommentArtCount(UserCommentInModel inModel);

    List<UserCommentOutModel> getOthersComment(UserCommentInModel inModel);

    List<UserCommentOutModel> getUserCommentAll(UserCommentInModel inModel);

    List<UserCommentOutModel> getUserCommentForApp(UserCommentInModel inModel);

    List<UserCommentOutModel> getMoreUserArtCnt(List<Long> userIds);

    List<UserCommentOutModel> getUserByCommentCnt(@Param("userId") String userId, @Param("time") String time);

    /**
     * 获取评论时间范围用户ID
     *
     * @param startDay
     * @param endDay
     * @return
     */
    List<Long> getCommentUserIdsByTime(@Param("start") Date startDay, @Param("end") Date endDay);

    /**
     * 资讯详情主评论分页列表【1.3.5】
     *
     * @param articleId
     * @param start
     * @param limit
     * @return
     */
    List<UserCommentOutModel> getCommentList(@Param("articleId") Long articleId, @Param("start") int start, @Param("limit") int limit);

    /**
     * 获取子回复列表
     *
     * @param commentIds
     * @return
     */
    List<CommentReplyOutModel> getCommentReplyList(@Param("list") Set<Long> list, @Param("limit") int limit);

    /**
     * 获取子回复总数
     *
     * @param commentIds
     * @return
     */
    @MapKey("parentCommentId")
    Map<Long, CommentReplyCount> getCommentReplyCountMap(@Param("list") Set<Long> list);

    /**
     * 获取评论详情 子回复列表
     *
     * @param commentId
     * @param start
     * @param limit
     * @return
     */
    List<CommentReplyOutModel> getCommentReplyByTopCommentId(@Param("commentId") Long commentId, @Param("start") int start, @Param("limit") int limit);

    /**
     * 获取子回复总数
     *
     * @param commentId
     * @return
     */
    long getCommentReplyCountByTopCommentId(@Param("commentId") Long commentId);

    /**
     * 根据评论Id获取用户ID
     *
     * @param connentId
     * @return
     */
    Long getUserIdByCommentId(@Param("connentId") Long connentId);

    /**
     * 根据子评论ID获取用户ID
     *
     * @param connentId
     * @return
     */
    Long getUserIdByCommentReplyId(@Param("connentId") Long connentId);

    /**
     * 根据评论Id,逻辑删除ds
     *
     * @param connentId
     * @return
     */
    Integer updateByCommentId(@Param("connentId") Long connentId);

    /**
     * 根据子评论Id,逻辑删除ds
     *
     * @param connentId
     * @return
     */
    Integer updateByCommentReplyId(@Param("connentId") Long connentId);

    /**
     * 根据用户ID获取评论列表
     *
     * @param aLong
     * @param start
     * @param limit
     * @return
     */
    List<CommentReplyOutModel> getMyCommentListByUserId(@Param("userId") Long userId, @Param("start") int start, @Param("limit") int limit);

    /**
     * 根据用户ID获取评论列表
     *
     * @param aLong
     * @param start
     * @param limit
     * @return
     */
    List<CommentReplyOutModel> getCommentListByUserId(@Param("userId") Long userId, @Param("start") int start, @Param("limit") int limit);

    /**
     * 根据ID查询评论内容数据
     *
     * @param parentCommentIds
     * @return
     */
    @MapKey("commentId")
    Map<Long, CommentReplyOutModel> getCommentListByIds(@Param("list") Set<Long> parentCommentIds);

    /**
     * 我的评论列表总数
     *
     * @param aLong
     * @return
     */
    Long getMyCommentListByUserIdCount(@Param("userId") Long userId);

    /**
     * 我的评论列表总数
     *
     * @param aLong
     * @return
     */
    Long getCommentListByUserIdCount(@Param("userId") Long userId);

    /**
     * 根据ID获取评论信息
     *
     * @param commentId
     * @return
     */
    UserCommentOutModel getCommentById(@Param("commentId") Long commentId);

    /**
     * 根据资讯ID获取评论总数
     *
     * @param articleId
     * @return
     */
    Integer getCommentTotalCount(@Param("articleId") Long articleId);

    /**
     * 获取我的评论总数
     *
     * @param articleId
     * @return
     */
    Integer getCommentTotalCountByUserId(@Param("userId") Long userId);

    /**
     * 获取用户的评论总数
     *
     * @param userId
     * @return
     */
    Integer getAdminCommentTotalCountByUserId(@Param("userId") Long userId);

    /**
     * 根据ID获取子评论信息
     *
     * @param commentId
     * @return
     */
    CommentReplyOutModel getCommentReplyById(@Param("commentId") Long commentId);

    /**
     * 根据用户IDS获取评论总数
     *
     * @param userIdList
     * @return
     */
    @MapKey("userId")
    Map<Long, CommentCntModel> getCommentTotalCountByUserIds(@Param("list") Set<Long> userIdList);
}
