package com.kg.platform.model.request.admin;

/**
 * 用户信息默认设置
 */
public class AdminUserInfoSetRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 451840098241223120L;

    private String info;

    private Boolean status;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
