package com.kg.platform.model.response;

import java.io.Serializable;

public class SiteBaseInfoResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6444544908895924980L;

    private String siteLogo;

    private String siteIcon;

    private String siteTitle;

    private String siteDesc;

    private String siteKeyword;

    private String siteCopyright;

    private String siteLicense;

    private String contactPhone;

    private String contactEmail;

    public String getSiteLogo() {
        return siteLogo;
    }

    public void setSiteLogo(String siteLogo) {
        this.siteLogo = siteLogo;
    }

    public String getSiteIcon() {
        return siteIcon;
    }

    public void setSiteIcon(String siteIcon) {
        this.siteIcon = siteIcon;
    }

    public String getSiteTitle() {
        return siteTitle;
    }

    public void setSiteTitle(String siteTitle) {
        this.siteTitle = siteTitle;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getSiteKeyword() {
        return siteKeyword;
    }

    public void setSiteKeyword(String siteKeyword) {
        this.siteKeyword = siteKeyword;
    }

    public String getSiteCopyright() {
        return siteCopyright;
    }

    public void setSiteCopyright(String siteCopyright) {
        this.siteCopyright = siteCopyright;
    }

    public String getSiteLicense() {
        return siteLicense;
    }

    public void setSiteLicense(String siteLicense) {
        this.siteLicense = siteLicense;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
