package com.kg.platform.common.mq;

/**
 * Created by Administrator on 2018/3/26.
 */
public class ConsumeMessge {

    public static String mongoTable = "kg_consume_message";

    private Long _id;

    private Integer status;

    private Integer type;

    private Long articleId;

    private Integer serviceType;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

}
