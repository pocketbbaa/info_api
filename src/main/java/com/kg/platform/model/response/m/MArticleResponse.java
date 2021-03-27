package com.kg.platform.model.response.m;

import com.kg.platform.model.response.KgArticleBonusResponse;
import com.kg.platform.model.response.UserProfileResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MArticleResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5792499177876973080L;

    private MArticleResponse OnResponse;

    private MArticleResponse Underesponse;

    private String username;

    private String articleTagnames;

    private String bonusNum;// 奖励领取人数

    private String tipsinCount;// 文章被打赏数

    private String profilename;// 专栏名称，热门文章接口用于文章所在的栏目展示

    private String profileavatar;// 专栏头像

    private String columnname;// 作者栏目名称

    private String columnintro;// 专栏介绍

    private Long collectstatus;// 收藏状态

    private Long articlefrom;// 来源

    private UserProfileResponse profileResponse;

    private String articleId;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private String articleSource;

    private String articleLink;

    private Integer columnId;

    private Integer isPublishBonus;

    private Integer articleMark;

    private Integer publishBonusStatus;

    private String freezeReason;

    private Integer secondColumn;

    private String displayStatus;

    private Integer displayOrder;

    private Long commentSet;

    private Boolean publishSet;

    private Date publishTime;

    private Integer publishStatus;

    private String createUser;

    private Date createDate;

    private String updateDate;

    private Long updateUser;

    private Integer auditUser;

    private Date auditDate;

    private Integer sysUser;

    private String refuseReason;

    private Integer bowseNum;

    private String articleText;

    private String secondcolumnname;

    private String userColumn;

    private String columnAvatar;

    private Integer bonusStatus;// 是否开启打赏

    private Long bonus;

    private Long praisestatus;// 用户是否点赞过 0：未点赞

    private Integer bonusState;// 阅读奖励是否生效

    private String bonusValuesum;

    private Integer getStatus;// 领取浏览奖励状态

    private Integer browseNum;// 文章浏览数

    private Integer thumbupNum;// 点赞数

    private Integer collect;// 收藏数

    private Integer comments;// 评论数

    private List<KgArticleBonusResponse> listArtBonus;

    private List<KgArticleBonusResponse> listAllBonus;

    private Long textnum;

    private String searchword;

    private Integer commentAudit;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private String updateTimestamp;

    private Integer articleImgSize;//1：小图  2：大图

    private String rewardIncome;//作者奖励收入

	public MArticleResponse getOnResponse() {
		return OnResponse;
	}

	public void setOnResponse(MArticleResponse onResponse) {
		OnResponse = onResponse;
	}

	public MArticleResponse getUnderesponse() {
		return Underesponse;
	}

	public void setUnderesponse(MArticleResponse underesponse) {
		Underesponse = underesponse;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getArticleTagnames() {
		return articleTagnames;
	}

	public void setArticleTagnames(String articleTagnames) {
		this.articleTagnames = articleTagnames;
	}

	public String getBonusNum() {
		return bonusNum;
	}

	public void setBonusNum(String bonusNum) {
		this.bonusNum = bonusNum;
	}

	public String getTipsinCount() {
		return tipsinCount;
	}

	public void setTipsinCount(String tipsinCount) {
		this.tipsinCount = tipsinCount;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public String getProfileavatar() {
		return profileavatar;
	}

	public void setProfileavatar(String profileavatar) {
		this.profileavatar = profileavatar;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumnintro() {
		return columnintro;
	}

	public void setColumnintro(String columnintro) {
		this.columnintro = columnintro;
	}

	public Long getCollectstatus() {
		return collectstatus;
	}

	public void setCollectstatus(Long collectstatus) {
		this.collectstatus = collectstatus;
	}

	public Long getArticlefrom() {
		return articlefrom;
	}

	public void setArticlefrom(Long articlefrom) {
		this.articlefrom = articlefrom;
	}

	public UserProfileResponse getProfileResponse() {
		return profileResponse;
	}

	public void setProfileResponse(UserProfileResponse profileResponse) {
		this.profileResponse = profileResponse;
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

	public String getArticleTags() {
		return articleTags;
	}

	public void setArticleTags(String articleTags) {
		this.articleTags = articleTags;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public String getArticleImage() {
		return articleImage;
	}

	public void setArticleImage(String articleImage) {
		this.articleImage = articleImage;
	}

	public Integer getArticleType() {
		return articleType;
	}

	public void setArticleType(Integer articleType) {
		this.articleType = articleType;
	}

	public String getArticleSource() {
		return articleSource;
	}

	public void setArticleSource(String articleSource) {
		this.articleSource = articleSource;
	}

	public String getArticleLink() {
		return articleLink;
	}

	public void setArticleLink(String articleLink) {
		this.articleLink = articleLink;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getIsPublishBonus() {
		return isPublishBonus;
	}

	public void setIsPublishBonus(Integer isPublishBonus) {
		this.isPublishBonus = isPublishBonus;
	}

	public Integer getArticleMark() {
		return articleMark;
	}

	public void setArticleMark(Integer articleMark) {
		this.articleMark = articleMark;
	}

	public Integer getPublishBonusStatus() {
		return publishBonusStatus;
	}

	public void setPublishBonusStatus(Integer publishBonusStatus) {
		this.publishBonusStatus = publishBonusStatus;
	}

	public String getFreezeReason() {
		return freezeReason;
	}

	public void setFreezeReason(String freezeReason) {
		this.freezeReason = freezeReason;
	}

	public Integer getSecondColumn() {
		return secondColumn;
	}

	public void setSecondColumn(Integer secondColumn) {
		this.secondColumn = secondColumn;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getCommentSet() {
		return commentSet;
	}

	public void setCommentSet(Long commentSet) {
		this.commentSet = commentSet;
	}

	public Boolean getPublishSet() {
		return publishSet;
	}

	public void setPublishSet(Boolean publishSet) {
		this.publishSet = publishSet;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(Integer auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getSysUser() {
		return sysUser;
	}

	public void setSysUser(Integer sysUser) {
		this.sysUser = sysUser;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Integer getBowseNum() {
		return bowseNum;
	}

	public void setBowseNum(Integer bowseNum) {
		this.bowseNum = bowseNum;
	}

	public String getArticleText() {
		return articleText;
	}

	public void setArticleText(String articleText) {
		this.articleText = articleText;
	}

	public String getSecondcolumnname() {
		return secondcolumnname;
	}

	public void setSecondcolumnname(String secondcolumnname) {
		this.secondcolumnname = secondcolumnname;
	}

	public String getUserColumn() {
		return userColumn;
	}

	public void setUserColumn(String userColumn) {
		this.userColumn = userColumn;
	}

	public String getColumnAvatar() {
		return columnAvatar;
	}

	public void setColumnAvatar(String columnAvatar) {
		this.columnAvatar = columnAvatar;
	}

	public Integer getBonusStatus() {
		return bonusStatus;
	}

	public void setBonusStatus(Integer bonusStatus) {
		this.bonusStatus = bonusStatus;
	}

	public Long getBonus() {
		return bonus;
	}

	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}

	public Long getPraisestatus() {
		return praisestatus;
	}

	public void setPraisestatus(Long praisestatus) {
		this.praisestatus = praisestatus;
	}

	public Integer getBonusState() {
		return bonusState;
	}

	public void setBonusState(Integer bonusState) {
		this.bonusState = bonusState;
	}

	public String getBonusValuesum() {
		return bonusValuesum;
	}

	public void setBonusValuesum(String bonusValuesum) {
		this.bonusValuesum = bonusValuesum;
	}

	public Integer getGetStatus() {
		return getStatus;
	}

	public void setGetStatus(Integer getStatus) {
		this.getStatus = getStatus;
	}

	public Integer getBrowseNum() {
		return browseNum;
	}

	public void setBrowseNum(Integer browseNum) {
		this.browseNum = browseNum;
	}

	public Integer getThumbupNum() {
		return thumbupNum;
	}

	public void setThumbupNum(Integer thumbupNum) {
		this.thumbupNum = thumbupNum;
	}

	public Integer getCollect() {
		return collect;
	}

	public void setCollect(Integer collect) {
		this.collect = collect;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public List<KgArticleBonusResponse> getListArtBonus() {
		return listArtBonus;
	}

	public void setListArtBonus(List<KgArticleBonusResponse> listArtBonus) {
		this.listArtBonus = listArtBonus;
	}

	public List<KgArticleBonusResponse> getListAllBonus() {
		return listAllBonus;
	}

	public void setListAllBonus(List<KgArticleBonusResponse> listAllBonus) {
		this.listAllBonus = listAllBonus;
	}

	public Long getTextnum() {
		return textnum;
	}

	public void setTextnum(Long textnum) {
		this.textnum = textnum;
	}

	public String getSearchword() {
		return searchword;
	}

	public void setSearchword(String searchword) {
		this.searchword = searchword;
	}

	public Integer getCommentAudit() {
		return commentAudit;
	}

	public void setCommentAudit(Integer commentAudit) {
		this.commentAudit = commentAudit;
	}

	public Integer getPublishKind() {
		return publishKind;
	}

	public void setPublishKind(Integer publishKind) {
		this.publishKind = publishKind;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoFilename() {
		return videoFilename;
	}

	public void setVideoFilename(String videoFilename) {
		this.videoFilename = videoFilename;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getArticleImgSize() {
		return articleImgSize;
	}

	public void setArticleImgSize(Integer articleImgSize) {
		this.articleImgSize = articleImgSize;
	}

	public String getRewardIncome() {
		return rewardIncome;
	}

	public void setRewardIncome(String rewardIncome) {
		this.rewardIncome = rewardIncome;
	}
}
