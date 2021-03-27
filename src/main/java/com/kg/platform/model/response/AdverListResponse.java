package com.kg.platform.model.response;

import java.io.Serializable;

/**
 * 首页图片推荐位出参
 * 
 * @author think
 *
 */
public class AdverListResponse implements Serializable {

   private  String location;

   private  AdverResponse  adInfo;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AdverResponse getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(AdverResponse adInfo) {
        this.adInfo = adInfo;
    }
}
