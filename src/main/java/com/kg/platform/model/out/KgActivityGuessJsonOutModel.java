package com.kg.platform.model.out;

import java.io.Serializable;

/**
 * @author
 */
public class KgActivityGuessJsonOutModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8723279314748478500L;

    /**
     * 赛事ID
     */
    private Integer competionId;

    /**
     * 
     * 支持的球队ID
     */
    private Integer supportTeamId;

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

}