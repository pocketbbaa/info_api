package com.kg.platform.model.request.admin;

/**
 * 认证专栏请求参数
 */
public class CertificationColumnRequest {

    private String userId;
    private String columnIdentity;  //专栏身份

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColumnIdentity() {
        return columnIdentity;
    }

    public void setColumnIdentity(String columnIdentity) {
        this.columnIdentity = columnIdentity;
    }
}
