package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * @author 
 */
public class KgMobileImgRequest implements Serializable {
    private Integer imgId;

    /**
     * 每个导航正常状态图片
     */
    private String normalImg;

    /**
     * 每个导航点击后的图片
     */
    private String choiseImg;

    /**
     * 0：正常 1：删除
     */
    private Integer isDel;

    /**
     * 排序 可依次表示导航按钮位置
     */
    private Integer orderby;

    /**
     * 预留字段
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getNormalImg() {
        return normalImg;
    }

    public void setNormalImg(String normalImg) {
        this.normalImg = normalImg;
    }

    public String getChoiseImg() {
        return choiseImg;
    }

    public void setChoiseImg(String choiseImg) {
        this.choiseImg = choiseImg;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getOrderby() {
        return orderby;
    }

    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        KgMobileImgRequest other = (KgMobileImgRequest) that;
        return (this.getImgId() == null ? other.getImgId() == null : this.getImgId().equals(other.getImgId()))
            && (this.getNormalImg() == null ? other.getNormalImg() == null : this.getNormalImg().equals(other.getNormalImg()))
            && (this.getChoiseImg() == null ? other.getChoiseImg() == null : this.getChoiseImg().equals(other.getChoiseImg()))
            && (this.getIsDel() == null ? other.getIsDel() == null : this.getIsDel().equals(other.getIsDel()))
            && (this.getOrderby() == null ? other.getOrderby() == null : this.getOrderby().equals(other.getOrderby()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getImgId() == null) ? 0 : getImgId().hashCode());
        result = prime * result + ((getNormalImg() == null) ? 0 : getNormalImg().hashCode());
        result = prime * result + ((getChoiseImg() == null) ? 0 : getChoiseImg().hashCode());
        result = prime * result + ((getIsDel() == null) ? 0 : getIsDel().hashCode());
        result = prime * result + ((getOrderby() == null) ? 0 : getOrderby().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", imgId=").append(imgId);
        sb.append(", normalImg=").append(normalImg);
        sb.append(", choiseImg=").append(choiseImg);
        sb.append(", isDel=").append(isDel);
        sb.append(", orderby=").append(orderby);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}