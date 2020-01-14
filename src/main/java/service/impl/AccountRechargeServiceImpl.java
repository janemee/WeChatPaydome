package service.impl;

import config.WeiXinConfig;
import exception.BussinessException;
import model.AccountRecharge;
import model.ResultEntity;
import model.User;
import model.WeChatPayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.AccountRechargeService;
import util.OrderNoUtils;
import util.StringUtils;
import util.WeChatPayUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;


/**
 * 充值记录表(AccountRecharge)表service
 *
 * @author Donfy
 * @since 2018-11-15 18:01:08
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Throwable.class)
public class AccountRechargeServiceImpl implements AccountRechargeService {
    private static final Logger log = LoggerFactory.getLogger(AccountRechargeServiceImpl.class);


    @Override
    public Map<String, String> weChatRecharge(WeChatPayInfo weChatPayInfo, String webUrl) throws Exception {
        Map<String, String> map;
        //需要支付金额 单位：分
        String payAmountStr = new BigDecimal(weChatPayInfo.getAmount()).multiply(new BigDecimal("100")).setScale(0).toString();
        //订单名称  注意订单名称内容不能太长
        String subject = StringUtils.getStr("账户充值：" + weChatPayInfo.getAmount());
        //生成充值订单号
        String orderNo = "CZ" + OrderNoUtils.getSerialNumber();
        System.out.println(orderNo);
        weChatPayInfo.setRechargeOrderNo(orderNo);
        //微信H5支付 考虑手机端浏览器支付不支持JSAPI支付  根据前端条件判断调用哪种支付
        if (WeChatPayInfo.TYPE.WC_H5_RECHARGE.code == weChatPayInfo.getType()) {
            map = WeChatPayUtil.weixinPay(orderNo, weChatPayInfo.getSpbill_create_ip(), payAmountStr, subject, webUrl);
            //SUCCESS/FAIL
            //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            if (!WeiXinConfig.SSUCCESS.equals(map.get("return_code"))) {
                throw new BussinessException(map.get("return_msg"));
            }
            //当return_code为FAIL时返回信息为错误原因 ，例如 /签名失败 /参数格式校验错误
            if (!WeiXinConfig.OK.equals(map.get("return_msg"))) {
                throw new BussinessException(map.get("return_msg"));
            }
            //支付成功后 跳转系统的页面地址
            String redirectUrl = "&redirect_url= 前端页面地址";
            //微信支付路径 拉取微信支付控件
            String weChatPayUrl = map.get("mweb_url") + redirectUrl;
            //生成提交表单
            String fromString = StringUtils.insertIntoFormStr(weChatPayUrl);
            //重置map
            map = new HashMap<>(1);
            //设置前台提交表单
            map.put("from", fromString);
        } else {
            //请求统一下单接口 jsApi
            map = WeChatPayUtil.weChatPay(orderNo, weChatPayInfo.getSpbill_create_ip(), payAmountStr, subject, webUrl, weChatPayInfo.getOpenId());
        }
        //todo 添加充值记录

        //todo 加入缓存队列

        //todo 返回数据表单给前端
        return map;
    }

    @Override
    public ResultEntity weChatNotifyRecharge(Map<String, String> dataMap, WeChatPayInfo weChatPayInfo) {
        //根据返回的订单号 查询本系统的支付订单
        AccountRecharge accountRecharge = new AccountRecharge();
        if (accountRecharge == null) {
            throw new BussinessException("充值信息不存在");
        }
        if (!AccountRecharge.Status.T1.equals(accountRecharge.getStatus())) {
            throw new BussinessException("充值已完成，请勿重复操作");
        }

        if (WeiXinConfig.SSUCCESS.equals(dataMap.get("return_code"))) {
            //充值状态为成功
            accountRecharge.setStatus(AccountRecharge.Status.T2);
            //实际到账金额 单位 ：分
            BigDecimal payAmount = new BigDecimal(dataMap.get("total_fee")).divide(new BigDecimal("100"));
            //手续费
            BigDecimal tradeFee = BigDecimal.ZERO;
            //获取系统充值手续费配置信息
            BigDecimal rFee = new BigDecimal("系统设置的手续费");
            //本地手续费未配置或者不配置时 充值金额 = 用户原充值金额
            if (rFee != null && BigDecimal.ZERO.compareTo(rFee) < 0) {
                //本平台收取手续费
                BigDecimal fee = payAmount.multiply(rFee);
                //扣除手续费后的到账金额
                payAmount = payAmount.subtract(fee);
                //手续费由平台承担 设置为手续费 0
                tradeFee = fee;
            }
            //todo 记录资金

            //todo 更新资金

            accountRecharge.setFee(tradeFee);
            accountRecharge.setActualAmount(payAmount);
            accountRecharge.setOutOrderNo(dataMap.get("transaction_id"));
            accountRecharge.setRemark("充值成功，充值金额:" + payAmount);
        } else {
            accountRecharge.setStatus(AccountRecharge.Status.T3);
            accountRecharge.setRemark(dataMap.get("return_code") + " : " + dataMap.get("return_msg"));
        }
        accountRecharge.setOutStatus(dataMap.get("return_code"));
        accountRecharge.setOutMsg(dataMap.get("return_msg"));
        accountRecharge.setReviewTime(new Timestamp(System.currentTimeMillis()));
        //更新充值信息
        if (update(accountRecharge) == 0) {
            throw new BussinessException("更新充值信息错误");
        }
        return ResultEntity.success();
    }

    /**
     * 更新充值订单信息
     *
     * @param accountRecharge
     * @return
     */
    private int update(AccountRecharge accountRecharge) {
        //默认模拟成功  根据自己业务所定
        return 1;
    }

    @Override
    public SortedMap<String, Object> weChatRechargeApp(WeChatPayInfo weChatPayInfo, String webUrl) throws Exception {
        SortedMap<String, Object> map = new TreeMap<>();
        //需要支付金额 单位：分
        String payAmountStr = new BigDecimal(weChatPayInfo.getAmount()).multiply(new BigDecimal("100")).setScale(0).toString();
        //订单名称
        String subject = StringUtils.getStr("账户充值：" + weChatPayInfo.getAmount());
        //生成充值订单号
        String orderNo = "CZ" + OrderNoUtils.getSerialNumber();
        System.out.println(orderNo);
        weChatPayInfo.setRechargeOrderNo(orderNo);
        //微信开放平台审核通过的应用APPID Android
        String APPID = WeiXinConfig.applyId;
        //app应用id
        if (weChatPayInfo.getAppType() != null) {
            //微信开放平台审核通过的应用APPID ios
            APPID = WeiXinConfig.iosApplyId;
        }
        map = WeChatPayUtil.weChatAppPay(orderNo, weChatPayInfo.getSpbill_create_ip(), payAmountStr, subject, webUrl, APPID);

        //todo 添加充值记录

        //todo 加入队列

        //返回数据表单给前端
        return map;
    }


}