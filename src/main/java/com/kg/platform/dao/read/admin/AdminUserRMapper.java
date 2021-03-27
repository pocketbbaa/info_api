package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.BonusQueryRequest;
import com.kg.platform.model.request.admin.PublishBonusQueryRequest;
import com.kg.platform.model.request.admin.ShareBonusQueryRequest;
import com.kg.platform.model.request.admin.UserBonusQueryRequest;
import com.kg.platform.model.request.admin.UserQueryRequest;
import com.kg.platform.model.response.admin.InviteBonusQueryResponse;
import com.kg.platform.model.response.admin.PublishBonusQueryResponse;
import com.kg.platform.model.response.admin.RealnameQueryResponse;
import com.kg.platform.model.response.admin.ShareQueryResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;

public interface AdminUserRMapper {

    UserQueryResponse selectById(Long userId);

    UserQueryResponse selectParentUserById(Long userId);

    List<UserQueryResponse> selectUserId(UserQueryRequest request);

    List<InviteBonusQueryResponse> selectUserInviteByCondition(BonusQueryRequest request);

    Long selectUserInviteCountByCondition(BonusQueryRequest request);

    List<RealnameQueryResponse> selectRealnameByCondition(BonusQueryRequest request);

    Long selectRealnameCountByCondition(BonusQueryRequest request);

    List<UserQueryResponse> selectUserInvite(UserQueryRequest request);

    Long selectCountUserInvite(UserQueryRequest request);

    /**
     * 发文奖励列表
     * 
     * @param request
     * @return
     */
    List<PublishBonusQueryResponse> getPublishBonusList(PublishBonusQueryRequest request);

    /**
     * 发文奖励总条数
     * 
     * @param request
     * @return
     */
    Long selectPublishBonusCountByCondition(PublishBonusQueryRequest request);

    /**
     * 发文奖励总支出
     * 
     * @param request
     * @return
     */
    PublishBonusQueryResponse getTotalPublishBonus();

    /**
     * 分享文章奖励列表
     * 
     * @param request
     * @return
     */
    List<ShareQueryResponse> getShareBonusList(ShareBonusQueryRequest request);

    /**
     * 分享文章奖励列表条数
     * 
     * @param request
     * @return
     */
    Long shareBonusCountByCondition(ShareBonusQueryRequest request);

    /**
     * 根据手机号查询用户列表信息
     * 
     * @param request
     * @return
     */
    List<UserQueryResponse> getUserListByMobile(UserBonusQueryRequest request);

}
