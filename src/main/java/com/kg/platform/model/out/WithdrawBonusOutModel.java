package com.kg.platform.model.out;

import java.io.Serializable;

public class WithdrawBonusOutModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3115070521289149529L;

    private Long userId;
    
    private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    
}