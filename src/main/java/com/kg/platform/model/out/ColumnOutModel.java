package com.kg.platform.model.out;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页栏目service出参
 * 
 * @author think
 *
 */
public class ColumnOutModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6143196004032857312L;

    private String article_title;

    private String article_image;

    private String article_id;

    private Integer columnId;

    private Integer prevColumn;

    private String columnName;

    private long navigatorDisplay;

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

    private long secondColumn;

    private String columnUrlName;

    public String getColumnUrlName() {
        return columnUrlName;
    }

    public void setColumnUrlName(String columnUrlName) {
        this.columnUrlName = columnUrlName;
    }

    /**
     * @return the secondColumn
     */
    public long getSecondColumn() {
        return secondColumn;
    }

    /**
     * @param secondColumn
     *            the secondColumn to set
     */
    public void setSecondColumn(long secondColumn) {
        this.secondColumn = secondColumn;
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

    public long getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(long navigatorDisplay) {
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
    public String getArticle_id() {
        return article_id;
    }

    /**
     * @param article_id
     *            the article_id to set
     */
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public ColumnOutModel(String article_title, String article_image, String article_id, Integer columnId,
            Integer prevColumn, String columnName, long navigatorDisplay, Boolean displayStatus, Integer columnOrder,
            Integer displayMode, String seoTitle, String seoKeyword, String seoDescription, Date createDate,
            Integer createUser, Date updateDate, Integer updateUser, Byte columnLevel) {
        super();
        this.article_title = article_title;
        this.article_image = article_image;
        this.article_id = article_id;
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

    public ColumnOutModel() {
        super();
    }

}
