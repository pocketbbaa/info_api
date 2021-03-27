package com.kg.platform.model.request.admin;

/**
 * 批量审核入参
 */
public class ArticleBatchReviewRequest {

    private String articleIds; //资讯ID集合
    private Integer columnId; //栏目ID
    private Integer secondColumn; //二级栏目ID
    private Integer ifPlatformPublishAward;//是否发送集成发文奖励 0：否 1：是
    private String auditUser; //审核人
    private Integer publicStatus; //1审核通过，3审核不通过
    private String refuseReason; //审批未通过理由

    private Integer ifIntoIndex;//是否展示到首页推荐列表 0：否 1：是

    public String getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(String articleIds) {
        this.articleIds = articleIds;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(Integer secondColumn) {
        this.secondColumn = secondColumn;
    }

    public Integer getIfPlatformPublishAward() {
        return ifPlatformPublishAward;
    }

    public void setIfPlatformPublishAward(Integer ifPlatformPublishAward) {
        this.ifPlatformPublishAward = ifPlatformPublishAward;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Integer getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Integer publicStatus) {
        this.publicStatus = publicStatus;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Integer getIfIntoIndex() {
        return ifIntoIndex;
    }

    public void setIfIntoIndex(Integer ifIntoIndex) {
        this.ifIntoIndex = ifIntoIndex;
    }
}
