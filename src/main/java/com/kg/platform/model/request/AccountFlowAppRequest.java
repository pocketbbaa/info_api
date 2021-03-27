package com.kg.platform.model.request;

/**
 * 账户流水请求参数
 */
public class AccountFlowAppRequest {

    private int type;          //1:钛值 2：钛小白
    private Long userId;       //用户ID
    private int currentPage;
    private int classify;      //分类 0：全部，1：奖励，2：打赏，3：转入，4：转出，5：进贡
    private String startDate;
    private String endDate;

    private Long accountFlowId;

    public Long getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(Long accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
