package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ArticleExportExcel extends BaseRowModel {

    @ExcelProperty(value = "资讯ID", index = 0)
    private String articleId;

    @ExcelProperty(value = "资讯标题", index = 1)
    private String articleTitle;

    @ExcelProperty(value = "作者ID", index = 2)
    private String authorId;

    @ExcelProperty(value = "作者昵称", index = 3)
    private String authorName;

    @ExcelProperty(value = "手机号码", index = 4)
    private String authorPhone;

    @ExcelProperty(value = "文章发布时间", index = 5)
    private String createTime;

    @ExcelProperty(value = "文章审核时间", index = 6)
    private String auditTime;

    @ExcelProperty(value = "审核状态(0 草稿 1 已发布（审核通过） 2 审核中 3 审批未通过  4前台保存的草)", index = 7)
    private String auditState;

    @ExcelProperty(value = "审核未通过理由", index = 8)
    private String refuseReason;

    @ExcelProperty(value = "审核人", index = 9)
    private String auditUser;

    @ExcelProperty(value = "一级栏目", index = 10)
    private String columnName;

    @ExcelProperty(value = "二级栏目", index = 11)
    private String secondColumnName;

    @ExcelProperty(value = "浏览量", index = 12)
    private String browseNum;

    @ExcelProperty(value = "收藏量", index = 13)
    private String collectNum;

    @ExcelProperty(value = "点赞量", index = 14)
    private String thumbupNum;

    @ExcelProperty(value = "分享量", index = 15)
    private String shareNum;

    @ExcelProperty(value = "评论量", index = 16)
    private String commentNum;

    @ExcelProperty(value = "显示状态(1 正常显示 2 首页置顶 3 首页推荐 4 前台隐藏)", index = 17)
    private String displayStatus; //显示设置 1 正常显示 2 首页置顶 3 首页推荐 4 前台隐藏

    @ExcelProperty(value = "原创或转载(0 默认 1 原创 2 转载)", index = 18)
    private String articleType; //文章类别 1 原创 2 转载

    @ExcelProperty(value = "是否有优质原创标识", index = 19)
    private String articleMark;  //文章标识 1优质文章

    @ExcelProperty(value = "是否展示到首页推荐列表 1 是", index = 20)
    private String toIndex;

    @ExcelProperty(value = "专栏等级（0：见习，1：一星，2：二星，3：三星，4：四星，5：五星）", index = 21)
    private String columnLevel;

    public String getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(String columnLevel) {
        this.columnLevel = columnLevel;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhone() {
        return authorPhone;
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSecondColumnName() {
        return secondColumnName;
    }

    public void setSecondColumnName(String secondColumnName) {
        this.secondColumnName = secondColumnName;
    }

    public String getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(String browseNum) {
        this.browseNum = browseNum;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public String getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(String thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public String getShareNum() {
        return shareNum;
    }

    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(String articleMark) {
        this.articleMark = articleMark;
    }

    public String getToIndex() {
        return toIndex;
    }

    public void setToIndex(String toIndex) {
        this.toIndex = toIndex;
    }

    public static ArticleExportExcel initModel(ArticleExportExcel articleExportExcel) {
        articleExportExcel.setArticleId(articleExportExcel.getArticleId() == null ? "" : articleExportExcel.getArticleId());
        articleExportExcel.setArticleTitle(articleExportExcel.getArticleTitle() == null ? "" : articleExportExcel.getArticleTitle());
        articleExportExcel.setAuthorId(articleExportExcel.getAuthorId() == null ? "" : articleExportExcel.getAuthorId());
        articleExportExcel.setAuthorName(articleExportExcel.getAuthorName() == null ? "" : articleExportExcel.getAuthorName());
        articleExportExcel.setAuthorPhone(articleExportExcel.getAuthorPhone() == null ? "" : articleExportExcel.getAuthorPhone());
        articleExportExcel.setCreateTime(articleExportExcel.getCreateTime() == null ? "" : articleExportExcel.getCreateTime());
        articleExportExcel.setAuditTime(articleExportExcel.getAuditTime() == null ? "" : articleExportExcel.getAuditTime());
        articleExportExcel.setRefuseReason(articleExportExcel.getRefuseReason() == null ? "" : articleExportExcel.getRefuseReason());
        return articleExportExcel;
    }
}
