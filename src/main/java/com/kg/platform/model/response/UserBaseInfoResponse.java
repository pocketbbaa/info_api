package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * 用户基本信息
 */
public class UserBaseInfoResponse implements Serializable {

    private String userId; // 用户编号

    private String userName; // 用户名

    private String inmagelink; // 头像

    private String resume; // 个人简介

    private String balance; // 可用钛值

    private String txbBalance; // 可用钛小白

    private String ritBalance; // 可用RIT

    private Long inviteCount; // 邀请人数

    private Long fansCount; // 粉丝人数

    private String userMobile;// 电话号码

    /** V1.0.1新增字段 **/
    private String identityTag; // 身份标识

    private int realAuthedTag; // 实名标签 0:无，1：有

    private int vipTag; // 是否有大V标签 0：无，1：有

    private int fansNew; // 是否有新粉丝

    private int userRole; // 用户角色

    private String compassUrl; //玩法指南url

    public String getCompassUrl() {
        return compassUrl;
    }

    public void setCompassUrl(String compassUrl) {
        this.compassUrl = compassUrl;
    }

    private List<PersonalAdvResponse> advers; // 广告

    /**V1.3.0新增字段  **/
    private Integer collectCnt;  //收藏数
    private Integer commentCnt;  //评论数
    private Long attentionCnt; //关注数
    private Integer praiseCnt; //点赞数
    private Long apprenticeCnt; //徒弟个数

	private String inviteCode;//邀请码

	private String yesterdayEarningsBtc;//昨日BTC收益

	public String getYesterdayEarningsBtc() {
		return yesterdayEarningsBtc;
	}

	public void setYesterdayEarningsBtc(String yesterdayEarningsBtc) {
		this.yesterdayEarningsBtc = yesterdayEarningsBtc;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Integer getCollectCnt() {
        return collectCnt;
    }

    public void setCollectCnt(Integer collectCnt) {
        this.collectCnt = collectCnt;
    }

    public Integer getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public Long getAttentionCnt() {
        return attentionCnt;
    }

    public void setAttentionCnt(Long attentionCnt) {
        this.attentionCnt = attentionCnt;
    }

    public Integer getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(Integer praiseCnt) {
        this.praiseCnt = praiseCnt;
    }

    public Long getApprenticeCnt() {
        return apprenticeCnt;
    }

    public void setApprenticeCnt(Long apprenticeCnt) {
        this.apprenticeCnt = apprenticeCnt;
    }

    public String getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(String ritBalance) {
        this.ritBalance = ritBalance;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public Long getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Long inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTxbBalance() {
        return txbBalance;
    }

    public void setTxbBalance(String txbBalance) {
        this.txbBalance = txbBalance;
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

    public String getInmagelink() {
        return inmagelink;
    }

    public void setInmagelink(String inmagelink) {
        this.inmagelink = inmagelink;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Long getFansCount() {
        return fansCount;
    }

    public void setFansCount(Long fansCount) {
        this.fansCount = fansCount;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getFansNew() {
        return fansNew;
    }

    public void setFansNew(int fansNew) {
        this.fansNew = fansNew;
    }

    public List<PersonalAdvResponse> getAdvers() {
        return advers;
    }

    public void setAdvers(List<PersonalAdvResponse> advers) {
        this.advers = advers;
    }
}
