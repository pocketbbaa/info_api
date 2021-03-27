package com.kg.platform.model.request;

import java.math.BigDecimal;
import java.util.Date;

public class AccountRequest {

    private Long flowId;

    private Long relationFlowId;

    private Long accountId;

    private Long userId;

    private Long browseUserId;

    private Long subUserId;

    private BigDecimal balance;

    private BigDecimal frozenBalance;

    private Byte status;

    private String txAddress;

    private Date createDate;

    private String txPassword;

    private Long articleId;

    private String bonusType;// 奖励类型

    private String bonusSecondType;

    private String FlowDetail;

    private String platFlowDetail;

    private String userName;

    private String UserPhone;

    private String userEmail;

    private BigDecimal amount;

    private String source;

    private BigDecimal rewardTv; // 打赏数量

    private Long rewardUid; // 打赏用户

    private Integer coinType;// 币种类型

    private Integer businessTypeId;

    private Integer flowStatus;

    private Long  tributeId;

    private Long  deleteUserFlowId;

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    /**
     * @return the bonusSecondType
     */
    public String getBonusSecondType() {
        return bonusSecondType;
    }

    /**
     * @param bonusSecondType
     *            the bonusSecondType to set
     */
    public void setBonusSecondType(String bonusSecondType) {
        this.bonusSecondType = bonusSecondType;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the bonusType
     */
    public String getBonusType() {
        return bonusType;
    }

    /**
     * @param bonusType
     *            the bonusType to set
     */
    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
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

    public BigDecimal getRewardTv() {
        return rewardTv;
    }

    public void setRewardTv(BigDecimal rewardTv) {
        this.rewardTv = rewardTv;
    }

    public Long getRewardUid() {
        return rewardUid;
    }

    public void setRewardUid(Long rewardUid) {
        this.rewardUid = rewardUid;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getFlowDetail() {
        return FlowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        FlowDetail = flowDetail;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public Long getRelationFlowId() {
        return relationFlowId;
    }

    public void setRelationFlowId(Long relationFlowId) {
        this.relationFlowId = relationFlowId;
    }

    public String getPlatFlowDetail() {
        return platFlowDetail;
    }

    public void setPlatFlowDetail(String platFlowDetail) {
        this.platFlowDetail = platFlowDetail;
    }

    public Long getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Long subUserId) {
        this.subUserId = subUserId;
    }

    public Long getBrowseUserId() {
        return browseUserId;
    }

    public void setBrowseUserId(Long browseUserId) {
        this.browseUserId = browseUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public Long getTributeId() {
        return tributeId;
    }

    public void setTributeId(Long tributeId) {
        this.tributeId = tributeId;
    }

    public Long getDeleteUserFlowId() {
        return deleteUserFlowId;
    }

    public void setDeleteUserFlowId(Long deleteUserFlowId) {
        this.deleteUserFlowId = deleteUserFlowId;
    }
}
