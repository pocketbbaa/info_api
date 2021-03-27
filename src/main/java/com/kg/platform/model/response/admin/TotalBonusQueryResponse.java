package com.kg.platform.model.response.admin;

public class TotalBonusQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8549770256464921903L;

    private String totalTv;

    private String totalKg;


    public String getTotalTv() {
        return totalTv;
    }

    public void setTotalTv(String totalTv) {
        this.totalTv = totalTv;
    }

    public String getTotalKg() {
        return totalKg;
    }

    public void setTotalKg(String totalKg) {
        this.totalKg = totalKg;
    }


}
