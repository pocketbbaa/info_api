package com.kg.platform.dao.read;

import java.util.List;
import java.util.Set;

import com.kg.platform.dao.entity.UserProfile;
import com.kg.platform.model.in.UserProfileInModel;
import com.kg.platform.model.out.UserProfileOutModel;
import org.apache.ibatis.annotations.Param;

public interface UserProfileRMapper {

    UserProfileOutModel selectByPrimaryKey(Long userId);

    List<UserProfileOutModel> selectByuserprofileId(UserProfileInModel inModel);

    UserProfileOutModel bowsenumCount(UserProfileInModel inModel);

    List<UserProfileOutModel> getUserproFile();

    List<UserProfileOutModel> getAppUserproFile(UserProfileInModel inModel);

    List<UserProfileOutModel> getBowsnum(UserProfileInModel inModel);

    List<UserProfileOutModel> getAppBowsnum(UserProfileInModel inModel);

    // 统计评论
    UserProfileOutModel countComments(UserProfileInModel inModel);

    // 统计收藏
    UserProfileOutModel countCollect(UserProfileInModel inModel);

    int countArtsum(UserProfileInModel inModel);

    int countArticle(UserProfileInModel inModel);

    // 普通用户（弃用）
    UserProfileOutModel getPersonal(UserProfileInModel inModel);

    // 个人专栏（弃用）
    UserProfileOutModel getIndividual(UserProfileInModel inModel);

    // 获取全部信息
    UserProfileOutModel getMedia(UserProfileInModel inModel);

    // 企业（弃用）
    UserProfileOutModel getEnterprise(UserProfileInModel inModel);

    int checkProfile(UserProfileInModel inModel);

    // 他人主页
    UserProfileOutModel getByIdProfile(UserProfileInModel inModel);

    // 查询App个人中心信息
    UserProfileOutModel selectBaseInfoByPrimaryKey(Long userId);

    /**
     * 获取认证专栏身份
     *
     * @param userId
     * @return
     */
    String getIdentityByUserId(@Param("userId") Long userId);

    List<UserProfileOutModel> selectByuserprofileIdWithHotAuthor(UserProfileInModel inModel);

    /**
     * 根据专栏名称查询
     */
    Integer selectInfoByColumnName(String columnName);

    List<UserProfileOutModel> selectMoreUserProfileInfo(List<Long> userIds);
}
