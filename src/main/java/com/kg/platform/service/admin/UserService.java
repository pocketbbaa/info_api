package com.kg.platform.service.admin;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.out.SiteinfoOutModel;
import com.kg.platform.model.out.UserColumnIdentityOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.admin.ChannelResponse;
import com.kg.platform.model.response.admin.UserCertQueryResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;

/**
 * 用户管理Service
 */
public interface UserService {

    PageModel<UserQueryResponse> getInviteUserList(UserQueryRequest request, PageModel<UserQueryResponse> page);

    boolean checkUser(String userId, Integer auditUserId, Integer inviteStatus);

    boolean freezeUser(String userId, Integer auditUserId, Integer bonusStatus, String freezeReason);

    boolean unBindUser(Long userId, Long relId);

    /**
     * 是否推荐为热门作者
     *
     * @param hotUser
     * @return
     */
    boolean setHotUser(Long userId, boolean hotUser, Integer rankingList);

    /**
     * 审核用户 判断用户角色，普通用户报错,不可设置 如果状态为审核通过，则不可设置
     * 正常情况：1、审核通过，改变审核状态，改变角色2、不通过，增加原因，改变审核状态
     *
     * @param userIdList
     * @param auditUserId
     * @param auditStatus
     * @param refuseReason
     * @return
     */
    Result<String> auditUser(List<Long> userIdList, Integer auditUserId, Integer auditStatus, String refuseReason);

    /**
     * 判断用户锁定状态，如果锁定状态和lockStatus相同报错 如果为锁定，则保存锁定时长和锁定单位
     *
     * @param userIdList
     * @param lockTime
     * @param lockUnit
     * @return
     */
    Result<String> lockUser(List<Long> userIdList, Long lockUserId, Integer lockTime, Integer lockUnit);

    /**
     * 获得用户基本信息
     *
     * @param userId
     * @return
     */
    UserQueryResponse getUserInfo(Long userId);

    UserQueryResponse getParentUserInfo(Long userId);

    /**
     * 用户登录状态保存时长
     *
     * @param request
     * @return
     */
    boolean loginSet(AdminLoginSetRequest request);

    boolean userInfoSet(AdminUserInfoSetRequest request);

    /**
     * 实名认证列表
     *
     * @param request
     * @return
     */
    PageModel<UserCertQueryResponse> getUserCert(UserCertQueryRequest request);

    /**
     * 实名认证审核
     *
     * @param request
     * @return
     */
    JsonEntity auditUserCert(UserCertEditRequest request);

    UserQueryResponse getUserId(UserQueryRequest request);

    SiteinfoOutModel getSiteInfo();

    /**
     * 查询用户信息
     *
     * @param inviteCode 邀请码
     * @return
     */
    UserkgOutModel selectByInviteCode(String inviteCode);

    /**
     * 查询用户详细信息
     *
     * @param
     * @return
     */
    UserkgOutModel getUserDetail(Long userId);

    /**
     * 获取用户专栏身份
     *
     * @return
     */
    List<UserColumnIdentityOutModel> getColumnIdentity();

    /**
     * 认证专栏
     *
     * @param request
     * @return
     */
    JsonEntity certificationColumn(CertificationColumnRequest request);

    /**
     * 取消专栏认证
     *
     * @param request
     * @return
     */
    JsonEntity cancelCertification(CertificationColumnRequest request);

    /**
     * 模糊查询作者列表
     *
     * @return
     */
    List<UserkgOutModel> getAuthorList(UserQueryRequest userQueryRequest);

    /**
     * 获取渠道
     *
     * @return
     */
    List<ChannelResponse> getChannel();

    /**
     * 更新专栏名称
     *
     * @param request
     * @return
     */
    JsonEntity updateColumnName(UserProfileRequest request);

    /**
     * 新版用户列表获取
     *
     * @param request
     * @return
     */
    JsonEntity getOptimizeUserList(UserQueryRequest request) throws Exception;

    /**
     * 设置用户排序
     *
     * @param request
     * @return
     */
    JsonEntity setUserOrder(UserQueryRequest request);

    /**
     * 获取用户资产信息
     *
     * @param request
     * @return
     */
    JsonEntity getUserAccountInfo(@RequestAttribute UserQueryRequest request);

    /**
     * 获取专栏等级列表
     *
     * @return
     */
    JsonEntity getColumnLevelList();

    /**
     * 修改专栏等级
     *
     * @param request
     * @return
     */
    JsonEntity updateColumnLevel(UserQueryRequest request);
}
