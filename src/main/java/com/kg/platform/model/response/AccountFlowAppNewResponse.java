package com.kg.platform.model.response;

import java.math.BigDecimal;

/**
 * app用户账单流水model
 */
public class AccountFlowAppNewResponse {

    private String accountFlowId; //账单ID
    private String flowDate; //账单时间
    private String typeName; //账单类型
    private String amount; //币数量
    private Integer freezeState; //是否冻结账户产生流水 0：否，1：是
    private String remark; //备注
    private String flowDetail; //流水信息

    public Integer getFreezeState() {
        return freezeState;
    }

    public void setFreezeState(Integer freezeState) {
        this.freezeState = freezeState;
    }

    public String getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(String accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public String getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(String flowDate) {
        this.flowDate = flowDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFlowDetail() {
        return flowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        this.flowDetail = flowDetail;
    }
}
