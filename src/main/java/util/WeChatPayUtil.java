package util;

import config.WeiXinConfig;
import exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WeChatPayUtil {
    private static final Logger log = LoggerFactory.getLogger(WeChatPayUtil.class);

    /**
     * h5 统一下单接口
     *
     * @param orderNumber
     * @param clientIP
     * @param totalFee
     * @return
     * @throws Exception
     */
    public static Map<String, String> weixinPay(String orderNumber, String clientIP, String totalFee, String body, String apiServiceUrl) throws Exception {
        SortedMap<String, Object> parameters = new TreeMap<>();
        parameters.put("appid", WeiXinConfig.appid);
        parameters.put("attach", body);
        parameters.put("body", body);
        parameters.put("mch_id", WeiXinConfig.mch_id);
        parameters.put("notify_url", apiServiceUrl + WeiXinConfig.notify_url);
        parameters.put("sign_type", WeiXinConfig.sign_type_md5);
        //订单号
        parameters.put("out_trade_no", orderNumber);
        parameters.put("nonce_str", WeiXinPayCommonUtil.genNonceStr());
        //用户IP
        parameters.put("spbill_create_ip", clientIP);
        //金额
        parameters.put("total_fee", totalFee);
        parameters.put("trade_type", WeiXinConfig.TRADE_TYPE.MWEB.key);
        parameters.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + WeiXinConfig.web_url + "\",\"wap_name\": \"" + body + "\"}}");
        //生成签名
        parameters.put("sign", WeiXinPayCommonUtil.createSign("utf-8", parameters));
        //生成xml请求
        String reXml = WeiXinPayCommonUtil.getRequestXml(parameters);
        log.info(reXml);
        //请求xml
        String xml = HttpUtil.httpRequest(WeiXinConfig.gatewayUrl, reXml, WeiXinConfig.DEFAULT_CONNECT_TIMEOUT_MS, WeiXinConfig.DEFAULT_READ_TIMEOUT_MS);
        //解析xml
        Map<String, String> responseMap = WXPayUtil.xmlToMap(xml);
        if (!WeiXinConfig.SSUCCESS.equals(responseMap.get("return_code"))) {
            log.info(responseMap.toString());
            return responseMap;
        }
        //预支付交易会话标识 用于掉起app支付接口的参数
        responseMap.get("prepay_id");
        log.info(responseMap.toString());
        return responseMap;
    }


    /**
     * jsApi 统一下单接口
     *
     * @param orderNumber
     * @param clientIP
     * @param totalFee
     * @return
     * @throws Exception
     */
    public static Map<String, String> weChatPay(String orderNumber, String clientIP, String totalFee, String body, String apiServiceUrl, String openid) throws Exception {
        SortedMap<String, Object> parameters = new TreeMap<>();
        parameters.put("appid", WeiXinConfig.appid);
        parameters.put("mch_id", WeiXinConfig.mch_id);
        parameters.put("nonce_str", WeiXinPayCommonUtil.genNonceStr());
        parameters.put("sign_type", WeiXinConfig.sign_type_md5);
        parameters.put("body", body);
        //订单号
        parameters.put("out_trade_no", orderNumber);
        //金额
        parameters.put("total_fee", totalFee);
        //用户IP
        parameters.put("spbill_create_ip", clientIP);
        parameters.put("notify_url", apiServiceUrl + WeiXinConfig.notify_url);
        //支付类型
        parameters.put("trade_type", WeiXinConfig.TRADE_TYPE.JSAPI.key);
        //上传此参数no_credit--可限制用户不能使用信用卡支付
        parameters.put("limit_pay", "no_credit");
        //微信用户授权标识
        parameters.put("openid", openid);
        //生成签名
        parameters.put("sign", WeiXinPayCommonUtil.createSign("utf-8", parameters));
        //输出签名前的原数据：
        log.info("未加密数据：" + parameters);
        //生成xml请求
        String reXml = WeiXinPayCommonUtil.getRequestXml(parameters);
        log.info("加密后生成的xml信息： " + reXml);
        //请求xml
        String xml = HttpUtil.httpRequest(WeiXinConfig.gatewayUrl, reXml, WeiXinConfig.DEFAULT_CONNECT_TIMEOUT_MS, WeiXinConfig.DEFAULT_READ_TIMEOUT_MS);
        //解析xml
        Map<String, String> responseData = WXPayUtil.xmlToMap(xml);
        log.info("jsApi 统一下单响应参数：" + responseData);
        if (!WeiXinConfig.SSUCCESS.equals(responseData.get("return_code"))) {
            throw new BussinessException(responseData.get("return_msg"));
        }
        if (!WeiXinConfig.SSUCCESS.equals(responseData.get("result_code"))) {
            throw new BussinessException(responseData.get("err_code_des"));
        }
        //生成前端请求参数  需要重新加密
        parameters = new TreeMap<>();
        //公众号APPID
        parameters.put("appId", WeiXinConfig.appid);
        //时间戳
        parameters.put("timeStamp", DateUtils.getTime(new Date()));
        //随机字符串，不长于32位。 统一下单中的随机字符串
        parameters.put("nonceStr", WeiXinPayCommonUtil.genNonceStr());
        //统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
        parameters.put("package", "prepay_id=" + responseData.get("prepay_id"));
        //签名方式 默认MD5
        parameters.put("signType", WeiXinConfig.sign_type_md5);
        //生成签名
        parameters.put("sign", WeiXinPayCommonUtil.createSign("utf-8", parameters));
        //生成前端请求参数 整理
        Map<String, String> stringStringMap = new HashMap<>();
        for (String s : parameters.keySet()) {
            stringStringMap.put(s, parameters.get(s).toString());
        }
        log.info("校验预支付的sign是否有效：" + isPayResultNotifySignatureValid(stringStringMap));
        log.info("app 预支付请求参数：" + parameters);
        return stringStringMap;
    }

    /**
     * app 统一下单接口
     *
     * @param orderNumber
     * @param clientIP
     * @param totalFee
     * @return
     * @throws Exception
     */
    public static SortedMap<String, Object> weChatAppPay(String orderNumber, String clientIP, String totalFee, String body, String apiServiceUrl, String appId) throws Exception {

        SortedMap<String, Object> parameters = new TreeMap<>();
        //app应用id
        parameters.put("appid", appId);
        //商户号
        parameters.put("mch_id", WeiXinConfig.mch_id);
        //标题
        parameters.put("body", body);
        //加密类型
        parameters.put("sign_type", WeiXinConfig.sign_type_md5);
        //订单号
        parameters.put("out_trade_no", orderNumber);
        //随机字符串
        parameters.put("nonce_str", WeiXinPayCommonUtil.genNonceStr());
        //用户IP
        parameters.put("spbill_create_ip", clientIP);
        //金额
        parameters.put("total_fee", totalFee);
        //交易方式
        parameters.put("trade_type", WeiXinConfig.trade_type_app);
        //回调接口地址
        parameters.put("notify_url", apiServiceUrl + WeiXinConfig.notify_url);
        //生成签名
        parameters.put("sign", WeiXinPayCommonUtil.createSign("utf-8", parameters));
        //生成xml请求
        String reXml = WeiXinPayCommonUtil.getRequestXml(parameters);
        log.info("app统一下单 生成支付参数xml:" + reXml);
        //请求xml
        String xml = HttpUtil.httpRequest(WeiXinConfig.gatewayUrl, reXml, WeiXinConfig.DEFAULT_CONNECT_TIMEOUT_MS, WeiXinConfig.DEFAULT_READ_TIMEOUT_MS);
        //解析xml
        Map<String, String> responseMap = WXPayUtil.xmlToMap(xml);
        log.info("responseXml：:" + xml);
        log.info("responseMap：:" + responseMap);
        if (!WeiXinConfig.SSUCCESS.equals(responseMap.get("result_code"))) {
            System.out.println(responseMap.toString());
            throw new BussinessException("发起支付失败");
        }
        //预支付交易会话标识 用于掉起app支付接口的参数
        String prepay_id = responseMap.get("prepay_id");
        //获取统一下单中的随机字符串 用来预支付接口使用
        String nonce_str = responseMap.get("nonce_str");
        //统一下单中的随机字符串
        log.info("app统一下单接口执行返回结果：" + responseMap.toString());
        //请求预支付接口 生成拉取app信息
        return weChatAppPay(prepay_id, appId, nonce_str);
    }


    /**
     * 微信app支付 生成预支付参数
     * 参数名要与官方文档一致 否则会提示签名错误
     *
     * @param prepayid 预支付会话id
     * @return
     */
    public static SortedMap<String, Object> weChatAppPay(String prepayid, String appId, String nonce_str) {
        try {
            SortedMap<String, Object> parameters = new TreeMap<>();
            parameters.put("appid", appId);
            //微信支付分配的商户号
            parameters.put("partnerid", WeiXinConfig.sub_mch_id);
            //微信返回的支付交易会话ID  依赖于统一下单接口中的响应参数
            parameters.put("prepayid", prepayid);
            //暂填写固定值Sign=WXPay
            parameters.put("package", WeiXinConfig.packageValue);
            //随机字符串，不长于32位。 统一下单中的随机字符串
            parameters.put("noncestr", WeiXinPayCommonUtil.genNonceStr());
            //时间戳
            parameters.put("timestamp", DateUtils.getTime(new Date()));
            //生成签名
            parameters.put("sign", WeiXinPayCommonUtil.createSign("utf-8", parameters));
            Map<String, String> stringStringMap = new HashMap<>();
            for (String s : parameters.keySet()) {
                stringStringMap.put(s, parameters.get(s).toString());
            }
            log.info("校验预支付的sign是否有效：" + isPayResultNotifySignatureValid(stringStringMap));
            log.info("app 预支付请求参数：" + parameters);
            return parameters;
        } catch (Exception e) {
            throw new BussinessException(e.getMessage());
        }
    }

    /**
     * 判断支付结果通知中的sign是否有效
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public static boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        return WXPayUtil.isSignatureValid(reqData, WeiXinConfig.key, WeiXinConfig.sign_type_md5);
    }


}
