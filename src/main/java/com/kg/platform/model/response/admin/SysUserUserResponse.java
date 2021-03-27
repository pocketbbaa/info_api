package com.kg.platform.model.response.admin;

/**
 * 系统账号关联查询出参
 */
public class SysUserUserResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -5085565631269973785L;

    private String relId;

    private String mobile;

    private String kgUserId;

    private String kgUsername;

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKgUserId() {
        return kgUserId;
    }

    public void setKgUserId(String kgUserId) {
        this.kgUserId = kgUserId;
    }

    public String getKgUsername() {
        return kgUsername;
    }

    public void setKgUsername(String kgUsername) {
        this.kgUsername = kgUsername;
    }

}
