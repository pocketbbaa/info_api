package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 实名认证/专栏作者奖励查询出参
 */
public class RealnameQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4389132702591588908L;

    private String userId;

    private String userName;

    private String mobile;

    private Integer userRole;

    private String userRoleDisplay;

    private Integer level;

    private String levelDisplay;

    private Double bonusValue;

    private Date bonusDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public Double getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(Double bonusValue) {
        this.bonusValue = bonusValue;
    }

    public Date getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(Date bonusDate) {
        this.bonusDate = bonusDate;
    }

}
