package com.kg.platform.enumeration;

public enum MQTagsEnum {
    payFlowTag("payFlow"),

    /*
     *
     */
    frozenFlowTag("frozenFlow"),

    /*
     *
     */
    orderChangeTag("orderChange"),

    /*
     *
     */
    confirmMrchTag("confirmMerch"),

    /*
     *
     */
    confirmOrderTag("confirmOrder"),

    /*
     *
     */
    withdraw("withdraw"),

    /*
     *
     */
    createDockingOrder("createDockingOrder"),
    /*
     *
     */
    otaRefundMerch("refundConfirm"),

    refundApply("trade_refund_apply_msg"),
    /*
     *
     */
    otaConsumerMerch("consumed"),
    /** 退款成功 */
    refundSuccess("trade_refund_msg"),
    /** 退款日志 */
    refundLog("trade_refund_log"),
    /** openAPI 交易消息 */
    trade_to_openapi("trade_to_openapi");

    private MQTagsEnum(final String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
