package com.kg.platform.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserRelationRequest extends BaseRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7209414187291803217L;

    /**
     * 
     */

    private Long userId;

    // 邀请人数
    private Long inviteCount;

    // 邀请码
    private String inviteCode;

    private Long relUserId;

    // 交易密码
    private String txPassword;

    private Long targetCount;

    private BigDecimal _bonus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Long inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getTxPassword() {
        return txPassword;
    }

    public void setTxPassword(String txPassword) {
        this.txPassword = txPassword;
    }

    public Long getRelUserId() {
        return relUserId;
    }

    public void setRelUserId(Long relUserId) {
        this.relUserId = relUserId;
    }

    public Long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Long targetCount) {
        this.targetCount = targetCount;
    }

    public BigDecimal get_bonus() {
        return _bonus;
    }

    public void set_bonus(BigDecimal _bonus) {
        this._bonus = _bonus;
    }

}
