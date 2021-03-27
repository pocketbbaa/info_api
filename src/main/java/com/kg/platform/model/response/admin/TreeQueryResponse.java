package com.kg.platform.model.response.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.kg.platform.tree.annotation.Children;
import com.kg.platform.tree.annotation.ParentId;
import com.kg.platform.tree.annotation.TreeId;

/**
 * 属性结构出参
 */
public class TreeQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8267982272777851141L;

    @TreeId
    private Long id;

    @ParentId
    private Long parentId;

    private String name;

    @Children
    private List<TreeQueryResponse> children = new ArrayList<>();

    private String user;

    private Date createDate;

    private Integer columnLevel;

    private Integer navigatorDisplay;

    private Long articleCount;

    private Long secondArticleCount;

    private String columnUrlname;

    public String getColumnUrlname() {
        return columnUrlname;
    }

    public void setColumnUrlname(String columnUrlname) {
        this.columnUrlname = columnUrlname;
    }

    public Long getSecondArticleCount() {
        return secondArticleCount;
    }

    public void setSecondArticleCount(Long secondArticleCount) {
        this.secondArticleCount = secondArticleCount;
    }

    private Long columnOrder;

    private Integer displayMode;

    private String title;

    private String keyword;

    private String description;

    private Integer displayStatus;

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
    }

    public Long getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Long columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public Integer getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(Integer navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public Long getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Long articleCount) {
        this.articleCount = articleCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeQueryResponse> getChildren() {
        return children;
    }

    public void setChildren(List<TreeQueryResponse> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
