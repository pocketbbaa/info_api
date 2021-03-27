package com.kg.platform.model;

import org.springframework.stereotype.Component;

@Component
public class EmailLinkConfig {

    private String address;
    private String url;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
