package service;

import model.ResultEntity;
import model.WeChatPayInfo;

import java.util.Map;
import java.util.SortedMap;

/**
 * 充值记录表(AccountRecharge)表service
 *
 * @author Donfy
 * @since 2018-11-15 18:01:08
 */
public interface AccountRechargeService {

    /**
     * 微信统一下单 生成提交html表单
     */
    Map<String, String> weChatRecharge(WeChatPayInfo weChatPayInfo, String webUrl) throws Exception;

    /**
     * 微信支付 充值回调处理
     *
     * @return
     */
    ResultEntity weChatNotifyRecharge(Map<String, String> dataMap, WeChatPayInfo weChatPayInfo);

    /**
     * APP支付 微信统一下单 生成提交html表单
     */
    SortedMap<String, Object> weChatRechargeApp(WeChatPayInfo weChatPayInfo, String webUrl) throws Exception;
}