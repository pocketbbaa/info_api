package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class KgActivityCompetionRequest implements Serializable {
    /**
     * 赛事ID
     */
    private Integer competionId;

    /**
     * 主场球队LOGO
     */
    private String homeTeamLogo;

    /**
     * 客场球队LOGO
     */
    private String guestTeamLogo;

    /**
     * 主场球队名
     */
    private String homeTeamName;

    /**
     * 主场球队编号
     */
    private Integer homeTeamId;

    /**
     * 客场球队名
     */
    private String guestTeamName;

    /**
     * 客场球队编号
     */
    private Integer guestTeamId;

    /**
     * 比赛时间
     */
    private Date competitionDate;

    private Long userId;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCompetionId() {
        return competionId;
    }

    public void setCompetionId(Integer competionId) {
        this.competionId = competionId;
    }

    public String getHomeTeamLogo() {
        return homeTeamLogo;
    }

    public void setHomeTeamLogo(String homeTeamLogo) {
        this.homeTeamLogo = homeTeamLogo;
    }

    public String getGuestTeamLogo() {
        return guestTeamLogo;
    }

    public void setGuestTeamLogo(String guestTeamLogo) {
        this.guestTeamLogo = guestTeamLogo;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public Integer getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(Integer guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }
}