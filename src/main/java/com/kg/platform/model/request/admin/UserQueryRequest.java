package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 用户管理查询条件入参： 用户ID、邮箱地址、手机号、用户角色、注册时间、审核状态、锁定状态
 */
public class UserQueryRequest extends AdminBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = 1218202388844625205L;

    private String userId;

    private String inviteUserId;

    private String userName;

    private String userEmail;

    private String userMobile;

    private Integer userRole;

    private Integer userLevel;

    private Date createDateStart;

    private Date createDateEnd;

    private Date relDateStart;

    private Date relDateEnd;

    private Integer auditStatus;

    private Integer lockStatus;

    private Boolean hotUser;

    private Integer relType;

    private String orderByClause;

    private Integer platform; //注册来源1：IOS，2：安卓，3：web，4:H5，5：千氪专栏Web

    /**
     * web1.2.0新增
     **/
    private Integer channel; //渠道

    private String master; //师傅

    private Integer userOrder; //用户排序

    private Integer isHotUser; //是否热门作者 0：不是 1：是

    private String sort;

    private String auditDateStart;

    private String auditDateEnd;

    /**
     * 1.3.4新增
     */
    private Integer columnLevel; //专栏等级

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getIsHotUser() {
        return isHotUser;
    }

    public void setIsHotUser(Integer isHotUser) {
        this.isHotUser = isHotUser;
    }

    public Integer getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(Integer userOrder) {
        this.userOrder = userOrder;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    private Integer rankingList;

    public Integer getRankingList() {
        return rankingList;
    }

    public void setRankingList(Integer rankingList) {
        this.rankingList = rankingList;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public Integer getRelType() {
        return relType;
    }

    public void setRelType(Integer relType) {
        this.relType = relType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getHotUser() {
        return hotUser;
    }

    public void setHotUser(Boolean hotUser) {
        this.hotUser = hotUser;
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

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Date getRelDateStart() {
        return relDateStart;
    }

    public void setRelDateStart(Date relDateStart) {
        this.relDateStart = relDateStart;
    }

    public Date getRelDateEnd() {
        return relDateEnd;
    }

    public void setRelDateEnd(Date relDateEnd) {
        this.relDateEnd = relDateEnd;
    }

    public Date getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(Date createDateStart) {
        this.createDateStart = createDateStart;
    }

    public Date getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getAuditDateStart() {
        return auditDateStart;
    }

    public void setAuditDateStart(String auditDateStart) {
        this.auditDateStart = auditDateStart;
    }

    public String getAuditDateEnd() {
        return auditDateEnd;
    }

    public void setAuditDateEnd(String auditDateEnd) {
        this.auditDateEnd = auditDateEnd;
    }
}
