package com.kg.platform.model.out;

import java.util.Date;
import java.util.List;

import com.kg.platform.model.response.ArticleResponse;

public class UserProfileOutModel {

    private String idcardFront;

    private String idcardBack;

    private int total;// 总数

    private long artsum;// 文章数

    private long collect;

    private int comments;

    private int pbowsenum;

    private Integer kgcolumnId;// 栏目Id

    private String kgcolumnname;// 栏目名称

    private String inviteCode;// 邀请码

    private String profileavatar;// 专栏头像

    private String article_id; // 文章ID

    private String article_title;// 文章抬头

    private int bowse_num;// 浏览数

    private Date update_date;// 修改时间

    private String columnintro;// 专栏介绍

    private String userRole;// 用户角色

    private String secondColumn;// 文章所属二级栏目

    private String username;

    private Date createDate;

    List<ArticleResponse> list;

    private Long userId;

    private int roleId;

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

    private String createUser;

    private String realnameAuthed;

    private String tradepasswordSet;

    private Integer columnAuthed;// 专栏是否认证 0 未认证 1 已认证

    private Date auditDate;// 审核时间

    private Integer articleImgSize;// 文章封面图大小

    private int concernUserStatus;

    private int concernedStatus;


    private Integer registChannel;

    private String articleImage;

    private Integer publishKind;

    private Integer columnLevel; //专栏等级

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public Integer getRegistChannel() {
        return registChannel;
    }

    public void setRegistChannel(Integer registChannel) {
        this.registChannel = registChannel;
    }

    private int vipTag;


    public Integer getArticleImgSize() {
        return articleImgSize;
    }

    public void setArticleImgSize(Integer articleImgSize) {
        this.articleImgSize = articleImgSize;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(Integer columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public int getConcernUserStatus() {
        return concernUserStatus;
    }

    public void setConcernUserStatus(int concernUserStatus) {
        this.concernUserStatus = concernUserStatus;
    }

    public int getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(int concernedStatus) {
        this.concernedStatus = concernedStatus;

    }

    /**
     * @return the secondColumn
     */
    public String getSecondColumn() {
        return secondColumn;
    }

    /**
     * @param secondColumn the secondColumn to set
     */
    public void setSecondColumn(String secondColumn) {
        this.secondColumn = secondColumn;
    }

    /**
     * @return the userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
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

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
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
     * @param idcardFront the idcardFront to set
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
     * @param idcardBack the idcardBack to set
     */
    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the artsum
     */
    public long getArtsum() {
        return artsum;
    }

    /**
     * @param artsum the artsum to set
     */
    public void setArtsum(long artsum) {
        this.artsum = artsum;
    }

    /**
     * @return the pbowsenum
     */
    public int getPbowsenum() {
        return pbowsenum;
    }

    /**
     * @param pbowsenum the pbowsenum to set
     */
    public void setPbowsenum(int pbowsenum) {
        this.pbowsenum = pbowsenum;
    }

    /**
     * @return the collect
     */
    public long getCollect() {
        return collect;
    }

    /**
     * @param collect the collect to set
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
     * @param comments the comments to set
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return the columnintro
     */
    public String getColumnintro() {
        return columnintro;
    }

    /**
     * @param columnintro the columnintro to set
     */
    public void setColumnintro(String columnintro) {
        this.columnintro = columnintro;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * /**
     *
     * @return the profileavatar
     */
    public String getProfileavatar() {
        return profileavatar;
    }

    /**
     * @return the kgcolumnname
     */
    public String getKgcolumnname() {
        return kgcolumnname;
    }

    /**
     * @param kgcolumnname the kgcolumnname to set
     */
    public void setKgcolumnname(String kgcolumnname) {
        this.kgcolumnname = kgcolumnname;
    }

    /**
     * @param profileavatar the profileavatar to set
     */

    public void setProfileavatar(String profileavatar) {
        this.profileavatar = profileavatar;
    }

    /**
     * @return the article_id
     */
    public String getArticle_id() {
        return article_id;
    }

    /**
     * @param article_id the article_id to set
     */
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    /**
     * @return the article_title
     */
    public String getArticle_title() {
        return article_title;
    }

    /**
     * @param article_title the article_title to set
     */
    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    /**
     * @return the bowse_num
     */
    public int getBowse_num() {
        return bowse_num;
    }

    /**
     * @param bowse_num the bowse_num to set
     */
    public void setBowse_num(int bowse_num) {
        this.bowse_num = bowse_num;
    }

    /**
     * @return the update_date
     */
    public Date getUpdate_date() {
        return update_date;
    }

    /**
     * @param update_date the update_date to set
     */
    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    /**
     * @return the list
     */
    public List<ArticleResponse> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<ArticleResponse> list) {
        this.list = list;
    }

    public Integer getKgcolumnId() {
        return kgcolumnId;
    }

    public void setKgcolumnId(Integer kgcolumnId) {
        this.kgcolumnId = kgcolumnId;
    }

    public UserProfileOutModel() {
        super();
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }
}