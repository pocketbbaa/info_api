package com.kg.platform.model.mongoTable;

public class FansOperate {

    private Long _id;

    private String userId;

    private String concenUserId;

    // 来源 0取关 1关注
    private Integer operation;

    private String createTime;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConcenUserId() {
        return concenUserId;
    }

    public void setConcenUserId(String concenUserId) {
        this.concenUserId = concenUserId;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
