package com.kg.platform.model.request;

import java.io.Serializable;

public class SampleRequest implements Serializable {

    private static final long serialVersionUID = -5303016964624884973L;

    private String testreq;

    public String getTestreq() {
        return testreq;
    }

    public void setTestreq(String testreq) {
        this.testreq = testreq;
    }

}
