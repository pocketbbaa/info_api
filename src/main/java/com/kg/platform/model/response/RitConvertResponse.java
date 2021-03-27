package com.kg.platform.model.response;

/**
 * kg兑换RIT返回对象
 */
public class RitConvertResponse {

    private Integer isComplete; //是否完成兑换 0：否，1：是
    private ConvertCompleteResponse convertComplete; //兑换完成返回
    private ConvertIndexResponse convertIndex;

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public ConvertCompleteResponse getConvertComplete() {
        return convertComplete;
    }

    public void setConvertComplete(ConvertCompleteResponse convertComplete) {
        this.convertComplete = convertComplete;
    }

    public ConvertIndexResponse getConvertIndex() {
        return convertIndex;
    }

    public void setConvertIndex(ConvertIndexResponse convertIndex) {
        this.convertIndex = convertIndex;
    }
}
