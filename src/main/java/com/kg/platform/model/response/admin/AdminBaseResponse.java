package com.kg.platform.model.response.admin;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class AdminBaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7728935791406489702L;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
