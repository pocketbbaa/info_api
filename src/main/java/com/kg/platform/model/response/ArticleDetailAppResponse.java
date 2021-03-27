package com.kg.platform.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.ArticleRequest;

/**
 * 文章详情
 */
public class ArticleDetailAppResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5792448677876973080L;

    private String articleTagnames; // 文章标签名称（便于前端显示）

    private long collectstatus; // 收藏状态

    private long articlefrom;// 来源

    private long createUserId;// 作者id

    private UserProfileAppResponse profileResponse; // 作者信息

    private List<AdverResponse> advers; //广告信息

    private String articleId;

    private String articleTitle;

    private String articleTags; // 标签ID

    private String articleDescription; // 文章摘要

    private String articleImage; // 封面图片

    private Integer articleType; // 文章类别 1 原创 2 转载

    private String articleSource; // 文章来源

    private String articleLink; // 原文链接

    private String displayStatus; // 显示设置 1 正常显示 2 首页置顶 3 首页推荐

    private long commentSet; // 评论设置 0 关闭评论 1 开启评论

    private Date createDate; // 创建时间

    private String articleText; // 文章内容

    private int bonusStatus;// 是否开启打赏

    private long praisestatus;// 用户是否点赞过 0：未点赞

    private int bonusState;// 阅读奖励是否生效

    private int getStatus;// 领取浏览奖励状态

    private Integer browseNum;// 文章浏览数

    private Integer thumbupNum;// 点赞数

    private Integer collect;// 收藏数

    private Integer comments;// 评论数

    private Integer concernStatus;// 关注状态

    private String videoUrl;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    // 阅读奖励
    private BigDecimal readbonus;

    // 阅读奖励单位
    private String readBonusUnit;

    private String updateTimestamp;

    private Integer haveShareBonus; // 0 无 1有

    private Integer commentAudit;//评论是否需要审核 0 不需要审核 1 需要审核

    private String blockchainUrl; //文章上链后的查看地址

    public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }

    public Integer getCommentAudit() {
        return commentAudit;
    }

    public void setCommentAudit(Integer commentAudit) {
        this.commentAudit = commentAudit;
    }

    public Integer getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(Integer concernStatus) {
        this.concernStatus = concernStatus;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public BigDecimal getReadbonus() {
        return readbonus;
    }

    public void setReadbonus(BigDecimal readbonus) {
        this.readbonus = readbonus;
    }

    public String getReadBonusUnit() {
        return readBonusUnit;
    }

    public void setReadBonusUnit(String readBonusUnit) {
        this.readBonusUnit = readBonusUnit;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    private List<KgArticleBonusResponse> listArtBonus;

    public List<KgArticleBonusResponse> getListArtBonus() {
        return listArtBonus;
    }

    public void setListArtBonus(List<KgArticleBonusResponse> listArtBonus) {
        this.listArtBonus = listArtBonus;
    }

    public String getArticleTagnames() {
        return articleTagnames;
    }

    public void setArticleTagnames(String articleTagnames) {
        this.articleTagnames = articleTagnames;
    }

    public long getCollectstatus() {
        return collectstatus;
    }

    public void setCollectstatus(long collectstatus) {
        this.collectstatus = collectstatus;
    }

    public long getArticlefrom() {
        return articlefrom;
    }

    public void setArticlefrom(long articlefrom) {
        this.articlefrom = articlefrom;
    }

    public UserProfileAppResponse getProfileResponse() {
        return profileResponse;
    }

    public void setProfileResponse(UserProfileAppResponse profileResponse) {
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

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public long getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(long commentSet) {
        this.commentSet = commentSet;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public int getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(int bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public long getPraisestatus() {
        return praisestatus;
    }

    public void setPraisestatus(long praisestatus) {
        this.praisestatus = praisestatus;
    }

    public int getBonusState() {
        return bonusState;
    }

    public void setBonusState(int bonusState) {
        this.bonusState = bonusState;
    }

    public int getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(int getStatus) {
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

    public Integer getHaveShareBonus() {
        return haveShareBonus;
    }

    public void setHaveShareBonus(Integer haveShareBonus) {
        this.haveShareBonus = haveShareBonus;
    }

    public List<AdverResponse> getAdvers() {
        return advers;
    }

    public void setAdvers(List<AdverResponse> advers) {
        this.advers = advers;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 构建详情基本信息
     *
     * @param articleResponse
     * @return
     */
    public static ArticleDetailAppResponse buildBaseDetail(ArticleResponse articleResponse, ArticleRequest request) {
        ArticleDetailAppResponse articleDetailAppResponse = new ArticleDetailAppResponse();
        articleDetailAppResponse.setArticleId(articleResponse.getArticleId());
        articleDetailAppResponse.setArticleDescription(articleResponse.getArticleDescription());
        articleDetailAppResponse.setArticlefrom(articleResponse.getArticlefrom());
        articleDetailAppResponse.setArticleImage(articleResponse.getArticleImage());
        articleDetailAppResponse.setArticleLink(articleResponse.getArticleLink());
        articleDetailAppResponse.setArticleSource(articleResponse.getArticleSource());
        articleDetailAppResponse.setArticleTagnames(articleResponse.getArticleTagnames());
        articleDetailAppResponse.setArticleTags(articleResponse.getArticleTags());
        articleDetailAppResponse.setArticleText(articleResponse.getArticleText());
        articleDetailAppResponse.setArticleTitle(articleResponse.getArticleTitle());
        articleDetailAppResponse.setArticleType(articleResponse.getArticleType());
        articleDetailAppResponse.setBonusState(articleResponse.getBonusState());
        articleDetailAppResponse.setBonusStatus(articleResponse.getBonusStatus());
        articleDetailAppResponse.setBrowseNum(articleResponse.getBrowseNum());
        articleDetailAppResponse.setCollect(articleResponse.getCollect());
        articleDetailAppResponse.setCollectstatus(articleResponse.getCollectstatus());
        articleDetailAppResponse.setComments(articleResponse.getComments());
        articleDetailAppResponse.setCommentSet(articleResponse.getCommentSet());
        articleDetailAppResponse.setCreateDate(articleResponse.getCreateDate());
        articleDetailAppResponse.setDisplayStatus(articleResponse.getDisplayStatus());
        articleDetailAppResponse.setGetStatus(articleResponse.getGetStatus());
        articleDetailAppResponse.setPraisestatus(articleResponse.getPraisestatus());
        articleDetailAppResponse.setThumbupNum(articleResponse.getThumbupNum());
        articleDetailAppResponse.setListArtBonus(articleResponse.getListArtBonus()); // 阅读奖励
        articleDetailAppResponse.setVideoUrl(articleResponse.getVideoUrl());
        articleDetailAppResponse.setPublishKind(articleResponse.getPublishKind());
        articleDetailAppResponse.setUpdateTimestamp(articleResponse.getUpdateTimestamp());
        articleDetailAppResponse.setCommentAudit(articleResponse.getCommentAudit());
        articleDetailAppResponse.setBlockchainUrl(articleResponse.getBlockchainUrl());
        String userId = request.getUserId();
        if (StringUtils.isEmpty(userId)) {
            articleDetailAppResponse.setPraisestatus(0);
        }
        // 默认有分享奖励
        articleDetailAppResponse.setHaveShareBonus(1);
        return articleDetailAppResponse;
    }
}
