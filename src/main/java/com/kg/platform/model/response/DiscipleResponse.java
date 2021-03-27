package com.kg.platform.model.response;

import java.math.BigDecimal;

public class DiscipleResponse {

    // 徒弟uid
    private Long discipleUid;

    // 师傅uid
    private Long teacherUid;

    // 排名
    private String rank;

    // 头像
    private String headImage;

    // 名称
    private String name;

    // 最新进贡时间
    private String reConbTime;

    // 进贡量
    private BigDecimal contributions;

    // 我的奖赏
    private BigDecimal awards;

    // 今日进贡量
    private BigDecimal todayContb;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getReConbTime() {
        return reConbTime;
    }

    public void setReConbTime(String reConbTime) {
        this.reConbTime = reConbTime;
    }

    public BigDecimal getContributions() {
        return contributions;
    }

    public void setContributions(BigDecimal contributions) {
        this.contributions = contributions;
    }

    public Long getDiscipleUid() {
        return discipleUid;
    }

    public void setDiscipleUid(Long discipleUid) {
        this.discipleUid = discipleUid;
    }

    public BigDecimal getAwards() {
        return awards;
    }

    public void setAwards(BigDecimal awards) {
        this.awards = awards;
    }

    public BigDecimal getTodayContb() {
        return todayContb;
    }

    public void setTodayContb(BigDecimal todayContb) {
        this.todayContb = todayContb;
    }

    public Long getTeacherUid() {
        return teacherUid;
    }

    public void setTeacherUid(Long teacherUid) {
        this.teacherUid = teacherUid;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}