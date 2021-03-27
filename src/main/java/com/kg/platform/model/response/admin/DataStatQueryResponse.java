package com.kg.platform.model.response.admin;

/**
 * 数据统计出参
 */
public class DataStatQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4290284085906169924L;

    private String name;

    private String time;

    private Integer value;

    private Integer collectNum;

    private Integer shareNum;

    private Integer browseNum;

    private Integer personal;

    private Integer media;

    private Integer articleNum;

    private Integer commentNum;

    private Double avgNum;

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getPersonal() {
        return personal;
    }

    public void setPersonal(Integer personal) {
        this.personal = personal;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public Double getAvgNum() {
        return avgNum;
    }

    public void setAvgNum(Double avgNum) {
        this.avgNum = avgNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
