package com.kg.platform.model.response.admin;

import java.util.List;

/**
 * 栏目查询出参 排序、栏目名、栏目级别、添加人、添加时间、导航栏显示、包含文章数、栏目ID
 */
public class ColumnQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 3030469729865072194L;

    private String columnId;

    private Integer columnOrder;

    private String columnName;

    private String columnLevelDisplay;

    private Integer columnLevel;

    private String createUsername;

    private String createDate;

    private Integer navigatorDisplay;

    private Integer articleNum;

    private List<ColumnQueryResponse> children;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnLevelDisplay() {
        return columnLevelDisplay;
    }

    public void setColumnLevelDisplay(String columnLevelDisplay) {
        this.columnLevelDisplay = columnLevelDisplay;
    }

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(Integer navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public List<ColumnQueryResponse> getChildren() {
        return children;
    }

    public void setChildren(List<ColumnQueryResponse> children) {
        this.children = children;
    }
}
