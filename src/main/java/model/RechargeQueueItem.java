package model;

import lombok.Data;

@Data
public class RechargeQueueItem {

    /**
     * 充值单号
     */
    private String orderNo;

    /**
     * 外部单号
     */
    private String outNo;

    /**
     * 支付方式
     */
//    private AccountRecharge.PaymentType paymentType;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 外部订单状态 string
     */
    private String outStatus;
}
