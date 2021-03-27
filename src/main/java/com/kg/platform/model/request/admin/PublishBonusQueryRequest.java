package com.kg.platform.model.request.admin;

/**
 * 发文奖励查询入参
 */
public class PublishBonusQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 8994710150789949353L;

    private Long articleId;

    private String articleTitle;

    private Long flowId;

    private Long userId;

    private Long adminId;

    private String userName;

    private String userPhone;

    private String publisher;

    private Long columnId;

    private String publishStartDate;

    private String publishEndDate;

    private String auditEndDate;

    private Integer bonusStatus;

    private Integer addBonusStatus;

    private Integer flowStatus;

    private Integer articleMark;

    private Integer sortRule;

    private String sortFiledName;

    private String auditStartDate;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getPublishStartDate() {
        return publishStartDate;
    }

    public void setPublishStartDate(String publishStartDate) {
        this.publishStartDate = publishStartDate;
    }

    public String getPublishEndDate() {
        return publishEndDate;
    }

    public void setPublishEndDate(String publishEndDate) {
        this.publishEndDate = publishEndDate;
    }

    public String getAuditEndDate() {
        return auditEndDate;
    }

    public void setAuditEndDate(String auditEndDate) {
        this.auditEndDate = auditEndDate;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public Integer getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(Integer articleMark) {
        this.articleMark = articleMark;
    }

    public String getAuditStartDate() {
        return auditStartDate;
    }

    public void setAuditStartDate(String auditStartDate) {
        this.auditStartDate = auditStartDate;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getAddBonusStatus() {
        return addBonusStatus;
    }

    public void setAddBonusStatus(Integer addBonusStatus) {
        this.addBonusStatus = addBonusStatus;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getSortRule() {
        return sortRule;
    }

    public void setSortRule(Integer sortRule) {
        this.sortRule = sortRule;
    }

    public String getSortFiledName() {
        return sortFiledName;
    }

    public void setSortFiledName(String sortFiledName) {
        this.sortFiledName = sortFiledName;
    }
}
