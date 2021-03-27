package com.kg.platform.model.request;

import java.io.Serializable;

public class SendsmsemailRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7902722525955517244L;

    private String userEmail;

    private String userMobile;

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     *            the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the userMobile
     */
    public String getUserMobile() {
        return userMobile;
    }

    /**
     * @param userMobile
     *            the userMobile to set
     */
    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public SendsmsemailRequest() {
        super();
    }

}
