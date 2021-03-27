package com.kg.platform.model.response.admin;

import com.kg.platform.model.out.UserProfileOutModel;

/**
 * 用户管理查询出参：
 * 用户ID、用户名、邮箱、手机号、注册时间、角色、级别、最后活动时间、发文量、评论量、收藏量、分享量、审核状态、审核人、审核时间、锁定状态、是否推荐、排序
 */
public class UserQueryResponse extends AdminBaseResponse {

    /**
     *
     */
    private static final long serialVersionUID = 8632550460929176918L;

    private String userId;

    private String userName;

    private String userEmail;

    private String columnName; //专栏名称

    private String userMobile;

    private String createDate;

    private Integer userRole;

    private String userRoleDisplay;

    private Integer userLevel;

    private String userLevelDisplay;

    private String applyColumnTime;

    private String lastActiveTime;// 最后活动时间（目前是最后登录时间）

    private Integer articleNum;

    private Integer commentNum;

    private Integer collectNum;

    private Integer shareNum;

    private Integer auditStatus;

    private String auditStatusDisplay;

    private String auditor;

    private String auditDate;

    private Integer lockStatus;

    private String lockStatusDisplay;

    private Boolean hotUser;

    private String hotUserDisplay;

    private Long userOrder;

    private Integer bowseNum;

    private Integer applyRole;

    private String relTime; // 师徒关系建立时间

    private String parentUser; // 师傅

    private Integer inviteStatus;

    private Integer bonusStatus;

    private Integer registerOrigin; // 注册来源

    private String registerOriginContent; //注册来源内容

    private String channel; //注册渠道来源

    private Integer columnAuthed; // 专栏是否认证 0 未认证 1 已认证

    private String columnIdentity; // 专栏身份

    private String nickName; // 昵称

    private String avatar; // 头像

    private Integer rankingList;

    private int existRollout;

    private Integer fansNum;  //粉丝总数

    private String columnLevel; //专栏等级

    public String getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(String columnLevel) {
        this.columnLevel = columnLevel;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public int getExistRollout() {
        return existRollout;
    }

    public void setExistRollout(int existRollout) {
        this.existRollout = existRollout;
    }

    public String getRegisterOriginContent() {
        return registerOriginContent;
    }

    public void setRegisterOriginContent(String registerOriginContent) {
        this.registerOriginContent = registerOriginContent;
    }

    public Integer getRankingList() {
        return rankingList;
    }

    public void setRankingList(Integer rankingList) {
        this.rankingList = rankingList;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getColumnIdentity() {
        return columnIdentity;
    }

    public void setColumnIdentity(String columnIdentity) {
        this.columnIdentity = columnIdentity;
    }

    public Integer getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(Integer columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public Integer getRegisterOrigin() {
        return registerOrigin;
    }

    public void setRegisterOrigin(Integer registerOrigin) {
        this.registerOrigin = registerOrigin;
    }

    public Integer getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Integer inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getParentUser() {
        return parentUser;
    }

    public void setParentUser(String parentUser) {
        this.parentUser = parentUser;
    }

    public String getRelTime() {
        return relTime;
    }

    public void setRelTime(String relTime) {
        this.relTime = relTime;
    }

    public Integer getApplyRole() {
        return applyRole;
    }

    public void setApplyRole(Integer applyRole) {
        this.applyRole = applyRole;
    }

    /* 其他信息 */
    private UserProfileOutModel profile;

    public String getApplyColumnTime() {
        return applyColumnTime;
    }

    public void setApplyColumnTime(String applyColumnTime) {
        this.applyColumnTime = applyColumnTime;
    }

    public UserProfileOutModel getProfile() {
        return profile;
    }

    public void setProfile(UserProfileOutModel profile) {
        this.profile = profile;
    }

    public Integer getBowseNum() {
        return bowseNum;
    }

    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLevelDisplay() {
        return userLevelDisplay;
    }

    public void setUserLevelDisplay(String userLevelDisplay) {
        this.userLevelDisplay = userLevelDisplay;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusDisplay() {
        return auditStatusDisplay;
    }

    public void setAuditStatusDisplay(String auditStatusDisplay) {
        this.auditStatusDisplay = auditStatusDisplay;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getLockStatusDisplay() {
        return lockStatusDisplay;
    }

    public void setLockStatusDisplay(String lockStatusDisplay) {
        this.lockStatusDisplay = lockStatusDisplay;
    }

    public Boolean getHotUser() {
        return hotUser;
    }

    public void setHotUser(Boolean hotUser) {
        this.hotUser = hotUser;
    }

    public String getHotUserDisplay() {
        return hotUserDisplay;
    }

    public void setHotUserDisplay(String hotUserDisplay) {
        this.hotUserDisplay = hotUserDisplay;
    }

    public Long getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(Long userOrder) {
        this.userOrder = userOrder;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
