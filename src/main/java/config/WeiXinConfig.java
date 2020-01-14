package config;

import java.util.HashMap;
import java.util.Map;

public class WeiXinConfig {
    /**
     * 微信支付缓存地址
     */
    public static final String REDIS_URL = "pay:weChatPay:order:";
    /**
     * 微信树木支付缓存地址
     */
    public static final String REDIS_URL_PRODUCT = "pay:weChatPay:productOrder:";

    /**
     * 微信退款缓存地址
     */
    public static final String REDIS_URL_OUT_PAY = "pay:weChatOutPay:order:";
    /**
     *
     */
    public static String REDIS_URL_RECHARGE = "pay:weChatPay:recharge:";
    /**
     * 微信支付申请成功后的秘钥 API秘钥 (用于支付时 加密支付相关参数的值)
     * 微信支付平台 > API安全 > API秘钥 > 设置API秘钥
     * 未加密秘钥:1111111
     * 加密后秘钥:8b5163ca0d957c6c0a0032ce8c0ad200
     */
    public static String key = "8b5163ca0d957c6c0a0032ce8c0ad200";
    /**
     * 微信支付登录密码（秘钥）
     * 用于下载加密参数的 cer 证书
     */
    public static String pwd = "123123";
    /**
     * 支付网关地址
     */
    public static String gatewayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 查询订单地址
     */
    public static String orderQueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 申请退款地址
     */
    public static String refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 申请退款回调地址
     */
    public static String notifyRefundUrl = "api/web/weChat/weChatOutPayNotify";
    /**
     * 公众号appID
     */
    public static String appid = "123123123";

    /**
     * 微信 app_secret 用于微信用户授权 获取openId
     * 微信公众平台获取
     */
    public static String appSecret = "123123123";

    /**
     * 微信商户号id (微信支付平台-商户信息-账户信息-微信支付商户号)
     */
    public static String mch_id = "123123";
    /**
     * 子商户Id (app支付需要)
     */
    public static String sub_mch_id = "123123";

    /**
     * 签名类型
     */
    public static String sign_type_md5 = "MD5";
    /**
     * 签名类型
     */
    public static String sign_type_HMACSHA256_ = "HMAC-SHA256";

    /**
     * 微信回调接口（重要）
     */
    public static String notify_url = "回调接口地址";
    /**
     * 正式环境  访问域名地址
     */
    public static String web_url = "http://www.baidu.com";
    /**
     * 支付交易类型
     * //支付场景 APP 微信app支付 JSAPI 公众号支付  NATIVE 扫码支付  MWEB h5支付
     */
    public static String trade_type_app = "APP";

    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 6 * 1000;
    public static final int DEFAULT_READ_TIMEOUT_MS = 8 * 1000;

    /**
     * 执行成功过 返回状态码
     */
    public static String SSUCCESS = "SUCCESS";

    /**
     * 执行成功过 返回状态码
     */
    public static String FILL = "FILL";
    /**
     * 执行成功过 返回信息
     */
    public static String OK = "OK";

    /**
     * 暂填写固定值Sign=WXPay
     */
    public static String packageValue = "Sign=WXPay";

    /**
     * 安卓app应用id
     */
    public static String applyId = "123123";

    /**
     * IOS APP应用id
     */
    public static String iosApplyId = "123123";

    /**
     * 支付场景
     */

    public enum TRADE_TYPE {
        APP("APP", "微信app支付"),
        JSAPI("JSAPI", "公众号支付"),
        NATIVE("NATIVE", "扫码支付"),
        MWEB("MWEB", "h5支付");


        public final String key;
        public final String value;
        private static Map<String, String> map = new HashMap<>();

        TRADE_TYPE(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String getValue(String key) {
            if (null == key || "".equals(key)) {
                return null;
            }
            for (TRADE_TYPE status : TRADE_TYPE.values()) {
                if (status.key.equals(key)) {
                    return status.value;
                }
            }
            return null;
        }

        public static String getCode(String value) {
            if (null == value || "".equals(value)) {
                return null;
            }
            for (TRADE_TYPE status : TRADE_TYPE.values()) {
                if (status.value.equals(value)) {
                    return status.key;
                }
            }
            return null;
        }

        public static Map<String, String> getEnumMap() {
            if (map.size() == 0) {
                for (TRADE_TYPE status : TRADE_TYPE.values()) {
                    map.put(status.key, status.value);
                }
            }
            return map;
        }
    }
}
