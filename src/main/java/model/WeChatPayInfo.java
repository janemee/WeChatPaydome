package model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class WeChatPayInfo {
    /**
     * 请求来源地址 （与商户平台配置域名一致）
     */
    public static final String Referer = "ssycd.com";
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户userUuid
     */
    private String userUuid;

    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 支付金额
     */
    private String amount;
    /**
     * 用户真实ip地址
     */
    private String spbill_create_ip;

    /**
     * 1 普通商品支付 2 树木订单支付 3 充值 4
     */
    private int type;

    //----------------------------树木支付相关参数
    /**
     * 树木订单id
     */
    private Integer skuId;
    /**
     * 树木数量
     */
    private Integer num;
    /**
     * 树木订单编号
     */
    private String skuOrderNo;


    //---------------------------充值相关参数
    /**
     * 系统订单号 （系统随机生成）
     */
    private String rechargeOrderNo;

    /**
     * ios 专用参数 返回地址
     */
    private String payFlag;

    /**
     * 为空  安卓  1 苹果
     */
    private Integer appType;

    /**
     * 微信用户授码
     */
    private String code;

    /**
     * 微信用户openId (JSAPI 必传参数)
     */
    private String openId;
    /**
     *
     * @param userId
     * @param orderId
     * @param amount
     * @param spbill_create_ip
     */
    public WeChatPayInfo(Integer userId, Integer orderId, String amount, String spbill_create_ip) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.spbill_create_ip = spbill_create_ip;
    }

    public WeChatPayInfo() {
    }


    public enum TYPE {
        SHOP("普通订单支付", 1),
        PROD_SHOP("树木订单支付", 2),
        RECHARGE("账户充值", 3),
        WC_H5_RECHARGE("微信h5支付", 4);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        TYPE(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (TYPE status : TYPE.values()) {
                if (status.code == code) {
                    return status.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value || "".equals(value)) {
                return null;
            }
            for (TYPE status : TYPE.values()) {
                if (status.value.equals(value)) {
                    return status.code;
                }
            }
            return null;
        }

        public static Map<Integer, String> getEnumMap() {
            if (map.size() == 0) {
                for (TYPE status : TYPE.values()) {
                    map.put(status.code, status.value);
                }
            }
            return map;
        }
    }
}
