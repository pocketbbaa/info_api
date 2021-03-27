package com.kg.platform.model.request;

/**
 * Created by Administrator on 2018/9/10.
 */
public class WithdrawOperRecordRequest {

    private Long operId;//操作ID

    private Long withdrawFlowId;//工单号

    private Integer operType;//操作类型

    private String operTypeName;//操作类型名

    private Long operUserId;//操作用户ID

    private String operUserName;//操作用户名

    private Long operDate;//操作时间

    private String txId;//交易号

    private String remark;//备注

    public Long getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(Long withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public Long getOperId() {
        return operId;
    }

    public void setOperId(Long operId) {
        this.operId = operId;
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

    public Long getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(Long operUserId) {
        this.operUserId = operUserId;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public Long getOperDate() {
        return operDate;
    }

    public void setOperDate(Long operDate) {
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
