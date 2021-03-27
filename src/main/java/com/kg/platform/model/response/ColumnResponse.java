package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.kg.platform.model.out.ColumnOutModel;

/**
 * 首页栏目出参
 * 
 * @author think
 *
 */
public class ColumnResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6143196004032857312L;

    private String article_title;

    private String article_image;

    private Integer article_id;

    private List<ArticleResponse> articleResponses;

    private List<ColumnOutModel> listout;

    private List<ColumnResponse> list;

    private Integer columnId;

    private Integer prevColumn;

    private String columnName;

    private Long navigatorDisplay;

    private Boolean displayStatus;

    private Integer columnOrder;

    private Integer displayMode;

    private String seoTitle;

    private String seoKeyword;

    private String seoDescription;

    private Date createDate;

    private Integer createUser;

    private Date updateDate;

    private Integer updateUser;

    private Byte columnLevel;

    private String columnUrlName;

	public String getColumnUrlName() {
		return columnUrlName;
	}

	public void setColumnUrlName(String columnUrlName) {
		this.columnUrlName = columnUrlName;
	}

	/**
     * @return the list
     */
    public List<ColumnResponse> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<ColumnResponse> list) {
        this.list = list;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getPrevColumn() {
        return prevColumn;
    }

    public void setPrevColumn(Integer prevColumn) {
        this.prevColumn = prevColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(Long navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public Boolean getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Byte getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Byte columnLevel) {
        this.columnLevel = columnLevel;
    }

    /**
     * @return the articleResponses
     */
    public List<ArticleResponse> getArticleResponses() {
        return articleResponses;
    }

    /**
     * @param articleResponses
     *            the articleResponses to set
     */
    public void setArticleResponses(List<ArticleResponse> articleResponses) {
        this.articleResponses = articleResponses;
    }

    /**
     * @return the article_title
     */
    public String getArticle_title() {
        return article_title;
    }

    /**
     * @param article_title
     *            the article_title to set
     */
    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    /**
     * @return the article_image
     */
    public String getArticle_image() {
        return article_image;
    }

    /**
     * @param article_image
     *            the article_image to set
     */
    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }

    /**
     * @return the article_id
     */
    public Integer getArticle_id() {
        return article_id;
    }

    /**
     * @param article_id
     *            the article_id to set
     */
    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }

    public ColumnResponse(String article_title, String article_image, Integer article_id,
            List<ArticleResponse> articleResponses, List<ColumnOutModel> listout, Integer columnId, Integer prevColumn,
            String columnName, Long navigatorDisplay, Boolean displayStatus, Integer columnOrder, Integer displayMode,
            String seoTitle, String seoKeyword, String seoDescription, Date createDate, Integer createUser,
            Date updateDate, Integer updateUser, Byte columnLevel) {
        super();
        this.article_title = article_title;
        this.article_image = article_image;
        this.article_id = article_id;
        this.articleResponses = articleResponses;
        this.listout = listout;
        this.columnId = columnId;
        this.prevColumn = prevColumn;
        this.columnName = columnName;
        this.navigatorDisplay = navigatorDisplay;
        this.displayStatus = displayStatus;
        this.columnOrder = columnOrder;
        this.displayMode = displayMode;
        this.seoTitle = seoTitle;
        this.seoKeyword = seoKeyword;
        this.seoDescription = seoDescription;
        this.createDate = createDate;
        this.createUser = createUser;
        this.updateDate = updateDate;
        this.updateUser = updateUser;
        this.columnLevel = columnLevel;
    }

    /**
     * @return the listout
     */
    public List<ColumnOutModel> getListout() {
        return listout;
    }

    /**
     * @param listout
     *            the listout to set
     */
    public void setListout(List<ColumnOutModel> listout) {
        this.listout = listout;
    }

    public ColumnResponse(List<ColumnOutModel> listout) {
        super();
        this.listout = listout;
    }

    public ColumnResponse() {
        super();
    }

}
