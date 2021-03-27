package com.kg.platform.model.in;

import java.math.BigDecimal;
import java.util.Date;

public class AccountInModel {
    private Long accountId;

    private Long userId;

    private BigDecimal balance;

    private BigDecimal frozenBalance;

    private BigDecimal txbBalance;

    private BigDecimal txbFrozenBalance;

    private Byte status;

    private String txAddress;

    private Date createDate;

    private String txPassword;

    private String flowDetails;

    private Long articleId;

    private BigDecimal ritBalance;

    private BigDecimal ritFrozenBalance;

    private BigDecimal amount;

    private String userMobile;

    private Integer type;//0：注册

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(BigDecimal ritBalance) {
        this.ritBalance = ritBalance;
    }

    public BigDecimal getRitFrozenBalance() {
        return ritFrozenBalance;
    }

    public void setRitFrozenBalance(BigDecimal ritFrozenBalance) {
        this.ritFrozenBalance = ritFrozenBalance;
    }

    /**
     * @return the articleId
     */
    public Long getArticleId() {
        return articleId;
    }

    /**
     * @param articleId
     *            the articleId to set
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public BigDecimal getTxbBalance() {
        return txbBalance;
    }

    public void setTxbBalance(BigDecimal txbBalance) {
        this.txbBalance = txbBalance;
    }

    public BigDecimal getTxbFrozenBalance() {
        return txbFrozenBalance;
    }

    public void setTxbFrozenBalance(BigDecimal txbFrozenBalance) {
        this.txbFrozenBalance = txbFrozenBalance;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTxAddress() {
        return txAddress;
    }

    public void setTxAddress(String txAddress) {
        this.txAddress = txAddress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTxPassword() {
        return txPassword;
    }

    public void setTxPassword(String txPassword) {
        this.txPassword = txPassword;
    }

    public String getFlowDetails() {
        return flowDetails;
    }

    public void setFlowDetails(String flowDetails) {
        this.flowDetails = flowDetails;
    }

}