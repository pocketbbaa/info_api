package com.kg.platform.model.request.admin;

/**
 * 添加/编辑系统关联账号入参
 */
public class SysUserUserRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -5692198454803556374L;

    private String kgUserId;

    private Integer relId;

    private Integer sysUserId;

    public String getKgUserId() {
        return kgUserId;
    }

    public void setKgUserId(String kgUserId) {
        this.kgUserId = kgUserId;
    }

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

}
