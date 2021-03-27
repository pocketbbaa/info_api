package com.kg.platform.dao.read;

import com.kg.platform.common.easyexcel.AsverageUserExportExcel;
import com.kg.platform.common.easyexcel.AuthorExportExcel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserCertEditRequest;
import com.kg.platform.model.request.admin.UserQueryRequest;
import com.kg.platform.model.response.CommentUser;
import com.kg.platform.model.response.admin.ChannelResponse;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserRMapper {

    UserkgOutModel selectByPrimaryKey(Long userId);

    UserkgOutModel selectByInviteCode(String inviteCode);

    int checkMobile(UserInModel model);

    int checkEmail(UserInModel model);

    int checkInviteCode(UserInModel model);

    UserkgOutModel getUserInfo(UserInModel model);

    UserkgOutModel selectByPrimary(UserInModel model);

    UserkgOutModel selectByPrimaryForModifyPwd(UserInModel model);

    UserkgOutModel selectUser(UserInModel model);

    UserkgOutModel getUserDetails(Long userId);

    UserkgOutModel selectApply(UserInModel model);

    UserkgOutModel selectByUser(UserInModel model);

    /**
     * 检查手机是否已经绑定过第三方账号
     *
     * @param model
     * @return
     */
    int checkThirdPartyPhone(UserInModel model);

    /**
     * 通过openID获取用户信息
     *
     * @param model
     * @return
     */
    UserkgOutModel getUserInfoByOpenId(UserInModel model);

    int checkUserIsLock(UserInModel userInModel);

    /**
     * 查询用户头像邀请码昵称信息
     *
     * @param model
     * @return
     */
    UserkgOutModel getUserProfiles(UserInModel model);

    /**
     * 查询用户头像邀请码昵称信息
     *
     * @param model
     * @return
     */
    Boolean checkNameAndIDCard(UserCertEditRequest request);

    List<UserkgOutModel> getAuthorList(UserInModel model);

    /**
     * 获取渠道
     *
     * @return
     */
    List<ChannelResponse> getChannel();

    /**
     * 根据userIds查询所有人
     *
     * @param request
     * @return
     */
    List<UserkgOutModel> getUserListByUserIds(UserQueryRequest request);

    Integer selectFilterCnt(UserQueryRequest request);

    List<UserkgOutModel> selectMoreParentInfo(List<Long> list);

    List<Long> selectParentFilterQuery(UserQueryRequest request);

    List<UserkgOutModel> selectOrderArticleByFilter(UserQueryRequest request);

    List<UserkgOutModel> selectOrderCommentByFilter(UserQueryRequest request);

    List<UserkgOutModel> selectOrderCollectByFilter(UserQueryRequest request);

    List<UserkgOutModel> selectOrderShareByFilter(UserQueryRequest request);

    List<UserkgOutModel> getAuthorListForSitemap(UserInModel inModel);

    long countAuthorListForSitemap();

    UserkgOutModel getUserAccountInfo(UserInModel inModel);

    List<AsverageUserExportExcel> getExportList(@Param("list") List<Long> useIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 发文行为用户
     *
     * @param startDay
     * @param endDay
     * @return
     */
    List<Long> getPostUserIds(@Param("start") Date startDay, @Param("end") Date endDay);

    /**
     * 新增粉丝行为用户
     *
     * @param startDay
     * @param endDay
     * @return
     */
    List<Long> getNewFansUserIds(@Param("start") Date startDay, @Param("end") Date endDay);

    /**
     * 等行为的用户
     *
     * @param startDay
     * @param endDay
     * @return
     */
    List<Long> getLoginUserIds(@Param("start") Date startDay, @Param("end") Date endDay);

    List<AuthorExportExcel> getAuthorExportExcelList(@Param("list") List<Long> userIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取用户已发文数
     *
     * @param userId
     * @param articleId 待过滤的资讯ID
     * @return
     */
    Integer postCount(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 获取用户已发文数
     *
     * @param userId
     * @param aIds
     * @return
     */
    Integer postCountForBatch(@Param("userId") Long userId, @Param("list") List<Long> list);

    /**
     * 获取评论作者信息
     *
     * @param userIds
     * @return
     */
    @MapKey("userId")
    Map<Long, CommentUser> commentUserList(@Param("list") List<Long> list, @Param("authorId") Long authorId);

    /**
     * 根据ID获取评论用户信息
     *
     * @param userId
     * @return
     */
    CommentUser getCommentUserByUserId(@Param("userId") Long userId);
}
