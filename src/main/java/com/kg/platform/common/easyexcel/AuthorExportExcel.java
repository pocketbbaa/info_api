package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class AuthorExportExcel extends BaseRowModel {

    @ExcelProperty(value = "作者ID", index = 0)
    private String userId;

    @ExcelProperty(value = "作者昵称", index = 1)
    private String userName;

    @ExcelProperty(value = "作者角色", index = 2)
    private String role;

    @ExcelProperty(value = "手机号码", index = 3)
    private String phone;

    @ExcelProperty(value = "注册时间", index = 4)
    private String createTime;

    @ExcelProperty(value = "专栏审核时间", index = 5)
    private String columnTime;

    @ExcelProperty(value = "注册来源", index = 6)
    private String registFrom;

    @ExcelProperty(value = "粉丝总量", index = 7)
    private String fansTotal;

    @ExcelProperty(value = "是否推荐（是否热门作者）", index = 8)
    private String hotAuthor;

    @ExcelProperty(value = "师傅昵称", index = 9)
    private String masterUserName;

    @ExcelProperty(value = "师傅ID", index = 10)
    private String masterUserId;

    @ExcelProperty(value = "氪金可用余额总量", index = 11)
    private String kgBalance;

    @ExcelProperty(value = "RIT可用余额总量", index = 12)
    private String ritBalance;

    @ExcelProperty(value = "审核通过文章总量", index = 13)
    private String auditNum;

    @ExcelProperty(value = "审核未通过文章总量", index = 14)
    private String auditFailedNum;

    @ExcelProperty(value = "最后登陆时间", index = 15)
    private String lastLoginTime;

    @ExcelProperty(value = "最后一次发文时间（审核通过时间）", index = 16)
    private String lastPostTime;

    @ExcelProperty(value = "首次发文时间（审核通过时间）", index = 17)
    private String firstPostTime;

    @ExcelProperty(value = "粉丝增量", index = 18)
    private String fans;

    @ExcelProperty(value = "专栏等级（0：见习，1：一星，2：二星，3：三星，4：四星，5：五星）", index = 19)
    private String columnLevel;

    public String getFirstPostTime() {
        return firstPostTime;
    }

    public void setFirstPostTime(String firstPostTime) {
        this.firstPostTime = firstPostTime;
    }

    public String getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(String columnLevel) {
        this.columnLevel = columnLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getColumnTime() {
        return columnTime;
    }

    public void setColumnTime(String columnTime) {
        this.columnTime = columnTime;
    }

    public String getRegistFrom() {
        return registFrom;
    }

    public void setRegistFrom(String registFrom) {
        this.registFrom = registFrom;
    }

    public String getFansTotal() {
        return fansTotal;
    }

    public void setFansTotal(String fansTotal) {
        this.fansTotal = fansTotal;
    }

    public String getHotAuthor() {
        return hotAuthor;
    }

    public void setHotAuthor(String hotAuthor) {
        this.hotAuthor = hotAuthor;
    }

    public String getMasterUserName() {
        return masterUserName;
    }

    public void setMasterUserName(String masterUserName) {
        this.masterUserName = masterUserName;
    }

    public String getMasterUserId() {
        return masterUserId;
    }

    public void setMasterUserId(String masterUserId) {
        this.masterUserId = masterUserId;
    }

    public String getKgBalance() {
        return kgBalance;
    }

    public void setKgBalance(String kgBalance) {
        this.kgBalance = kgBalance;
    }

    public String getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(String ritBalance) {
        this.ritBalance = ritBalance;
    }

    public String getAuditNum() {
        return auditNum;
    }

    public void setAuditNum(String auditNum) {
        this.auditNum = auditNum;
    }

    public String getAuditFailedNum() {
        return auditFailedNum;
    }

    public void setAuditFailedNum(String auditFailedNum) {
        this.auditFailedNum = auditFailedNum;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastPostTime() {
        return lastPostTime;
    }

    public void setLastPostTime(String lastPostTime) {
        this.lastPostTime = lastPostTime;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }
}
