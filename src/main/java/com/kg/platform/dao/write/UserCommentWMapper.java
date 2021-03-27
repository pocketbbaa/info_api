package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.UserComment;
import com.kg.platform.model.in.CommentReplyInModel;
import com.kg.platform.model.in.UserCommentInModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCommentWMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(UserComment record);

    int insertSelective(UserCommentInModel inModel);

    int updateByPrimaryKeySelective(UserComment record);

    int updateByPrimaryKey(UserComment record);

    int deleteByKey(UserCommentInModel inModel);

    int deleteByIds(@Param("ids") List<Long> ids);

    Integer addCommentReply(CommentReplyInModel inModel);

    /**
     * 更新评论显示状态
     *
     * @param commentId
     * @param displayStatus
     * @return
     */
    void updateDisplayStatusFroComment(@Param("commentId") Long commentId, @Param("displayStatus") Integer displayStatus);

    /**
     * 更新子评论显示状态
     *
     * @param commentId
     * @param displayStatus
     * @return
     */
    void updateDisplayStatusFroCommentReply(@Param("commentId") Long commentId, @Param("displayStatus") Integer displayStatus);

    /**
     * 更新子评论审核状态
     *
     * @param commentId
     * @param status
     * @param refuseReason
     * @return
     */
    Integer updateCommentStatusForCommentReply(@Param("commentId") Long commentId, @Param("status") Integer status, @Param("refuseReason") String refuseReason);

    /**
     * 批量更新评论显示状态
     *
     * @param commentIds
     */
    void updateDisplayStatusFroCommentBatch(@Param("list") List<Long> commentIds);

    /**
     * 批量更新子评论显示状态
     *
     * @param commentReplyIds
     */
    void updateDisplayStatusFroCommentReolyBatch(@Param("list") List<Long> commentReplyIds);
}
