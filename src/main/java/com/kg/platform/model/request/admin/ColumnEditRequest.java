package com.kg.platform.model.request.admin;

/**
 * 编辑栏目入参
 */
public class ColumnEditRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -7774455657833612813L;

    private Integer columnId;

    private Integer parentId;

    private String name;

    private Integer navigatorDisplay = 3;

    private Boolean displayStatus;

    private Integer order;

    private Integer displayMode;

    private String title;

    private String keyword;

    private String description;

    private String urlname;

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(Integer navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public Boolean getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
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
}
