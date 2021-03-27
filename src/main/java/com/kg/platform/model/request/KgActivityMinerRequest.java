package com.kg.platform.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
public class KgActivityMinerRequest implements Serializable {
    /**
     * 矿机ID
     */
    private Long minerId;

    /**
     * 抢单ID
     */
    private Long robId;

    /**
     * 矿机名
     */
    private String minerName;

    /**
     * 助力码
     */
    private String assistCode;

    /**
     * 矿机价格
     */
    private BigDecimal minerPrice;

    /**
     * 需助力人数（不包括自己）
     */
    private Integer minerAssistFrequency;

    /**
     * 参与人数
     */
    private Integer minerJoinFrequency;

    /**
     * 剩余数量
     */
    private Integer minerNumber;

    /**
     * 活动开始时间
     */
    private Date minerStartDate;

    /**
     * 活动结束时间
     */
    private Date minerEndDate;

    /**
     * 矿机简介
     */
    private String minerDesc;

    /**
     * 矿机购买链接
     */
    private String minerLink;

    /**
     * 矿机主图地址
     */
    private String minerPhoto;

    private Integer currentPage;

    private Integer pageSize;

    private static final long serialVersionUID = 1L;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getMinerId() {
        return minerId;
    }

    public void setMinerId(Long minerId) {
        this.minerId = minerId;
    }

    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName;
    }

    public BigDecimal getMinerPrice() {
        return minerPrice;
    }

    public void setMinerPrice(BigDecimal minerPrice) {
        this.minerPrice = minerPrice;
    }

    public Integer getMinerAssistFrequency() {
        return minerAssistFrequency;
    }

    public void setMinerAssistFrequency(Integer minerAssistFrequency) {
        this.minerAssistFrequency = minerAssistFrequency;
    }

    public Integer getMinerJoinFrequency() {
        return minerJoinFrequency;
    }

    public void setMinerJoinFrequency(Integer minerJoinFrequency) {
        this.minerJoinFrequency = minerJoinFrequency;
    }

    public Integer getMinerNumber() {
        return minerNumber;
    }

    public void setMinerNumber(Integer minerNumber) {
        this.minerNumber = minerNumber;
    }

    public Date getMinerStartDate() {
        return minerStartDate;
    }

    public void setMinerStartDate(Date minerStartDate) {
        this.minerStartDate = minerStartDate;
    }

    public Date getMinerEndDate() {
        return minerEndDate;
    }

    public void setMinerEndDate(Date minerEndDate) {
        this.minerEndDate = minerEndDate;
    }

    public String getMinerDesc() {
        return minerDesc;
    }

    public void setMinerDesc(String minerDesc) {
        this.minerDesc = minerDesc;
    }

    public String getMinerLink() {
        return minerLink;
    }

    public void setMinerLink(String minerLink) {
        this.minerLink = minerLink;
    }

    public String getMinerPhoto() {
        return minerPhoto;
    }

    public void setMinerPhoto(String minerPhoto) {
        this.minerPhoto = minerPhoto;
    }

    public String getAssistCode() {
        return assistCode;
    }

    public void setAssistCode(String assistCode) {
        this.assistCode = assistCode;
    }

    public Long getRobId() {
        return robId;
    }

    public void setRobId(Long robId) {
        this.robId = robId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KgActivityMinerRequest other = (KgActivityMinerRequest) that;
        return (this.getMinerId() == null ? other.getMinerId() == null : this.getMinerId().equals(other.getMinerId()))
                && (this.getMinerName() == null ? other.getMinerName() == null
                        : this.getMinerName().equals(other.getMinerName()))
                && (this.getMinerPrice() == null ? other.getMinerPrice() == null
                        : this.getMinerPrice().equals(other.getMinerPrice()))
                && (this.getMinerAssistFrequency() == null ? other.getMinerAssistFrequency() == null
                        : this.getMinerAssistFrequency().equals(other.getMinerAssistFrequency()))
                && (this.getMinerJoinFrequency() == null ? other.getMinerJoinFrequency() == null
                        : this.getMinerJoinFrequency().equals(other.getMinerJoinFrequency()))
                && (this.getMinerNumber() == null ? other.getMinerNumber() == null
                        : this.getMinerNumber().equals(other.getMinerNumber()))
                && (this.getMinerStartDate() == null ? other.getMinerStartDate() == null
                        : this.getMinerStartDate().equals(other.getMinerStartDate()))
                && (this.getMinerEndDate() == null ? other.getMinerEndDate() == null
                        : this.getMinerEndDate().equals(other.getMinerEndDate()))
                && (this.getMinerDesc() == null ? other.getMinerDesc() == null
                        : this.getMinerDesc().equals(other.getMinerDesc()))
                && (this.getMinerLink() == null ? other.getMinerLink() == null
                        : this.getMinerLink().equals(other.getMinerLink()))
                && (this.getMinerPhoto() == null ? other.getMinerPhoto() == null
                        : this.getMinerPhoto().equals(other.getMinerPhoto()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMinerId() == null) ? 0 : getMinerId().hashCode());
        result = prime * result + ((getMinerName() == null) ? 0 : getMinerName().hashCode());
        result = prime * result + ((getMinerPrice() == null) ? 0 : getMinerPrice().hashCode());
        result = prime * result + ((getMinerAssistFrequency() == null) ? 0 : getMinerAssistFrequency().hashCode());
        result = prime * result + ((getMinerJoinFrequency() == null) ? 0 : getMinerJoinFrequency().hashCode());
        result = prime * result + ((getMinerNumber() == null) ? 0 : getMinerNumber().hashCode());
        result = prime * result + ((getMinerStartDate() == null) ? 0 : getMinerStartDate().hashCode());
        result = prime * result + ((getMinerEndDate() == null) ? 0 : getMinerEndDate().hashCode());
        result = prime * result + ((getMinerDesc() == null) ? 0 : getMinerDesc().hashCode());
        result = prime * result + ((getMinerLink() == null) ? 0 : getMinerLink().hashCode());
        result = prime * result + ((getMinerPhoto() == null) ? 0 : getMinerPhoto().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", minerId=").append(minerId);
        sb.append(", minerName=").append(minerName);
        sb.append(", minerPrice=").append(minerPrice);
        sb.append(", minerAssistFrequency=").append(minerAssistFrequency);
        sb.append(", minerJoinFrequency=").append(minerJoinFrequency);
        sb.append(", minerNumber=").append(minerNumber);
        sb.append(", minerStartDate=").append(minerStartDate);
        sb.append(", minerEndDate=").append(minerEndDate);
        sb.append(", minerDesc=").append(minerDesc);
        sb.append(", minerLink=").append(minerLink);
        sb.append(", minerPhoto=").append(minerPhoto);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}