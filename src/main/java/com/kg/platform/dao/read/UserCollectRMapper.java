package com.kg.platform.dao.read;

import java.util.HashMap;
import java.util.List;

import com.kg.platform.dao.entity.UserCollect;
import com.kg.platform.model.in.UserCollectInModel;
import com.kg.platform.model.out.UserCollectOutModel;
import org.apache.ibatis.annotations.Param;

public interface UserCollectRMapper {

//    UserCollect selectByPrimaryKey(Long collectId);

    List<UserCollectOutModel> getCollectAll(UserCollectInModel inModel);

    int getCollectCount(UserCollectInModel inModel);

    // 点赞
    int getgiveCount(UserCollectInModel inModel);

    // 收藏
    int getClosedCount(UserCollectInModel inModel);

    // 检查是否分享过
    int checkShareStatus(UserCollectInModel inModel);

    // 检查是否浏览过
    int checkBrowseStatus(UserCollectInModel inModel);

    /**
     * 查询用户是否收藏获取点赞
     *
     * @param userId
     * @param articleId
     * @param operType
     * @return
     */
    Integer getCollectByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId, @Param("operType") Integer operType);

    List<UserCollectOutModel> getMoreCollectInfo(List<Long> userIds);

    List<HashMap> selectOrderByFilter();

    List<UserCollectOutModel> getUserByCollectCnt(@Param("userId")String userId, @Param("time")String time);

    /**
     * 分页获取
     * @param inModel
     * @return
     */
    List<UserCollect> getByPage(UserCollectInModel inModel);
}
