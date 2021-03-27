package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * 栏目入参
 * 
 * @author think
 *
 */
public class ColumnRequest implements Serializable {

    private static final long serialVersionUID = 6455929167310179497L;

    private Integer navigatorDisplay;

    private Integer columnId;

    private Integer prevColumn;

    private String columnLevel;

    /**
     * @return the navigatorDisplay
     */
    public Integer getNavigatorDisplay() {
        return navigatorDisplay;
    }

    /**
     * @return the columnLevel
     */
    public String getColumnLevel() {
        return columnLevel;
    }

    /**
     * @param columnLevel
     *            the columnLevel to set
     */
    public void setColumnLevel(String columnLevel) {
        this.columnLevel = columnLevel;
    }

    /**
     * @param navigatorDisplay
     *            the navigatorDisplay to set
     */
    public void setNavigatorDisplay(Integer navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
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

}
