package com.kg.platform.model.request;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/9/5.
 */
public class AccountWithdrawFlowRitRequest {

    private BigDecimal withdrawAmount;//RIT提现数量

    private String toAddress;//提现地址

    private String remark;//备注

    private String tranPassword;//交易密码

    private String code;//验证码

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTranPassword() {
        return tranPassword;
    }

    public void setTranPassword(String tranPassword) {
        this.tranPassword = tranPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
