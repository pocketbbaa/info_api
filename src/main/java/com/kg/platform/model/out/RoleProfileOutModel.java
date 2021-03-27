package com.kg.platform.model.out;

public class RoleProfileOutModel {
    private Integer roleProfileId;

    private Integer roleId;

    private String profileName;

    private String profileType;

    private String defaultValue;

    private Boolean displayStatus;

    private Boolean requiredStatus;

    public Integer getRoleProfileId() {
        return roleProfileId;
    }

    public void setRoleProfileId(Integer roleProfileId) {
        this.roleProfileId = roleProfileId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Boolean getRequiredStatus() {
        return requiredStatus;
    }

    public void setRequiredStatus(Boolean requiredStatus) {
        this.requiredStatus = requiredStatus;
    }
}