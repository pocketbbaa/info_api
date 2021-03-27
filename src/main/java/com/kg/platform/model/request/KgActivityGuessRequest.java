package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class KgActivityGuessRequest implements Serializable {
    /**
     * 竞猜ID
     */
    private Integer guessId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 竞猜时间
     */
    private Date guessDate;

    /**
     * 赛事ID
     */
    private Integer competionId;

    /**
     * 支持的球队ID
     */
    private Integer supportTeamId;

    /**
     * 预留字段
     */
    private String remark;

    /**
     * 竞猜信息 json格式 [{"supportTeamId":41,"competionId":123456}]
     */
    private String guessInfo;

    private static final long serialVersionUID = 1L;

    public Integer getGuessId() {
        return guessId;
    }

    public void setGuessId(Integer guessId) {
        this.guessId = guessId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getGuessDate() {
        return guessDate;
    }

    public void setGuessDate(Date guessDate) {
        this.guessDate = guessDate;
    }

    public Integer getCompetionId() {
        return competionId;
    }

    public void setCompetionId(Integer competionId) {
        this.competionId = competionId;
    }

    public Integer getSupportTeamId() {
        return supportTeamId;
    }

    public void setSupportTeamId(Integer supportTeamId) {
        this.supportTeamId = supportTeamId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGuessInfo() {
        return guessInfo;
    }

    public void setGuessInfo(String guessInfo) {
        this.guessInfo = guessInfo;
    }

}