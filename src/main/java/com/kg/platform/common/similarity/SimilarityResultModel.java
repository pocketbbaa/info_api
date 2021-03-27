package com.kg.platform.common.similarity;

import java.math.BigInteger;

public class SimilarityResultModel {

    private BigInteger hashCode;
    private String percent;

    public SimilarityResultModel(BigInteger hashCode, String percent) {
        this.hashCode = hashCode;
        this.percent = percent;
    }

    public BigInteger getHashCode() {
        return hashCode;
    }

    public void setHashCode(BigInteger hashCode) {
        this.hashCode = hashCode;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
