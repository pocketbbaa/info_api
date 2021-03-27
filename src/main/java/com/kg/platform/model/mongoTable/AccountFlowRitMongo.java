package com.kg.platform.model.mongoTable;

import java.math.BigDecimal;
import java.util.Date;


public class AccountFlowRitMongo {

    private  Long account_flow_id;

    private Long relation_flow_id;

    private Long user_id;

    private String user_phone;

    private String user_email;

    private String user_name;

    private BigDecimal amount=new BigDecimal(0.00000000);

    private BigDecimal freeze_amount=new BigDecimal(0.00000000);

    private Integer business_type_id;

    private String tx_id;

    private String tx_address;

    private BigDecimal account_amount=new BigDecimal(0.00000000);

    private BigDecimal poundage_amount=new BigDecimal(0.00000000);

    private Long account_date;

    private Long article_id;

    private Integer bonusTotalPerson;

    private Integer flow_status;

    private Long flow_date;

    private String flow_detail;

    private String remark;

    public Long getAccount_flow_id() {
        return account_flow_id;
    }

    public void setAccount_flow_id(Long account_flow_id) {
        this.account_flow_id = account_flow_id;
    }

    public Long getRelation_flow_id() {
        return relation_flow_id;
    }

    public void setRelation_flow_id(Long relation_flow_id) {
        this.relation_flow_id = relation_flow_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFreeze_amount() {
        return freeze_amount;
    }

    public void setFreeze_amount(BigDecimal freeze_amount) {
        this.freeze_amount = freeze_amount;
    }

    public Integer getBusiness_type_id() {
        return business_type_id;
    }

    public void setBusiness_type_id(Integer business_type_id) {
        this.business_type_id = business_type_id;
    }

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }

    public String getTx_address() {
        return tx_address;
    }

    public void setTx_address(String tx_address) {
        this.tx_address = tx_address;
    }

    public BigDecimal getAccount_amount() {
        return account_amount;
    }

    public void setAccount_amount(BigDecimal account_amount) {
        this.account_amount = account_amount;
    }

    public BigDecimal getPoundage_amount() {
        return poundage_amount;
    }

    public void setPoundage_amount(BigDecimal poundage_amount) {
        this.poundage_amount = poundage_amount;
    }

    public Long getAccount_date() {
        return account_date;
    }

    public void setAccount_date(Long account_date) {
        this.account_date = account_date;
    }

    public Long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Long article_id) {
        this.article_id = article_id;
    }

    public Integer getBonusTotalPerson() {
        return bonusTotalPerson;
    }

    public void setBonusTotalPerson(Integer bonusTotalPerson) {
        this.bonusTotalPerson = bonusTotalPerson;
    }

    public Integer getFlow_status() {
        return flow_status;
    }

    public void setFlow_status(Integer flow_status) {
        this.flow_status = flow_status;
    }

    public Long getFlow_date() {
        return flow_date;
    }

    public void setFlow_date(Long flow_date) {
        this.flow_date = flow_date;
    }

    public String getFlow_detail() {
        return flow_detail;
    }

    public void setFlow_detail(String flow_detail) {
        this.flow_detail = flow_detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}