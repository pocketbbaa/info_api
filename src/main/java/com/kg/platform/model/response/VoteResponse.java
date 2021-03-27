package com.kg.platform.model.response;

public class VoteResponse {

    private Long voteId;

    private String companyName;

    private String companyLogo;

    private Integer voteNum;

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

}