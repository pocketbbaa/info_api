package com.kg.platform.model.mongoTable;

import java.math.BigDecimal;

/**
 * 进贡记录表
 */
public class SubTributeRecordMongo {

    //id
    private Long tribute_id;

    //进贡币种类型 1.tv  2.kg
    private Integer coin_type;

    //徒弟id
    private Long sub_id;

    //徒弟名字
    private String sub_name;

    //师傅id
    private Long master_id;

    //进贡数量
    private BigDecimal amount;

    //师傅名称
    private String master_name;

    //创建时间
    private Long create_date;

    public Long getTribute_id() {
        return tribute_id;
    }

    public void setTribute_id(Long tribute_id) {
        this.tribute_id = tribute_id;
    }

    public Integer getCoin_type() {
        return coin_type;
    }

    public void setCoin_type(Integer coin_type) {
        this.coin_type = coin_type;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public Long getMaster_id() {
        return master_id;
    }

    public void setMaster_id(Long master_id) {
        this.master_id = master_id;
    }

    public Long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Long create_date) {
        this.create_date = create_date;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSub_id() {
        return sub_id;
    }

    public void setSub_id(Long sub_id) {
        this.sub_id = sub_id;
    }
}