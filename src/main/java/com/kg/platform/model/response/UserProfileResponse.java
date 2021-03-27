package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.List;

public class UserProfileResponse implements Serializable {

    private static final long serialVersionUID = 8937025886437992127L;

    private String idcardFront;

    private String idcardBack;

    private String userName;

    private String createUser;

    private long artsum;// 文章数

    private int pbowsenum;

    private long collect;

    private int comments;

    List<ArticleResponse> list;

    private String userId;

    private Integer roleId;

    private String avatar;

    private String sex;

    private String country;

    private String province;

    private String city;

    private String county;

    private String resume;

    private String roleDefine;

    private String columnName;

    private String columnIntro;

    private String columnAvatar;

    private String columnCountry;

    private String columnProvince;

    private String columnCity;

    private String columnCounty;

    private String realName;

    private String idcard;

    private String idcardPic;

    private String email;

    private String mobile;

    private String otherPic;

    private String companyName;

    private String licensePic;

    private String siteLink;

    private String kgcolumnname;// 栏目名称

    private String profileavatar;// 专栏头像

    private long article_id; // 文章ID

    private long article_title;// 文章抬头

    private int bowse_num;// 浏览数

    private String update_date;// 修改时间

    private String createDate;

    private String realnameAuthed;

    private String tradepasswordSet;

    private long thumbupNum;// 作者点赞数

    private String userRole;// 角色

    private String identityTag; //身份标识
    private Integer realAuthedTag; //实名标签 0:无，1：有
    private Integer vipTag; //是否有大V标签 0：无，1：有

    private Integer columnAuthed;// 专栏是否认证 0 未认证 1 已认证

    private int concernedStatus;// 作者关注当前用户状态 0未关注 1已关注

    private int concernUserStatus;// 当前登录用户关注作者状态 0未关注 1已关注

	private long fansCount;//粉丝数量

	public long getFansCount() {
		return fansCount;
	}

	public void setFansCount(long fansCount) {
		this.fansCount = fansCount;
	}

	public int getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(int concernedStatus) {
        this.concernedStatus = concernedStatus;
    }

    public int getConcernUserStatus() {
        return concernUserStatus;
    }

    public void setConcernUserStatus(int concernUserStatus) {
        this.concernUserStatus = concernUserStatus;
    }

    public Integer getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(Integer columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public Integer getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(Integer realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public Integer getVipTag() {
        return vipTag;
    }

    public void setVipTag(Integer vipTag) {
        this.vipTag = vipTag;
    }

    /**
     * @return the userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole
     *            the userRole to set
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the thumbupNum
     */
    public long getThumbupNum() {
        return thumbupNum;
    }

    /**
     * @param thumbupNum
     *            the thumbupNum to set
     */
    public void setThumbupNum(long thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRealnameAuthed() {
        return realnameAuthed;
    }

    public void setRealnameAuthed(String realnameAuthed) {
        this.realnameAuthed = realnameAuthed;
    }

    public String getTradepasswordSet() {
        return tradepasswordSet;
    }

    public void setTradepasswordSet(String tradepasswordSet) {
        this.tradepasswordSet = tradepasswordSet;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the idcardFront
     */
    public String getIdcardFront() {
        return idcardFront;
    }

    /**
     * @param idcardFront
     *            the idcardFront to set
     */
    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    /**
     * @return the idcardBack
     */
    public String getIdcardBack() {
        return idcardBack;
    }

    /**
     * @param idcardBack
     *            the idcardBack to set
     */
    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pbowsenum
     */
    public int getPbowsenum() {
        return pbowsenum;
    }

    /**
     * @param pbowsenum
     *            the pbowsenum to set
     */
    public void setPbowsenum(int pbowsenum) {
        this.pbowsenum = pbowsenum;
    }

    /**
     * @return the kgcolumnname
     */
    public String getKgcolumnname() {
        return kgcolumnname;
    }

    /**
     * @param kgcolumnname
     *            the kgcolumnname to set
     */
    public void setKgcolumnname(String kgcolumnname) {
        this.kgcolumnname = kgcolumnname;
    }

    /**
     * @return the profileavatar
     */
    public String getProfileavatar() {
        return profileavatar;
    }

    /**
     * @param profileavatar
     *            the profileavatar to set
     */
    public void setProfileavatar(String profileavatar) {
        this.profileavatar = profileavatar;
    }

    /**
     * @return the article_id
     */
    public long getArticle_id() {
        return article_id;
    }

    /**
     * @param article_id
     *            the article_id to set
     */
    public void setArticle_id(long article_id) {
        this.article_id = article_id;
    }

    /**
     * @return the article_title
     */
    public long getArticle_title() {
        return article_title;
    }

    /**
     * @param article_title
     *            the article_title to set
     */
    public void setArticle_title(long article_title) {
        this.article_title = article_title;
    }

    /**
     * @return the bowse_num
     */
    public int getBowse_num() {
        return bowse_num;
    }

    /**
     * @param bowse_num
     *            the bowse_num to set
     */
    public void setBowse_num(int bowse_num) {
        this.bowse_num = bowse_num;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    /**
     * @return the list
     */
    public List<ArticleResponse> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<ArticleResponse> list) {
        this.list = list;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getRoleDefine() {
        return roleDefine;
    }

    public void setRoleDefine(String roleDefine) {
        this.roleDefine = roleDefine;
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

    public String getColumnCountry() {
        return columnCountry;
    }

    public void setColumnCountry(String columnCountry) {
        this.columnCountry = columnCountry;
    }

    public String getColumnProvince() {
        return columnProvince;
    }

    public void setColumnProvince(String columnProvince) {
        this.columnProvince = columnProvince;
    }

    public String getColumnCity() {
        return columnCity;
    }

    public void setColumnCity(String columnCity) {
        this.columnCity = columnCity;
    }

    public String getColumnCounty() {
        return columnCounty;
    }

    public void setColumnCounty(String columnCounty) {
        this.columnCounty = columnCounty;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardPic() {
        return idcardPic;
    }

    public void setIdcardPic(String idcardPic) {
        this.idcardPic = idcardPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtherPic() {
        return otherPic;
    }

    public void setOtherPic(String otherPic) {
        this.otherPic = otherPic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    /**
     * @return the collect
     */
    public long getCollect() {
        return collect;
    }

    /**
     * @param collect
     *            the collect to set
     */
    public void setCollect(long collect) {
        this.collect = collect;
    }

    /**
     * @return the comments
     */
    public int getComments() {
        return comments;
    }

    /**
     * @param comments
     *            the comments to set
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return the artsum
     */
    public long getArtsum() {
        return artsum;
    }

    /**
     * @param artsum
     *            the artsum to set
     */
    public void setArtsum(long artsum) {
        this.artsum = artsum;
    }

    public UserProfileResponse() {
        super();
    }

}