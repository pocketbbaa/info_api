package com.kg.platform.common.entity;

import java.io.Serializable;

import com.kg.platform.common.utils.TraceUtil;

public class MonitorEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String traceId;

    private MonitorEntity() {
        this.traceId = TraceUtil.getTraceId();
    }

    public static MonitorEntity getInstance() {
        return new MonitorEntity();
    }

    public String getTraceId() {
        return traceId;
    }
}
