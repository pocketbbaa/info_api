package com.kg.platform.model.in;

/**
 * 分页参数
 * 
 * @author gavine
 *
 */
public class PagerInModel {

    private int limit;

    private int start;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
