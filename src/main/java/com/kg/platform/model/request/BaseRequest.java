package com.kg.platform.model.request;

import java.io.Serializable;

public class BaseRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2994034400134143401L;

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
