package com.kg.platform.dao.entity;

import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.model.in.AccountInModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 待扣余额
 */
public class BalanceDeducted {

    private Long id;
    private Long userId;
    private Integer type;
    private Integer state;
    private Date createTime;
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static BalanceDeducted buildBalanceDeducted(AccountInModel inModel) {
        BalanceDeducted balanceDeducted = new BalanceDeducted();
        balanceDeducted.setUserId(inModel.getUserId());
        balanceDeducted.setType(CoinEnum.KG.getCode());
        balanceDeducted.setState(0);
        balanceDeducted.setCreateTime(new Date());
        balanceDeducted.setAmount(inModel.getBalance());
        return balanceDeducted;
    }
}
