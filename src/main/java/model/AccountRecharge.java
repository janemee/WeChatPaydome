package model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值记录表(AccountRecharge)表实体类
 *
 * @author Donfy
 * @since 2018-11-15 18:01:08
 */
@Data
public class AccountRecharge  {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户类型 1普通用户,2代理商,3厂家
     */
    private UserType userType;

    /**
     * 充值方式
     */
    private PaymentType paymentType;

    /**
     * 充值类型名称
     */
    private String type;

    /**
     * 状态1待审核,2充值成功,3充值失败
     */
    private Status status;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 支行名称
     */
    private String branch;

    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 实际到账金额（手续费自承情况）
     */
    private BigDecimal actualAmount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 平台承担手续费金额
     */
    private BigDecimal feePlat;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 外部单号
     */
    private String outOrderNo;

    /**
     * 第三方返回状态
     */
    private String outStatus;

    /**
     * 第三方返回消息
     */
    private String outMsg;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核时间
     */
    private Date reviewTime;


    /**
     * 版本号
     */
    private Integer version;

    /**
     * 状态: 1待审核,2充值成功,3充值失败
     */
    @AllArgsConstructor
    public enum Status implements BaseEnum {
        /**
         * 待支付
         */

        T1(1, "待支付", HMTypeColro.T1),
        /**
         * 2充值成功
         */

        T2(2, "充值成功", HMTypeColro.T2),
        /**
         * 3充值失败
         */

        T3(3, "充值失败", HMTypeColro.T3),

        ;
        @Getter
        public final int code;
        @Getter
        public final String value;
        public final String lableColor;

        @Override
        @JsonValue
        public int getCode() {
            return this.code;
        }


    }

    public String getStatusFormatter() {
        if (null == this.status) {
            return null;
        }
        return this.status.value;
    }

    public String getStatusColor() {
        if (null == this.status) {
            return null;
        }
        return this.status.lableColor;
    }

    /**
     * 充值方式 1快捷支付, 2支付宝, 3微信
     */
    @AllArgsConstructor
    public enum PaymentType implements BaseEnum {
        /**
         * 1快捷支付 - 该方式要记录银行卡号
         */

        T1(1, "llpay", HMTypeColro.T1),
        /**
         * 2支付宝
         */

        T2(2, "alipay", HMTypeColro.T2),
        /**
         * 3微信
         */

        T3(3, "wechat_pay", HMTypeColro.T3),
        /**
         * 3微信
         */

        T4(4, "renGong_pay", HMTypeColro.T4),
        ;
        @Getter
        public final int code;
        @Getter
        public final String value;
        public final String lableColor;

        @Override
        @JsonValue
        public int getCode() {
            return this.code;
        }
    }

    public String getPaymentTypeFormatter() {
        if (null == this.paymentType) {
            return null;
        }
        return this.paymentType.value;
    }

    public String getPaymentTypeColor() {
        if (null == this.paymentType) {
            return null;
        }
        return this.paymentType.lableColor;
    }

    /**
     * 用户类型 1普通用户, 2代理商, 3厂家
     */
    @AllArgsConstructor
    public enum UserType implements BaseEnum {
        /**
         * 1普通用户
         */

        T1(1, "普通用户", HMTypeColro.T1),
        /**
         * 2代理商
         */

        T2(2, "代理商", HMTypeColro.T2),
        /**
         * 3厂家
         */

        T3(3, "厂家", HMTypeColro.T3),

        ;
        @Getter
        public final int code;
        @Getter
        public final String value;
        public final String labelColor;

        @Override
        @JsonValue
        public int getCode() {
            return this.code;
        }
    }

    public String getUserTypeFormatter() {
        if (null == this.userType) {
            return null;
        }
        return this.userType.value;
    }

    public String getUserTypeColor() {
        if (null == this.userType) {
            return null;
        }
        return this.userType.labelColor;
    }
}