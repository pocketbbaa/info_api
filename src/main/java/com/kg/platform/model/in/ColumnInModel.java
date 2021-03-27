package com.kg.platform.model.in;

public class ColumnInModel {
    private Integer columnId;

    private String navigatorDisplay;

    private Integer prevColumn;

    private long secondColumn;

    private String name;

    private String columnLevel;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * @return the prevColumn
     */
    public Integer getPrevColumn() {
        return prevColumn;
    }

    /**
     * @param prevColumn
     *            the prevColumn to set
     */
    public void setPrevColumn(Integer prevColumn) {
        this.prevColumn = prevColumn;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(String navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public ColumnInModel(Integer columnId, String integer) {
        super();
        this.columnId = columnId;
        this.navigatorDisplay = integer;
    }

    public ColumnInModel(String navigatorDisplay) {
        super();
        this.navigatorDisplay = navigatorDisplay;
    }

    public ColumnInModel() {
        super();
    }

}