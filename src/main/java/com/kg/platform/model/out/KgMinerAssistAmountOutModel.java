package com.kg.platform.model.out;

/**
 * @author
 */
public class KgMinerAssistAmountOutModel {

    /**
     * 蓄能值
     */
    private Integer assistAmount;

    /**
     * 是否已经蓄能满 0 未满 1已满
     */
    private Integer isFull;

    public Integer getAssistAmount() {
        return assistAmount;
    }

    public void setAssistAmount(Integer assistAmount) {
        this.assistAmount = assistAmount;
    }

    public Integer getIsFull() {
        return isFull;
    }

    public void setIsFull(Integer isFull) {
        this.isFull = isFull;
    }

}