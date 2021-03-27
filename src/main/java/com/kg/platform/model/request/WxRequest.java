package com.kg.platform.model.request;

import java.io.Serializable;

public class WxRequest extends BaseRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8062763821845901020L;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
