package com.kg.platform.model.response.admin;

public class BusinessTypeQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -1818096982270178784L;

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
