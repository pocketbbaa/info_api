package com.kg.platform.model.out;

import java.io.Serializable;

/**
 * @author 
 */
public class KgArticleExtendOutModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 是否展示到首页推荐列表 0：否 1：是
     */
    private Integer ifIntoIndex;

    private Integer titleModify;

    private String blockchainUrl;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getIfIntoIndex() {
        return ifIntoIndex;
    }

    public void setIfIntoIndex(Integer ifIntoIndex) {
        this.ifIntoIndex = ifIntoIndex;
    }

    public Integer getTitleModify() {
        return titleModify;
    }

    public void setTitleModify(Integer titleModify) {
        this.titleModify = titleModify;
    }

    public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
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
        KgArticleExtendOutModel other = (KgArticleExtendOutModel) that;
        return (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()))
            && (this.getIfIntoIndex() == null ? other.getIfIntoIndex() == null : this.getIfIntoIndex().equals(other.getIfIntoIndex()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        result = prime * result + ((getIfIntoIndex() == null) ? 0 : getIfIntoIndex().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", articleId=").append(articleId);
        sb.append(", ifIntoIndex=").append(ifIntoIndex);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}