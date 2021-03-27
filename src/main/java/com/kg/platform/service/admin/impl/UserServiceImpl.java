package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.support.CacheService;
import com.kg.platform.common.utils.*;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.dao.entity.*;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.read.admin.AdminUserCertRMapper;
import com.kg.platform.dao.read.admin.AdminUserRMapper;
import com.kg.platform.dao.read.admin.KgRitRolloutRMapper;
import com.kg.platform.dao.read.admin.UserColumnIdentityRMapper;
import com.kg.platform.dao.write.*;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.CommentCntModel;
import com.kg.platform.model.in.UserCertInModel;
import com.kg.platform.model.in.UserConcernInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.in.UserProfileInModel;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.KgUserResponse;
import com.kg.platform.model.response.admin.ChannelResponse;
import com.kg.platform.model.response.admin.UserCertQueryResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;
import com.kg.platform.model.strategyUtil.BaseStrategyCentre;
import com.kg.platform.service.PushService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.UserService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.filter.Filter;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

@Service("AdminUserService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AdminUserRMapper adminUserRMapper;

    @Autowired
    private UserWMapper userWMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserActiveWMapper userActiveWMapper;

    @Autowired
    private UserActiveRMapper userActiveRMapper;

    @Autowired
    private UserProfileRMapper userProfileRMapper;

    @Autowired
    private UserProfileWMapper userProfileWMapper;

    @Autowired
    private SiteinfoWMapper siteinfoWMapper;

    @Autowired
    private AdminUserCertRMapper adminUserCertRMapper;

    @Autowired
    private SiteinfoRMapper siteinfoRMapper;

    @Inject
    private UserRelationWMapper userRelationWMapper;

    @Inject
    private UserRelationRMapper userRelationRMapper;

    @Inject
    private SendSms sendSms;

    @Inject
    private PropertyLoader propertyLoader;

    @Inject
    private TokenManager manager;

    @Autowired
    private UserColumnIdentityRMapper userColumnIdentityRMapper;

    @Autowired
    private UserCertRMapper userCertRMapper;

    @Autowired
    private RoleLevelRMapper roleLevelRMapper;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private UserCollectRMapper userCollectRMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private RoleRMapper roleRMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private SysUserRMapper sysUserRMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KgRitRolloutRMapper kgRitRolloutRMapper;

    @Autowired
    private UserConcernRMapper userConcernRMapper;

    @Autowired
    private PushService pushService;


    @Override
    public boolean unBindUser(Long userId, Long relUserId) {
        logger.info("取消关联入参：{},{}", userId, relUserId);
        UserRelation userRel = new UserRelation();
        userRel.setUserId(userId);
        userRel.setRelUserId(relUserId);
        int record = userRelationWMapper.deleteByUserAndRelUser(userRel);
        return record > 0;
    }

    /**
     * 构建注册来源
     *
     * @param user
     * @return
     */
    private String getRegisterOriginContent(UserQueryResponse user) {
        Integer registerOrigin = user.getRegisterOrigin();
        RegistOriginEnum registOriginEnum = RegistOriginEnum.getByCode(registerOrigin);
        //根据用ID查询是否活动注册
        UserRelation userRelation = userRelationRMapper.selectByRelUserId(Long.valueOf(user.getUserId()));
        if (userRelation == null || userRelation.getActivityId() == null) {
            return registOriginEnum.getMessage();
        }
        UserActivityEnum userActivityEnum = UserActivityEnum.getByCode(userRelation.getActivityId());
        if (userActivityEnum.equals(UserActivityEnum.WORLD_CUP) || userActivityEnum.equals(UserActivityEnum.ZHOU_CONCERT)) {
            return registOriginEnum.getMessage() + " " + userActivityEnum.getMessage();
        }
        return registOriginEnum.getMessage();
    }

    private String getRegisterOriginContent(Integer registerOrigin, UserRelation userRelation) {
        RegistOriginEnum registOriginEnum = RegistOriginEnum.getByCode(registerOrigin);

        if (userRelation == null || userRelation.getActivityId() == null) {
            return registOriginEnum.getMessage();
        }
        UserActivityEnum userActivityEnum = UserActivityEnum.getByCode(userRelation.getActivityId());
        if (userActivityEnum.equals(UserActivityEnum.WORLD_CUP) || userActivityEnum.equals(UserActivityEnum.ZHOU_CONCERT)) {
            return registOriginEnum.getMessage() + " " + userActivityEnum.getMessage();
        }
        return registOriginEnum.getMessage();
    }

    @Override
    public PageModel<UserQueryResponse> getInviteUserList(UserQueryRequest request, PageModel<UserQueryResponse> page) {
        request.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        request.setLimit(page.getPageSize());
        Date end = request.getCreateDateEnd();
        if (null != end) {
            // 设置时分秒
            request.setCreateDateEnd(DateUtils.getDateEnd(end));
        }
        List<UserQueryResponse> userList = adminUserRMapper.selectUserInvite(request);
        if (null != userList && userList.size() > 0) {
            userList.forEach(user -> {
                user.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(user.getUserRole())).getDisplay());
                user.setUserLevelDisplay(
                        UserLevelEnum.getUserLevelEnum(NumberUtils.intValue(user.getUserLevel())).getDisplay());
                user.setAuditStatusDisplay(
                        AuditStatusEnum.getAuditStatusEnum(NumberUtils.intValue(user.getAuditStatus())).getDisplay());
                user.setLockStatusDisplay(
                        LockStatusEnum.getLockStatusEnum(NumberUtils.intValue(user.getLockStatus())).getDisplay());
                user.setHotUserDisplay(null != user.getHotUser() && user.getHotUser() ? "是" : "否");
                String lastTime = user.getLastActiveTime();
                user.setLastActiveTime(StringUtils.isEmpty(lastTime) ? "" : lastTime);
            });
        }
        long count = adminUserRMapper.selectCountUserInvite(request);
        page.setData(userList);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public boolean setHotUser(Long userId, boolean hotUser, Integer rankingList) {
        User user = new User();
        user.setUserId(userId);
        user.setHotUser(hotUser);
        user.setRankingList(rankingList);
        int record = userWMapper.updateByPrimaryKeySelective(user);
        return record > 0;
    }

    @Override
    public boolean checkUser(String userId, Integer auditUserId, Integer inviteStatus) {
        UserActive userActive = new UserActive();
        userActive.setUserId(Long.parseLong(userId));
        userActive.setInviteStatus(inviteStatus);
        userActive.setCheckUser(auditUserId);
        userActive.setCheckDate(new Date());
        int record = userActiveWMapper.updateByPrimaryKeySelective(userActive);
        return record > 0;
    }

    @Override
    public boolean freezeUser(String userId, Integer auditUserId, Integer bonusStatus, String freezeReason) {
        UserActive userActive = new UserActive();
        userActive.setUserId(Long.parseLong(userId));
        userActive.setBonusStatus(bonusStatus);
        userActive.setBonusFreezeReason(freezeReason);
        userActive.setFreezeUser(auditUserId);
        userActive.setFreezeDate(new Date());
        int record = userActiveWMapper.updateByPrimaryKeySelective(userActive);
        return record > 0;
    }

    @Override
    @Transactional
    public Result<String> auditUser(List<Long> userIdList, Integer auditUserId, Integer auditStatus,
                                    String refuseReason) {
        Result<String> result = new Result<>();
        userIdList.stream().forEach(userId -> {
            UserkgOutModel user1 = userRMapper.selectByPrimaryKey(userId);
            UserRoleEnum userRole = UserRoleEnum
                    .getUserRoleEnum(NumberUtils.intValue(Integer.parseInt(user1.getApplyRole())));
            if (userRole == UserRoleEnum.NORMAL) {
                // result.setErrorMsg(ExceptionEnum.DATAERROR.getMessage());
            } else {
                AuditStatusEnum auditStatusEnum = AuditStatusEnum
                        .getAuditStatusEnum(NumberUtils.intValue(user1.getAuditStatus()));
                if (auditStatusEnum == AuditStatusEnum.PASS) {
                    // result.setErrorMsg(ExceptionEnum.DATAERROR.getMessage());
                } else {

                    auditStatusEnum = AuditStatusEnum.getAuditStatusEnum(NumberUtils.intValue(auditStatus));
                    User user = new User();

                    user.setUserId(userId);
                    user.setAuditStatus(auditStatus);
                    UserActive userActive = new UserActive();
                    userActive.setUserId(userId);
                    userActive.setAuditDate(new Date());
                    userActive.setUpdateDate(new Date());
                    userActive.setAuditUserid(auditUserId);
                    userActive.setAuditDate(new Date());

                    // 获取被审核的用户TOKEN，用于推送手机APP
                    String token = manager.getTokenByUserId(userId);
                    String messageText = "";
                    String title = "系统通知";
                    String tags = "";

                    if (auditStatusEnum == AuditStatusEnum.PASS) {
                        //app1.2.5屏蔽专栏作者审核通过奖励
                        //accountService.userColumnBonus(userId);

                        UserProfileOutModel upom = userProfileRMapper.selectByPrimaryKey(userId);
                        if (null != upom) {
                            user.setUserName(upom.getColumnName());

                            UserProfileInModel inModel = new UserProfileInModel();
                            inModel.setUserId(userId);
                            inModel.setAvatar(upom.getColumnAvatar()); // 专栏申请通过后，专栏头像覆盖用户头像
                            userProfileWMapper.updateProfile(inModel);
                        }

                        UserQueryResponse response = getParentUserInfo(userId);
                        if (null != response && response.getUserRole().intValue() == 1) {
                            // 师傅还未成为专栏作家前，徒弟先成为了专栏作家，此时师徒关系解除。
                            UserRelation userRel = new UserRelation();
                            userRel.setUserId(Long.parseLong(response.getUserId()));
                            userRel.setRelUserId(userId);
                            userRelationWMapper.deleteByUserAndRelUser(userRel);
                        }

                        user.setUserRole(userRole.getRole());
//                        sendSms.send(user1.getUserMobile(),
//                                "您申请成为" + UserRoleEnum.getUserRoleEnum(userRole.getRole()).getDisplay()
//                                        + "专栏用户，已通过审核,并获得" + Constants.USERCOLUMN_BONUS_TV + "TV奖励。请登录千氪，发布文章吧！");
                        String roleName = UserRoleEnum.getUserRoleEnum(userRole.getRole()).getDisplay();
                        String mess = "恭喜你,你申请的" + roleName + "专栏已审核通过，快去发布你的文章吧！";
                        sendSms.send(user1.getUserMobile(), mess);
                        messageText = mess;
                        tags = "passPersonalPage";

                    } else {
                        logger.info("专栏审核不通过时，未传拒绝通过原因时，验证审核原因重置为空");
                        userActive.setRefuseReason(refuseReason);
                        if (StringUtils.isEmpty(refuseReason)) {
                            userActive.setRefuseReason("");
                        }
                        sendSms.send(user1.getUserMobile(),
                                "您申请成为" + UserRoleEnum.getUserRoleEnum(userRole.getRole()).getDisplay()
                                        + "专栏用户，未通过审核。请登录千氪，重新提交资料。如有疑问，联系客服"
                                        + propertyLoader.getProperty("common", "global.CustomerServicePhone"));
                        messageText = "你申请的" + UserRoleEnum.getUserRoleEnum(userRole.getRole()).getDisplay()
                                + "专栏审核未通过";
                        if (StringUtils.isNotBlank(refuseReason)) {
                            messageText = messageText + "，原因：" + refuseReason;
                        }
                        tags = "noPassPersonalPage";
                    }
                    userWMapper.updateByPrimaryKeySelective(user);
                    userActiveWMapper.updateByPrimaryKeySelective(userActive);


                    pushService.ColumnApproval(title, messageText, userId.toString(), tags);


                }
            }
            result.setData("true");
        });
        return result;
    }

    @Override
    @Transactional
    public Result<String> lockUser(List<Long> userIdList, Long lockUserId, Integer lockTime, Integer lockUnit) {
        Result<String> result = new Result<>();
        userIdList.stream().forEach(userId -> {
            UserQueryResponse user1 = adminUserRMapper.selectById(userId);
            LockStatusEnum lockStatusEnum = LockStatusEnum
                    .getLockStatusEnum(NumberUtils.intValue(user1.getLockStatus()));
            User user = new User();
            user.setUserId(userId);
            user.setLockStatus(lockStatusEnum == LockStatusEnum.UNLOCK ? LockStatusEnum.LOCKED.getStatus()
                    : LockStatusEnum.UNLOCK.getStatus());
            UserActive userActive = new UserActive();
            userActive.setUserId(userId);
            userActive.setUpdateDate(new Date());
            userActive.setUpdateUserid(lockUserId);
            if (lockStatusEnum == LockStatusEnum.UNLOCK) {
                if (null != lockTime && null != lockUnit) {
                    userActive.setLockTime(lockTime);
                    userActive.setLockUnit(lockUnit);
                    userActive.setLockDate(new Date());
                    userWMapper.updateByPrimaryKeySelective(user);
                    userActiveWMapper.updateByPrimaryKeySelective(userActive);
                    result.setData("true");
                } else {
                    result.setData("false");
                }

            } else {
                userActive.setLockTime(null);
                userActive.setLockUnit(null);
                userActive.setLockDate(null);
                userWMapper.updateByPrimaryKeySelective(user);
                userActiveWMapper.updateByPrimaryKeySelective(userActive);
                result.setData("true");
            }
        });
        return result;
    }

    @Override
    public UserQueryResponse getUserInfo(Long userId) {
        UserQueryResponse response = adminUserRMapper.selectById(userId);
        if (null != response) {
            UserProfileOutModel profile = userProfileRMapper.selectByPrimaryKey(userId);
            response.setProfile(profile);
            response.setUserRoleDisplay(
                    UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(response.getUserRole())).getDisplay());
            response.setUserLevelDisplay(
                    UserLevelEnum.getUserLevelEnum(NumberUtils.intValue(response.getUserLevel())).getDisplay());
            response.setAuditStatusDisplay(
                    AuditStatusEnum.getAuditStatusEnum(NumberUtils.intValue(response.getAuditStatus())).getDisplay());
            response.setLockStatusDisplay(
                    LockStatusEnum.getLockStatusEnum(NumberUtils.intValue(response.getLockStatus())).getDisplay());
            response.setHotUserDisplay(null != response.getHotUser() && response.getHotUser() ? "是" : "否");

            // 添加头像 昵称返回 注册来源
            response.setNickName(profile.getUsername());
            response.setAvatar(profile.getAvatar());
            response.setRegisterOrigin(response.getRegisterOrigin());
            response.setRegisterOriginContent(getRegisterOriginContent(response));
            if (response.getUserRole() != 1) {
                response.setNickName(profile.getColumnName());
                response.setAvatar(profile.getColumnAvatar());
            }
            response = formatDate(response);
            //1.3.0版本新加字段
            response.setExistRollout(commonService.judgeRitRollout(userId) ? 1 : 0);
            //1.3.4版本新增字段专栏等级+粉丝数量
            UserConcernInModel inModel = new UserConcernInModel();
            inModel.setUserId(userId);
            long fansTotal = userConcernRMapper.getFansCount(inModel);
            response.setFansNum((int) fansTotal);
            Integer columnLevel = profile.getColumnLevel();
            if (columnLevel != null) {
                if (response.getUserRole() != 1) {
                    response.setColumnLevel(ColumnLevelEnum.getByCode(columnLevel).getDes());
                }
            }
        }
        response = buildActiveData(userId, response);
        return response;
    }

    /**
     * 构建浏览分享点赞浏览数据
     *
     * @param userId
     * @param response
     * @return
     */
    private UserQueryResponse buildActiveData(Long userId, UserQueryResponse response) {
        BasicDBObject query = new BasicDBObject("userId", userId);
        long countCollect = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), query);
        long countShare = MongoUtils.count(UserLogEnum.KG_USER_SHARE.getTable(), query);
        long countBrow = MongoUtils.count(UserLogEnum.KG_USER_BROWSE.getTable(), query);
        response.setCollectNum((int) countCollect);
        response.setBowseNum((int) countBrow);
        response.setShareNum((int) countShare);
        Integer commentCount = userCommentRMapper.getAdminCommentTotalCountByUserId(userId);
        response.setCommentNum(commentCount == null ? 0 : commentCount);
        return response;
    }

    /**
     * 日期格式化
     *
     * @param response
     * @return
     */
    private UserQueryResponse formatDate(UserQueryResponse response) {
        String createDate = response.getCreateDate();
        if (StringUtils.isNotEmpty(createDate)) {
            response.setCreateDate(DateUtils.formatDate(DateUtils.parseDate(createDate, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
        String applyColumnTime = response.getApplyColumnTime();
        if (StringUtils.isNotEmpty(applyColumnTime)) {
            response.setApplyColumnTime(DateUtils.formatDate(DateUtils.parseDate(applyColumnTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
        String auditDate = response.getAuditDate();
        if (StringUtils.isNotEmpty(auditDate)) {
            response.setAuditDate(DateUtils.formatDate(DateUtils.parseDate(auditDate, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
        String lastActiveTime = response.getLastActiveTime();
        if (StringUtils.isNotEmpty(lastActiveTime)) {
            response.setLastActiveTime(DateUtils.formatDate(DateUtils.parseDate(lastActiveTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
        return response;
    }

    public static void main(String[] args) {
        String a = "2018-07-27 10:29:22.0";
        Date aa = DateUtils.parseDate(a, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        System.out.println(DateUtils.formatDate(DateUtils.parseDate(a, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));

    }

    @Override
    public UserQueryResponse getParentUserInfo(Long userId) {
        UserQueryResponse response = adminUserRMapper.selectParentUserById(userId);
        if (null != response) {
            response.setUserRoleDisplay(
                    UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(response.getUserRole())).getDisplay());
        }
        return response;
    }

    @Override
    public boolean loginSet(AdminLoginSetRequest request) {
        Siteinfo siteinfo = new Siteinfo();
        siteinfo.setSiteId(1);
        siteinfo.setLoginTimeUnit(request.getUnit());
        siteinfo.setLoginTime(request.getTime());
        return siteinfoWMapper.updateByPrimaryKeySelective(siteinfo) > 0;
    }

    @Override
    public boolean userInfoSet(AdminUserInfoSetRequest request) {
        Siteinfo siteinfo = new Siteinfo();
        siteinfo.setSiteId(1);
        siteinfo.setPersonalInfo(request.getInfo());
        siteinfo.setPersonalInfoStatus(request.getStatus());
        return siteinfoWMapper.updateByPrimaryKeySelective(siteinfo) > 0;
    }

    @Override
    public PageModel<UserCertQueryResponse> getUserCert(UserCertQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
        }
        request.setStartDate(startDate);
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateEnd(endDate);
        }
        request.setEndDate(endDate);
        List<UserCertQueryResponse> data = adminUserCertRMapper.selectByCondition(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                item.setStatusDisplay(item.getStatus() == 0 ? "不通过"
                        : item.getStatus() == 1 ? "已认证" : item.getStatus() == 2 ? "审核中" : "");
            });
        }
        long count = adminUserCertRMapper.selectCountByCondition(request);
        PageModel<UserCertQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    @Transactional
    public JsonEntity auditUserCert(UserCertEditRequest request) {
        /*
         * List<Long> userIdList =
         * StringUtils.convertString2LongList(request.getUserIds(), ",");
         * userIdList.stream().forEach((Long id) -> {
         */
        Long id = request.getUserId();
        UserCertInModel userCertInModel = new UserCertInModel();
        userCertInModel.setUserId(id.toString());
        UserCertOutModel userCertOutModel = userCertRMapper.selectByUser(userCertInModel);
        if (request.getStatus() != null && request.getStatus() == 1 && userCertOutModel.getStatus() == 0) {
            // 此认证被拒绝通过然后再次审核通过时 判断重复
            UserCertInModel inModel = new UserCertInModel();
            inModel.setIdcardNo(request.getIdcardNo());
            long ifHave = userCertRMapper.selectByIdCardNo(inModel);
            if (ifHave > 0) {
                // 存在已通过或者审核中的记录，不能进行再次提交
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USERCERTREPEAT);
            }
        }
        request.setUserId(id);
        adminUserCertRMapper.auditUserCert(request);
        User user = new User();
        user.setUserId(request.getUserId());
        user.setRealnameAuthed(request.getStatus());
        userWMapper.updateByPrimaryKeySelective(user);

        String messageText = "";
        String token = manager.getTokenByUserId(id);
        String title = "系统通知";
        String tags = "";
        if (request.getStatus().intValue() == 0) {
            // 未通过
            messageText = "你的实名认证审核未通过";
            if (StringUtils.isNotBlank(request.getRefuseReason())) {
                messageText = messageText + "，原因：" + request.getRefuseReason();
            }
            tags = "noPassUserCert";

        }
        if (null != request.getStatus() && request.getStatus().intValue() == 1) {
            // boolean state = accountService.realnameBonus(request.getUserId());

//            if (state) {
//                // 通过
//                //messageText = "恭喜你！你的实名认证已审核通过,获得" + Constants.REALNAME_BONUS_TV + "TV奖励";
//                messageText = "恭喜你！你的实名认证已审核通过";
//            } else {
//                messageText = "恭喜你！你的实名认证已审核通过";
//            }
            messageText = "恭喜你！你的实名认证已审核通过";
            tags = "passUserCert";
        }


        pushService.auditUserCert(title, messageText, id.toString(), tags);

        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage());
    }

    @Override
    public UserQueryResponse getUserId(UserQueryRequest request) {
        List<UserQueryResponse> list = adminUserRMapper.selectUserId(request);
        if (null != list && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public SiteinfoOutModel getSiteInfo() {
        return siteinfoRMapper.selectByPrimaryKey(1);
    }

    @Override
    public UserkgOutModel selectByInviteCode(String inviteCode) {
        return userRMapper.selectByInviteCode(inviteCode);
    }

    @Override
    public UserkgOutModel getUserDetail(Long userId) {
        return userRMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<UserColumnIdentityOutModel> getColumnIdentity() {
        List<UserColumnIdentity> userColumnIdentities = userColumnIdentityRMapper.getAll();
        if (CollectionUtils.isEmpty(userColumnIdentities)) {
            return new ArrayList<>();
        }
        List<UserColumnIdentityOutModel> userColumnIdentityOutModelList = new ArrayList<>();
        for (UserColumnIdentity userColumnIdentity : userColumnIdentities) {
            UserColumnIdentityOutModel outModel = new UserColumnIdentityOutModel();
            BeanUtils.copyProperties(userColumnIdentity, outModel);
            userColumnIdentityOutModelList.add(outModel);
        }
        return userColumnIdentityOutModelList;
    }

    @Transactional
    @Override
    public JsonEntity certificationColumn(CertificationColumnRequest request) throws ApiException {

        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(Long.valueOf(request.getUserId()));
        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        if (userkgOutModel.getUserRole() == 1) {
            return JsonEntity.makeExceptionJsonEntity("", "用户是普通用户，不能认证专栏");
        }
        if (userkgOutModel.getColumnAuthed() == 1) {
            return JsonEntity.makeExceptionJsonEntity("", "用户已认证，不能重复认证");
        }
        Long userId = Long.valueOf(request.getUserId());
        // 更新专栏认证
        Integer count = userWMapper.updateColumnAuthed(userId, 1);
        if (count == null || count <= 0) {
            throw new ApiException();
        }
        // 更新专栏身份
        Integer update = userProfileWMapper.updateColumnAuthed(userId, request.getColumnIdentity());
        if (update == null || update <= 0) {
            throw new ApiException();
        }
        return JsonEntity.makeSuccessJsonEntity("认证成功");
    }

    @Transactional
    @Override
    public JsonEntity cancelCertification(CertificationColumnRequest request) throws ApiException {
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(Long.valueOf(request.getUserId()));
        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        if (userkgOutModel.getUserRole() == 1) {
            return JsonEntity.makeExceptionJsonEntity("", "用户是普通用户，不能认证专栏");
        }
        if (userkgOutModel.getColumnAuthed() == 0) {
            return JsonEntity.makeExceptionJsonEntity("", "用户还未认证专栏，取消失败");
        }
        Long userId = Long.valueOf(request.getUserId());
        // 更新专栏认证
        Integer count = userWMapper.updateColumnAuthed(userId, 0);
        if (count == null || count <= 0) {
            throw new ApiException();
        }
        // 更新专栏身份
        Integer update = userProfileWMapper.updateColumnAuthed(userId, "");
        if (update == null || update <= 0) {
            throw new ApiException();
        }
        return JsonEntity.makeSuccessJsonEntity("取消认证成功");
    }

    @Override
    public List<UserkgOutModel> getAuthorList(UserQueryRequest userQueryRequest) {
        UserInModel inmodel = new UserInModel();
        inmodel.setUserName(userQueryRequest.getUserName());
        return userRMapper.getAuthorList(inmodel);
    }

    @Override
    public List<ChannelResponse> getChannel() {
        return userRMapper.getChannel();
    }

    public JsonEntity updateColumnName(UserProfileRequest request) {

        UserProfile userProfile = new UserProfile();
        User user = new User();
        org.wltea.analyzer.result.Result<List<String>> wordsResult;
        try {
            wordsResult = Filter.doFilter(request.getColumnName());
        } catch (IOException e) {
            logger.error("【检测敏感词失败:e】" + e.getMessage());
            return new JsonEntity(ExceptionEnum.SERVERERROR);
        }
        if (wordsResult.getData() != null) {
            return new JsonEntity(ExceptionEnum.SENSITIVE_TAG_ERROR.getCode(), wordsResult.getMessage(), toStr(wordsResult.getData()));
        }

        //检测是否已被使用
        Integer isExist = userProfileRMapper.selectInfoByColumnName(request.getColumnName());
        if (isExist != 0) {
            return new JsonEntity(ExceptionEnum.MOBILE_USED);
        }
        userProfile.setUserId(request.getUserId());
        userProfile.setColumnName(request.getColumnName());

        user.setUserId(request.getUserId());
        user.setUserName(request.getColumnName());

        try {
            userProfileWMapper.updateByPrimaryKeySelective(userProfile);
            userWMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            logger.error("【update columnName fail e:" + e.getMessage() + "】");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVERERROR);
        }
        return JsonEntity.makeSuccessJsonEntity("更新专栏名称成功");
    }

    class TotalNumber {
        private Integer totalNumber = 0;

        public Integer getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(Integer totalNumber) {
            this.totalNumber = totalNumber;
        }
    }

    /**
     * 1 根据查询条件和分页 从kg_user获取基础数据
     * 2 内存中建立其他信息索引表
     * 3 根据userId获取详情
     * <p>
     * 用到的表  kg_user_active(分享数,浏览数) kg_user_profile(专栏名称 渠道来源)  kg_channel kg_user_relation
     *
     * @param request
     * @return
     */
    @Override
    public JsonEntity getOptimizeUserList(UserQueryRequest request) throws Exception {
        HashMap<String, Object> rows = new HashMap<>();
        rows.put("data", new ArrayList<>());
        if (request == null) {
            return JsonEntity.makeSuccessJsonEntity(rows);
        }
        Integer tempPage = request.getCurrentPage();
        //分页前置操作
        if (request.getCurrentPage() == 0) {
            request.setCurrentPage(1);
        }
        Integer page = (request.getCurrentPage() - 1) * (request.getPageSize());
        request.setCurrentPage(page);
        Long startTime = System.currentTimeMillis();
        TotalNumber totalNumber = new TotalNumber();
        //获取基础信息
        List<UserkgOutModel> filterUsers = getNewBaseUserInfo(request, totalNumber);

        if (filterUsers == null || filterUsers.size() == 0) {
            return JsonEntity.makeSuccessJsonEntity(rows);
        }
        Long time1 = System.currentTimeMillis();
        logger.info("【第一阶段： 查询基础信息耗时：(" + (time1 - startTime) + ")】");
        //获取所有的userId
        Set<Long> userIdList = parserAllUserId(filterUsers);
        ///////////////获取用户额外的基本信息///////////////////////////
        //查询关联表 获取用户其他信息 (kg_user_active)
        List<UserActive> activeList = userActiveRMapper.selectMoreUserIdInfo(new ArrayList<>(userIdList));
        //查询关联表 (kg_user_profile)
        List<UserProfileOutModel> profileList = userProfileRMapper.selectMoreUserProfileInfo(new ArrayList<>(userIdList));
        //查询关联表 (kg_user_relation)
        List<UserRelation> relationList = userRelationRMapper.selectMoreUserRelationInfo(new ArrayList<>(userIdList));
        //获取后台管理员账号列表  todo:应该设置缓存 静态数据不用实时查询
        List<SysUser> sysUserList = sysUserRMapper.selectAll();


        //解析出所有师傅id  获取师傅详情
        Set<Long> parentIds = parserAllParentId(relationList);
        List<UserkgOutModel> parentList = new ArrayList<>();
        if (parentIds.size() != 0) {
            //查询关联表(kg_user)获取师傅详情
            parentList = userRMapper.selectMoreParentInfo(new ArrayList<>(parentIds));
        }

        //////////////获取文章数 评论数 点赞数....

        //查询关联表(kg_user_comment)
        List<UserCommentOutModel> commentsList = userCommentRMapper.getMoreUserArtCnt(new ArrayList<>(userIdList));
        //查询关联表(kg_user_collect_log)
        List<UserCollectOutModel> collectsList = getUserCollectOutModelList(userIdList);
        //查询关联表(kg_article)
        List articleList = articleRMapper.getMoreArticleInfo(new ArrayList<>(userIdList));

        /////////////////////////////////////////////////
        //查询等级表
        HashMap<Integer, String> userLevelMap = queryLevelMap();
        //查询权限表
        HashMap<Integer, String> userRolMap = queryRoleMap();
        Long time2 = System.currentTimeMillis();
        logger.info("【第二阶段： 查询关联表耗时：(" + (time2 - time1) + ")】");
        //建立查询表结构
        HashMap<Long, Object> activeMap = commonService.transferFormat(activeList, "UserId");
        HashMap<Long, Object> profileMap = commonService.transferFormat(profileList, "UserId");
        HashMap<Long, Object> relationMap = commonService.transferFormat(relationList, "RelUserId");
        HashMap<Long, Object> sysUserMap = commonService.transferFormat(sysUserList, "SysUserId");//审核人信息
        HashMap<Long, Object> parentMap = commonService.transferFormat(parentList, "UserId");//师傅详情

        Map<Long, CommentCntModel> commentCntMap = userCommentRMapper.getCommentTotalCountByUserIds(userIdList); //评论数


        HashMap<Long, Integer> collectCntMap = transferToCnt(collectsList, "UserId");  //收藏数
        HashMap<Long, Integer> articleCntMap = parserUserArticleCnt(articleList);    //发文数

        //合并数据
        List<UserQueryResponse> data = mergeData(filterUsers, activeMap,
                profileMap, relationMap, userLevelMap,
                commentCntMap, collectCntMap, articleCntMap,
                userRolMap, parentMap, sysUserMap);
        Long time3 = System.currentTimeMillis();
        logger.info("【第三阶段： 数据解析组装耗时：(" + (time3 - time2) + ")】");
        rows.put("data", data);
        rows.put("pageSize", request.getPageSize());
        rows.put("currentPage", tempPage);
        rows.put("totalNumber", totalNumber.getTotalNumber());
        return JsonEntity.makeSuccessJsonEntity(rows);
    }

    private List<UserCollectOutModel> getUserCollectOutModelList(Set<Long> userIdList) {
        Long[] type = new Long[userIdList.size()];
        type = userIdList.toArray(type);
        BasicDBObject query = new BasicDBObject();
        query.put("userId", new BasicDBObject(Seach.IN.getOperStr(), type));
        MongoCursor<Document> cursor = MongoUtils.findByFilter(UserLogEnum.KG_USER_COLLECT.getTable(), query);
        List<UserCollectOutModel> list = new ArrayList<>();
        while (cursor.hasNext()) {
            UserCollectOutModel outModel = new UserCollectOutModel();
            Document doc = cursor.next();
            outModel.setCollectId(doc.getLong("collectId").toString());
            outModel.setUserId(doc.getLong("userId").toString());
            outModel.setArticleId(doc.getLong("articleId").toString());
            outModel.setCollectDate(doc.getDate("collectDate"));
            outModel.setSource(doc.getInteger("source"));
            list.add(outModel);
        }
        return list;
    }

    /**
     * 获取源数据
     * todo：这里的查询模式只适用于每页返回条数比较少、
     * <p>
     * 这里使用策略模式进行基础源查询 需要新增查询规则时.只需要新增一种策略器即可
     * <p>
     * 策略：
     * 1 普通查询
     * 2 使用师傅名称查询
     * 3 按照文章数排序查询
     * 4 按照点赞数排序
     * .....more
     */
    private List<UserkgOutModel> getNewBaseUserInfo(UserQueryRequest request, TotalNumber totalNumber) {
        //策略选择前置操作 编号选择
        Integer strategyNo = doParserStrategyNo(request);
        if (strategyNo == -1) {
            return null;
        }
        StrategyEnum strategyEnum = StrategyEnum.find(strategyNo, StrategyEnum.BASISQUERY);
        //选择执行器实例
        Class<? extends BaseStrategyCentre> constructor = strategyEnum.getFiledClassName();
        String desc = strategyEnum.getDesc();
        try {
            Constructor<? extends BaseStrategyCentre> con = constructor.getConstructor(UserQueryRequest.class);
            //反射获得构造实例
            BaseStrategyCentre baseStrategyCentre = makeStrategyExample(con, request, desc);
            if (baseStrategyCentre == null) {
                return null;
            }
            //执行
            baseStrategyCentre = baseStrategyCentre.queryStrategy();
            if (baseStrategyCentre == null) {
                return null;
            }
            //设置参数给返回值
            totalNumber.setTotalNumber(baseStrategyCentre.getTotalNumber());
            return baseStrategyCentre.getResultModel();

        } catch (Exception e) {
            logger.error("【在执行策略时 出现异常 :strategyNo:(" + strategyNo + ")】");
        }
        return null;
    }

    /**
     * 实例化策略器
     */
    private BaseStrategyCentre makeStrategyExample(Constructor<? extends BaseStrategyCentre> con, UserQueryRequest request, String desc) {
        BaseStrategyCentre baseStrategyCentre = null;
        try {
            //链式设置属性
            baseStrategyCentre = con.newInstance(request)
                    .setStrategyType(desc);
            baseStrategyCentre
                    .setCommonService(commonService)
                    .setUserRelationRMapper(userRelationRMapper)
                    .setUserRMapper(userRMapper)
                    .setArticleRMapper(articleRMapper)
                    .setUserCollectRMapper(userCollectRMapper)
                    .setUserCommentRMapper(userCommentRMapper);
        } catch (Exception e) {
            logger.error("【在执行策略时 出现异常 e:】" + e.getMessage());
        }
        return baseStrategyCentre;
    }

    private Integer doParserStrategyNo(UserQueryRequest request) {
        //不存在自定义排序的情况
        if (request.getOrderByClause() == null) {
            if (StringUtils.isEmpty(request.getMaster())) {
                return 1;                                       //基本查询方式
            } else {
                return 2;                                       //以师傅的条件查询
            }
        } else {
            String orderStr = request.getOrderByClause();
            String[] orderArray = orderStr.split(" ");
            String orderFileName = orderArray[0];
            if ("article_num".equals(orderFileName)) {
                return 3;                                       //发文方式排序
            } else if ("comment_num".equals(orderFileName)) {
                return 4;                                       //评论方式排序
            } else if ("collect_num".equals(orderFileName)) {
                return 5;                                       //收藏方式排序
            } else if ("share_num".equals(orderFileName)) {
                return 6;                                       //分享方式排序
            } else if ("audit_date".equals(orderFileName)) {
                request.setSort(orderArray[1]);
                return 7;                                       //审核时间排序
            }
        }
        return -1;
    }

    /**
     * 获取源数据
     * todo：这里的查询模式只适用于每页返回条数比较少的场景
     * 这里使用策略模式进行基础源查询
     * 策略：
     * 1 普通查询
     * 2 使用师傅名称查询
     * 3 按照文章数排序查询
     * 4 按照点赞数排序
     */
    private List<UserkgOutModel> getBaseUserInfo(UserQueryRequest request, TotalNumber totalNumber) {
        if (StringUtils.isEmpty(request.getMaster())) {
            totalNumber.setTotalNumber(userRMapper.selectFilterCnt(request));
            //如果师傅查询条件为空 则直接查询user表
            return userRMapper.getUserListByUserIds(request);
        } else {
            /**
             * 1 根据师傅字段去user表查出满足条件的师傅id
             * 2 然后根据师傅id列表去relation表查出满足条件的徒弟id列表
             * 3 根据徒弟id列表去user表获取最终数据列表
             * 根据mysql的特性 当子查询数据小时 使用in方式查询 反之则使用exists
             */
            //获取所有师傅userId
            List<Long> filterIds = userRMapper.selectParentFilterQuery(request);
            logger.debug("filterIds:" + JSON.toJSONString(filterIds));
            Set<Long> childUserIdSet = new HashSet<>(filterIds);
            //根据师傅userId获取满足条件的徒弟userId列表
            List<UserRelation> childUserIds = userRelationRMapper.selectMoreChildUserRelationInfo(new ArrayList<>(childUserIdSet));
            Set<Long> userIdSet;
            try {
                userIdSet = commonService.parserAllUserId(childUserIds, "RelUserId");
            } catch (Exception e) {
                logger.error("【解析集合{userId}属性失败 e:】" + e.getMessage());
                return null;
            }
            totalNumber.setTotalNumber(userIdSet.size());
            //根据徒弟id列表去user表获取最终数据列表
            //todo  这里进行第一层分页 只获取pageSize个徒弟id以供前端展示
            List<Long> childList = commonService.getPageSet(request.getCurrentPage(), request.getPageSize(), new ArrayList<Long>(userIdSet));
            if (childList.size() == 0) {
                return new ArrayList<>();
            }
            return userRMapper.selectMoreParentInfo(childList);
        }
    }

    @Override
    public JsonEntity setUserOrder(UserQueryRequest request) {
        User user = buildUserForSetUserOrder(request);
        int count = userWMapper.updateByPrimaryKeySelective(user);
        return count > 0 ? JsonEntity.makeSuccessJsonEntity() : JsonEntity.makeExceptionJsonEntity("", "设置失败");
    }

    @Override
    public JsonEntity getUserAccountInfo(UserQueryRequest request) {
        if (StringUtils.isBlank(request.getUserMobile())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        UserInModel inModel = new UserInModel();
        inModel.setUserMobile(request.getUserMobile());
        UserkgOutModel outModel = userRMapper.getUserAccountInfo(inModel);
        KgUserResponse response = new KgUserResponse();
        if (outModel != null) {
            response.setUserId(outModel.getUserId());
            response.setUserMobile(outModel.getUserMobile());
            UserRoleEnum userRoleEnum = UserRoleEnum.getUserRoleEnum(outModel.getUserRole());
            response.setUserRole(userRoleEnum.getRole());
            response.setUserRoleDisplay(userRoleEnum.getDisplay());
            response.setTxbBalance(outModel.getTxbBalance().stripTrailingZeros().toPlainString());
            response.setRitBalance(outModel.getRitBalance().stripTrailingZeros().toPlainString());
            response.setRitFrozenBalance(outModel.getRitFrozenBalance().stripTrailingZeros().toPlainString());
            KgRitRollout kgRitRollout = kgRitRolloutRMapper.selectByPrimaryUserType(outModel.getUserRole() == 1 ? 0 : 1);
            response.setFee(kgRitRollout.getRate().toString());
            return JsonEntity.makeSuccessJsonEntity(response);
        } else {
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.NO_DATA);
        }
    }

    @Override
    public JsonEntity getColumnLevelList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ColumnLevelEnum columnLevelEnum : ColumnLevelEnum.values()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", columnLevelEnum.getCode());
            map.put("level", columnLevelEnum.getDes());
            list.add(map);
        }
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @Override
    public JsonEntity updateColumnLevel(UserQueryRequest request) {
        if (StringUtils.isEmpty(request.getUserId()) || request.getColumnLevel() == null) {
            logger.info("传入参数有误：request.getUserId()：" + request.getUserId() + "  request.getColumnLevel()：" + request.getColumnLevel());
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        int success = userWMapper.updateColumnLevel(Long.valueOf(request.getUserId()), request.getColumnLevel());
        if (success > 0) {
            return JsonEntity.makeSuccessJsonEntity();
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
    }

    private User buildUserForSetUserOrder(UserQueryRequest request) {
        User user = new User();
        Integer userOrder = request.getUserOrder();
        userOrder = userOrder == null ? 0 : userOrder;
        user.setUserId(Long.valueOf(request.getUserId()));
        user.setUserOrder(userOrder);
        return user;
    }

    private HashMap<Long, Integer> parserUserArticleCnt(List articleList) {
        HashMap<Long, Integer> result = new HashMap<>();
        if (articleList == null) {
            return result;
        }
        HashMap userArticleMap = null;
        for (Object obj : articleList) {
            if (obj instanceof HashMap) {
                userArticleMap = (HashMap) obj;
            }
            if (userArticleMap == null) {
                continue;
            }
            result.put(Long.parseLong(userArticleMap.get("create_user") + ""), Integer.parseInt(userArticleMap.get("cnt") + ""));
        }
        return result;
    }

    private Set<Long> parserAllParentId(List<UserRelation> relationList) {
        Set<Long> result = new HashSet<>();
        if (relationList == null) {
            return result;
        }
        for (UserRelation userRelation : relationList) {
            result.add(userRelation.getUserId());
        }
        return result;
    }

    private HashMap<Integer, String> queryRoleMap() {
        HashMap<Integer, String> userLevelMap = new HashMap<>();
        List<Role> roles = roleRMapper.selectAllInfo();
        for (Role Role : roles) {
            userLevelMap.put(Role.getRoleId(), Role.getRoleName());
        }
        return userLevelMap;
    }

    private HashMap<Integer, String> queryLevelMap() {
        HashMap<Integer, String> userLevelMap = new HashMap<>();
        List<RoleLevel> roleLevels = roleLevelRMapper.selectAllInfo();
        for (RoleLevel roleLevel : roleLevels) {
            userLevelMap.put(roleLevel.getRoleId(), roleLevel.getLevelName());
        }
        return userLevelMap;
    }

    private List<UserQueryResponse> mergeData(List<UserkgOutModel> filterUsers, HashMap<Long, Object> activeMap,
                                              HashMap<Long, Object> profileMap, HashMap<Long, Object> relationMap,
                                              HashMap<Integer, String> userLevelMap, Map<Long, CommentCntModel> commentCntMap,
                                              HashMap<Long, Integer> collectCntMap, HashMap<Long, Integer> articleCntMap,
                                              HashMap<Integer, String> userRolMap, HashMap<Long, Object> parentMap, HashMap<Long, Object> sysUserMap) {

        List<UserQueryResponse> result = new ArrayList<>();
        UserQueryResponse response = null;
        Long userId;
        UserActive active;
        UserProfileOutModel profile;
        UserRelation userRelation;
        UserkgOutModel userkgOutModel = null;
        SysUser sysUser;
        for (UserkgOutModel model : filterUsers) {
            userId = Long.parseLong(model.getUserId());
            active = (UserActive) activeMap.get(userId);
            if (active == null) {
                active = new UserActive();
            }
            if (active.getAuditUserid() == null) {
                sysUser = new SysUser();
            } else {
                sysUser = (SysUser) sysUserMap.get(Long.parseLong(active.getAuditUserid() + ""));
            }
            profile = (UserProfileOutModel) profileMap.get(userId);
            if (profile == null) {
                profile = new UserProfileOutModel();
            }
            userRelation = (UserRelation) relationMap.get(userId);
            if (userRelation != null) {
                userkgOutModel = (UserkgOutModel) parentMap.get(userRelation.getUserId());
            } else {
                userkgOutModel = null;
            }
            try {
                response = fillData(model, userLevelMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response == null) {
                continue;
            }
            response.setUserRoleDisplay(userRolMap.get(model.getUserRole()));
            //填充active属性
            response.setShareNum(active.getShareNum());
            response.setBowseNum(active.getBowseNum());
            response.setAuditDate(DateUtils.formatDate(active.getAuditDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            response.setApplyColumnTime(DateUtils.formatDate(active.getApplyColumnTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            //填充profile属性
            response.setColumnName(profile.getColumnName());
            if (userkgOutModel == null) {
                response.setParentUser("无");
            } else {
                //填充师傅字段
                response.setParentUser(userkgOutModel.getUserName());
            }
            //专栏等级
            if (model.getUserRole() != 1) {
                if (model.getColumnLevel() != null) {
                    response.setColumnLevel(ColumnLevelEnum.getByCode(model.getColumnLevel()).getDes());
                }
            } else {
                response.setColumnLevel("");
            }
            //填充文章数
            response.setArticleNum(Integer.parseInt(getDefaultNULLValue(articleCntMap.get(userId), 0) + ""));
            //填充邀新状态字段
            response.setInviteStatus(active.getInviteStatus());
            //填充评论数
            CommentCntModel commentCntModel = commentCntMap.get(userId);
            Integer total = null;
            if (commentCntModel != null) {
                total = commentCntMap.get(userId).getTotal();
            }
            response.setCommentNum(Integer.parseInt(getDefaultNULLValue(total, 0) + ""));
            //填充收藏数
            response.setCollectNum(Integer.parseInt(getDefaultNULLValue(collectCntMap.get(userId), 0) + ""));
            //填充最后活动时间
            response.setLastActiveTime(DateUtils.formatDate(active.getLastloginTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            //填充审核人
            response.setAuditor(getDefaultNULLValue(sysUser.getSysUserName(), "") + "");
            //填充注册来源属性
            response.setRegisterOriginContent(getRegisterOriginContent(model.getRegisterOrigin(), userRelation));
            //填充渠道来源
            response.setChannel(getDefaultNULLValue(cacheService.getCacheChannelMapKey(profile.getRegistChannel()), "") + "");
            result.add(response);
        }
        return result;
    }

    private Object getDefaultNULLValue(Object obj, Object defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            return obj;
        }
    }

    private UserQueryResponse fillData(UserkgOutModel model, HashMap<Integer, String> userLevelMap) {
        UserQueryResponse response = new UserQueryResponse();
        Integer level = model.getUserLevel().intValue();
        //填充属性
        response.setUserEmail(model.getUserEmail());
        response.setUserMobile(model.getUserMobile());
        response.setUserLevel(level);
        response.setUserId(model.getUserId());
        response.setUserLevelDisplay(userLevelMap.get(level));
        response.setUserName(model.getUserName());
        response.setCreateDate(DateUtils.formatDate(model.getCreateDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD));
        response.setAuditStatus(model.getAuditStatus());
        response.setRankingList(model.getRankingList());
        response.setUserRole(model.getUserRole());
        if (model.getAuditStatus() == 0) {
            response.setAuditStatusDisplay("审核中");
        } else if (model.getAuditStatus() == 1) {
            response.setAuditStatusDisplay("通过");
        } else if (model.getAuditStatus() == 2) {
            response.setAuditStatusDisplay("未通过");
        }
        response.setLockStatus(model.getLockStatus());
        if (model.getLockStatus() == 1) {
            response.setLockStatusDisplay("未锁定");
        } else {
            response.setLockStatusDisplay("已锁定");
        }
        if (model.getIsHotUser() == 1) {
            response.setHotUserDisplay("是");
            response.setHotUser(true);
        } else {
            response.setHotUserDisplay("否");
            response.setHotUser(false);
        }
        response.setUserOrder(Long.valueOf(model.getUserOrder()));
        response.setInviteStatus(model.getInviteStatus());
        //填充注册来源
        response.setRegisterOrigin(model.getRegisterOrigin());
        response.setColumnAuthed(model.getColumnAuthed());
        return response;
    }

    private HashMap<Long, Integer> transferToCnt(List<?> list, String filedName) throws Exception {
        HashMap<Long, Integer> result = new HashMap<>();
        if (list == null || list.size() == 0) {
            return result;
        }
        Integer cnt;
        Long userId;
        for (Object obj : list) {
            Field[] field = obj.getClass().getDeclaredFields();
            Object name = commonService.getFieldName(field, filedName, obj);
            if (name == null) {
                continue;
            }
            userId = Long.parseLong(name + "");
            cnt = result.get(userId);
            if (cnt == null) {
                cnt = 0;
            }
            cnt++;
            result.put(userId, cnt);
        }
        return result;
    }

    private Set<Long> parserAllUserId(List<UserkgOutModel> filterUsers) {
        Set<Long> result = new HashSet<>();
        if (filterUsers == null || filterUsers.size() == 0) {
            return result;
        }

        for (UserkgOutModel userkgOutModel : filterUsers) {
            result.add(Long.parseLong(userkgOutModel.getUserId()));
        }
        return result;
    }

    private String toStr(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        Integer index = 0;
        for (String str : list) {
            result.append(str);
            if (index < list.size() - 1) {
                result.append(",");
            }
            index++;
        }
        return result.toString();
    }
}
