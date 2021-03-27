package com.kg.platform.model.request.admin;

import java.math.BigDecimal;

public class ArticleQueryRequest extends AdminBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = -3494164201385576674L;

    private String articleId;

    private String articleTitle;

    private String articleTag;

    private String createUser;

    private String auditUser;

    private Long columnId;

    private Long userId;

    private Long secondColumn;

    private Integer publishStatus;

    private Integer publishBonusStatus;

    private Integer publishKind;

    private Integer adminId;

    private Integer displayStatus;

    private String orderByClause;

    private BigDecimal bonus;

    private Integer bonusType;

    private String bonusReason;

    private String freezeReason;

    private Integer ifPlatformPublishAward; // 是否设有发文基础奖励 0：否 1：是

    private Integer articleFrom; // 文章来源 0：全部 1：人工添加 2：抓取

    private Integer ifIntoIndex; // 是否展示到首页推荐列表 0：否 1：是

    private Integer articleType; // 文章类别 0 默认 1 原创 2 转载 

    private Integer ifPush;//是否推送 0：否 1：是

    private Integer articleMark; // 文章标识 1优质文章

    private Integer blockchainUrl; //是否上链 0：否 1：是

    private String startDay; //开始时间
    private String endDay; //结束时间

    public Integer getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(Integer blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }

    public Integer getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(Integer articleMark) {
        this.articleMark = articleMark;
    }

    public Integer getIfPush() {
        return ifPush;
    }

    public void setIfPush(Integer ifPush) {
        this.ifPush = ifPush;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public Integer getIfIntoIndex() {
        return ifIntoIndex;
    }

    public void setIfIntoIndex(Integer ifIntoIndex) {
        this.ifIntoIndex = ifIntoIndex;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public Integer getArticleFrom() {
        return articleFrom;
    }

    public void setArticleFrom(Integer articleFrom) {
        this.articleFrom = articleFrom;
    }

    public Integer getIfPlatformPublishAward() {
        return ifPlatformPublishAward;
    }

    public void setIfPlatformPublishAward(Integer ifPlatformPublishAward) {
        this.ifPlatformPublishAward = ifPlatformPublishAward;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(Long secondColumn) {
        this.secondColumn = secondColumn;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getPublishBonusStatus() {
        return publishBonusStatus;
    }

    public void setPublishBonusStatus(Integer publishBonusStatus) {
        this.publishBonusStatus = publishBonusStatus;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public Integer getBonusType() {
        return bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFreezeReason() {
        return freezeReason;
    }

    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }
}
