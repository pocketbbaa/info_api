package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.google.common.collect.Lists;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.mongoTable.AccountWithdrawFlowRit;
import com.kg.platform.model.response.AccountWithdrawFlowRitResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */
public class AccountWithdrawFlowRitExcel extends BaseRowModel {

    @ExcelProperty(value = "提现工单号" ,index = 0)
    private String withdrawFlowId;//提现工单ID
    @ExcelProperty(value = "手机号" ,index = 1)
    private String userPhone;//用户手机号
    @ExcelProperty(value = "昵称" ,index = 2)
    private String userName;//用户昵称
    @ExcelProperty(value = "提现地址" ,index = 3)
    private String toAddress;//提现地址
    @ExcelProperty(value = "提现数（RIT）" ,index = 4)
    private String withdrawAmount;//RIT提现数量
    @ExcelProperty(value = "实际到账数（RIT）" ,index = 5)
    private String accountAmount;//RIT实际到账数
    @ExcelProperty(value = "提现时间" ,index = 6)
    private String withdrawTime;//提现时间
    @ExcelProperty(value = "转出时间" ,index = 7)
    private String accountTime;//转出时间
    @ExcelProperty(value = "状态" ,index = 8)
    private String statusName;//状态含义
    @ExcelProperty(value = "审核人" ,index = 9)
    private String auditUserName;//审核人用户姓名

    public String getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(String withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(String withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public String getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(String accountTime) {
        this.accountTime = accountTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public static List<AccountWithdrawFlowRitExcel> MeaningOfConversion(List<AccountWithdrawFlowRitResponse> list){
        List<AccountWithdrawFlowRitExcel> accountWithdrawFlowRitExcels = Lists.newArrayList();
        for (AccountWithdrawFlowRitResponse accountWithdrawFlowRitResponse:list) {
            AccountWithdrawFlowRitExcel excel = new AccountWithdrawFlowRitExcel();
            excel.setWithdrawFlowId(accountWithdrawFlowRitResponse.getWithdrawFlowId());
            excel.setUserPhone(accountWithdrawFlowRitResponse.getUserPhone());
            excel.setUserName(accountWithdrawFlowRitResponse.getUserName());
            excel.setToAddress(accountWithdrawFlowRitResponse.getToAddress());
            excel.setWithdrawAmount(accountWithdrawFlowRitResponse.getWithdrawAmount());
            excel.setAccountAmount(accountWithdrawFlowRitResponse.getAccountAmount());
            excel.setStatusName(accountWithdrawFlowRitResponse.getStatusName());
            excel.setAuditUserName(accountWithdrawFlowRitResponse.getAuditUserName());
            //提现时间
            excel.setWithdrawTime(DateUtils.parseTimestamp(accountWithdrawFlowRitResponse.getWithdrawTime()));
            //转出时间
            if(StringUtils.isNotBlank(accountWithdrawFlowRitResponse.getAccountTime())){
                excel.setAccountTime(DateUtils.parseTimestamp(accountWithdrawFlowRitResponse.getAccountTime()));
            }
            accountWithdrawFlowRitExcels.add(excel);
        }
        return accountWithdrawFlowRitExcels;
    }
}
