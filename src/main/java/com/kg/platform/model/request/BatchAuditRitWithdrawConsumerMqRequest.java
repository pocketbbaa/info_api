package com.kg.platform.model.request;

import com.kg.platform.dao.entity.SysUser;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17.
 */
public class BatchAuditRitWithdrawConsumerMqRequest {
    List<Long> withdrawFlowIdList;

    AccountWithDrawRequest request;

    SysUser sysUser;

    long now;

    public List<Long> getWithdrawFlowIdList() {
        return withdrawFlowIdList;
    }

    public void setWithdrawFlowIdList(List<Long> withdrawFlowIdList) {
        this.withdrawFlowIdList = withdrawFlowIdList;
    }

    public AccountWithDrawRequest getRequest() {
        return request;
    }

    public void setRequest(AccountWithDrawRequest request) {
        this.request = request;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }
}
