package com.kg.platform.model.response.admin;

import java.math.BigDecimal;
import java.util.Date;

public class PublishBonusQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8549770256464921903L;

    private String flowId;

    private String userName;

    private String articleId;

    private String articleTitle;

    private String userMobile;

    private String publisher;

    private Date publishTime;

    private Date auditTime;

    private String sysUserName;

    private String displayStatus;

    private Integer bowseNum;

    private Integer isMarkArticle;

    private Integer publishBonusStatus;

    private Integer isAddBonus;

    private Integer shareNum;

    private BigDecimal tvAmount;

    private BigDecimal amount;

    private BigDecimal txbAmount;

    private String articleMark;

    private String columnName;

    private String secondColumnName;

    private String flowStatus;

    private BigDecimal tributeAmount;

    private String checkMan;

    private Integer publishKind;

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getBowseNum() {
        return bowseNum;
    }

    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public String getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(String articleMark) {
        this.articleMark = articleMark;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public BigDecimal getTributeAmount() {
        return tributeAmount;
    }

    public void setTributeAmount(BigDecimal tributeAmount) {
        this.tributeAmount = tributeAmount;
    }

    public BigDecimal getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(BigDecimal tvAmount) {
        this.tvAmount = tvAmount;
    }

    public BigDecimal getTxbAmount() {
        return txbAmount;
    }

    public void setTxbAmount(BigDecimal txbAmount) {
        this.txbAmount = txbAmount;
    }

    public String getSecondColumnName() {
        return secondColumnName;
    }

    public void setSecondColumnName(String secondColumnName) {
        this.secondColumnName = secondColumnName;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public Integer getIsMarkArticle() {
        return isMarkArticle;
    }

    public void setIsMarkArticle(Integer isMarkArticle) {
        this.isMarkArticle = isMarkArticle;
    }

    public Integer getIsAddBonus() {
        return isAddBonus;
    }

    public void setIsAddBonus(Integer isAddBonus) {
        this.isAddBonus = isAddBonus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getPublishBonusStatus() {
        return publishBonusStatus;
    }

    public void setPublishBonusStatus(Integer publishBonusStatus) {
        this.publishBonusStatus = publishBonusStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
