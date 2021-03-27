package com.kg.platform.model.response;

public class UserProfileColumnAppResponse {

    private String createUser; // 用户ID

    private long artsum;// 文章数

    private int pbowsenum; // 浏览量

    private int comments; // 评论数

    private String columnName; // 作者名

    private String columnIntro; // 专栏介绍

    private String columnAvatar; // 专栏头像

    private Integer concernedStatus; // 作者关注当前用户状态 0未关注 1已关注

    private Integer concernUserStatus; // 当前登录用户关注作者状态 0未关注 1已关注

    private long thumbupNum;// 作者点赞数

    private Integer userRole; // 用户角色

    private String sex; // 性别

    private String country; // 国家

    private String province; // 省

    private String city; // 城市

    private String county; // 区

    private String resume; // 用户简介

    /** V1.0.1新增字段 **/
    private String identityTag; // 身份标识

    private int realAuthedTag; // 实名标签 0:无，1：有

    private int vipTag; // 是否有大V标签 0：无，1：有

    private String rewardIncome; // 奖励收入

    private String inviteCode; // 邀请码

    private int existRollout;

    public int getExistRollout() {
        return existRollout;
    }

    public void setExistRollout(int existRollout) {
        this.existRollout = existRollout;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    public String getRewardIncome() {
        return rewardIncome;
    }

    public void setRewardIncome(String rewardIncome) {
        this.rewardIncome = rewardIncome;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getArtsum() {
        return artsum;
    }

    public void setArtsum(long artsum) {
        this.artsum = artsum;
    }

    public int getPbowsenum() {
        return pbowsenum;
    }

    public void setPbowsenum(int pbowsenum) {
        this.pbowsenum = pbowsenum;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnIntro() {
        return columnIntro;
    }

    public void setColumnIntro(String columnIntro) {
        this.columnIntro = columnIntro;
    }

    public String getColumnAvatar() {
        return columnAvatar;
    }

    public void setColumnAvatar(String columnAvatar) {
        this.columnAvatar = columnAvatar;
    }

    public long getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(long thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public Integer getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(Integer concernedStatus) {
        this.concernedStatus = concernedStatus;
    }

    public Integer getConcernUserStatus() {
        return concernUserStatus;
    }

    public void setConcernUserStatus(Integer concernUserStatus) {
        this.concernUserStatus = concernUserStatus;
    }

}