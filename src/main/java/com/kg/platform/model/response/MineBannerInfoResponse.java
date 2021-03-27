package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/6/7.
 */
public class MineBannerInfoResponse {
    public static final String SETTING_KEY = "mine_banner_info";
    private String mineBannerImg;
    private String mineBannerLink;
    private String mineBannerType;

    public String getMineBannerImg() {
        return mineBannerImg;
    }

    public void setMineBannerImg(String mineBannerImg) {
        this.mineBannerImg = mineBannerImg;
    }

    public String getMineBannerLink() {
        return mineBannerLink;
    }

    public void setMineBannerLink(String mineBannerLink) {
        this.mineBannerLink = mineBannerLink;
    }

    public String getMineBannerType() {
        return mineBannerType;
    }

    public void setMineBannerType(String mineBannerType) {
        this.mineBannerType = mineBannerType;
    }
}
