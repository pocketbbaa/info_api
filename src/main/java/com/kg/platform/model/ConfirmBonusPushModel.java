package com.kg.platform.model;

public class ConfirmBonusPushModel {

    private String userId;
    private String message;

    public ConfirmBonusPushModel() {
    }

    public ConfirmBonusPushModel(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
