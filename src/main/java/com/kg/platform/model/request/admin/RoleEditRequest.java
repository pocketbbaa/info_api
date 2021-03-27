package com.kg.platform.model.request.admin;

/**
 * 编辑角色入参
 */
public class RoleEditRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 7050784241033445752L;

    private Integer id;

    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
