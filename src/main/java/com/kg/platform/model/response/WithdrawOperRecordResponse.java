package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/9/10.
 */
public class WithdrawOperRecordResponse {

    private String operId;//操作ID

    private String withdrawFlowId;//工单号

    private Integer operType;//操作类型

    private String operTypeName;//操作类型名

    private String operUserId;//操作用户ID

    private String operUserName;//操作用户名

    private String operDate;//操作时间

    private String txId;//交易号

    private String remark;//备注

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(String withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getOperTypeName() {
        return operTypeName;
    }

    public void setOperTypeName(String operTypeName) {
        this.operTypeName = operTypeName;
    }

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
