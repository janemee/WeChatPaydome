package util;

import config.WeiXinConfig;

import java.util.*;

public class WeiXinPayCommonUtil {
    public static String createSign(String characterEncoding, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WeiXinConfig.key);
        // 最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        String sign = WeiXinMD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;

    }


    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("sign".equalsIgnoreCase(k)) {

            } else if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("<" + "sign" + ">" + "<![CDATA[" + parameters.get("sign") + "]]></" + "sign" + ">");
        sb.append("</xml>");
        return sb.toString();
    }


    public static String genNonceStr() {
        Random random = new Random();
        return WeiXinMD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "");
    }

}
