package com.kg.platform.model.request.admin;

/**
 * 角色查询入参
 */
public class RoleQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 7667481216523039936L;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
