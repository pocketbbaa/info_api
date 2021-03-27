package com.kg.platform.model.request.admin;

/**
 * 热门关键词查询入参
 */
public class KeywordQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -556352912078405308L;

    private String name;

    private Boolean status;

    private Integer channel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
