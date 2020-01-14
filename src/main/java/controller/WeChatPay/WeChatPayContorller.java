package controller.WeChatPay;

import com.alibaba.fastjson.JSON;
import com.google.common.io.CharStreams;
import config.WeiXinConfig;
import exception.BussinessException;
import io.swagger.annotations.ApiOperation;
import model.ResultEntity;
import model.User;
import model.WeChatPayInfo;
import model.WeChatUserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.AccountRechargeService;
import util.RequestWrapper;
import util.WXPayUtil;
import util.WeChatJsDoLoginUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WeChatPayContorller
 * @Description TODO
 * @Author janeVV
 * @Date : 2020/1/10 11:00
 **/
@Controller
@RequestMapping("/api/web/weChat")
public class WeChatPayContorller extends ResultEntity {
    private static final Logger log = LoggerFactory.getLogger(WeChatPayContorller.class);

    @Autowired
    private AccountRechargeService accountRechargeService;


    @ApiOperation(value = "微信JsApi支付-账户充值", notes = "统一下单纯接口")
    @RequestMapping("/recharge")
    @ResponseBody
    public ResultEntity recharge(WeChatPayInfo weChatPayInfo) {
        HashMap<String, Object> data = new HashMap<>();
        Map<String, String> map;
        String webUrl = "服务器访问地址 例如：www.baidu.com";
        //平台用户信息
        User user = new User();
        if (user == null) {
            return fail("用户不存在");
        }
        try {
            if (weChatPayInfo.getType() == WeChatPayInfo.TYPE.RECHARGE.code) {
                //未微信授权 去获取用户微信openId (jsApi支付情况下)
                if (StringUtils.isBlank(user.getWechatOpenId())) {
                    if (StringUtils.isBlank(weChatPayInfo.getCode())) {
                        throw new BussinessException("请先微信授权认证");
                    }
                    //获取微信openId
                    WeChatUserInfo weChatUserInfo = WeChatJsDoLoginUtils.getOpenId(weChatPayInfo.getCode());
                    //授权失败
                    if (weChatUserInfo.getState() != 0) {
                        throw new BussinessException(weChatUserInfo.getMessage());
                    }
                    String openId = weChatUserInfo.getOpenId();
                    if (StringUtils.isBlank(openId)) {
                        throw new BussinessException("微信授权失败");
                    }
                    //设置用户的微信授权标识（微信平台唯一）
                    user.setWechatOpenId(openId);
                    //设置openId到支付信息中
                    weChatPayInfo.setOpenId(user.getWechatOpenId());
                } else {
                    //第一次授权之后存下来的openId
                    weChatPayInfo.setOpenId(user.getWechatOpenId());
                }
                //请求统一下单接口
                Map<String, String> dataMap = accountRechargeService.weChatRecharge(weChatPayInfo, webUrl);
                //返回前端请求拉取微信支付控件
                data.putAll(dataMap);
            } else if (weChatPayInfo.getType() == WeChatPayInfo.TYPE.WC_H5_RECHARGE.code) {
                //请求统一下单接口
                Map<String, String> dataMap = accountRechargeService.weChatRecharge(weChatPayInfo, webUrl);
                //返回前端请求拉取微信支付控件
                data.putAll(dataMap);
            }
        } catch (Exception e) {
            return fail("支付失败，失败原因：" + e.getMessage());
        }
        return ResultEntity.success(data, "微信支付授权成功");
    }

    @ApiOperation(value = "支付回调", notes = "支付回调")
    @RequestMapping("/weChatPayNotify")
    public void weChatPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, String> data = new HashMap<>();
        response.setCharacterEncoding("UTF-8");
        System.out.println("====================进入支付异步通知数据接收处理====================");
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String parameterStr = CharStreams.toString(new InputStreamReader(requestWrapper.getInputStream(), "utf-8"));
        // 解析xml  转换为Map格式
        Map<String, String> notifyDate = WXPayUtil.xmlToMap(parameterStr);
        //平台订单号 用来查询缓存信息
        String orderNo = notifyDate.get("out_trade_no");
        //根据订单号查询本地缓存的支付信息
        String string = "$%^&*(#$%^&*()_";
        //转换成对应的对象
        WeChatPayInfo weChatPayInfo = JSON.parseObject(string, WeChatPayInfo.class);
        //三方返回的支付金额 单位：分
        String weChatPay_amonut = notifyDate.get("total_fee");
        //系统金额 元 转为 分
        String amount = new BigDecimal(weChatPayInfo.getAmount()).multiply(new BigDecimal("100")).intValue() + "";

        if (!amount.equals(weChatPay_amonut)) {
            log.info("系统缓存请求信息 weChatPayInfo:" + notifyDate);
            log.info("回调返回参数 notifyDate:" + notifyDate);
            log.error("支付金额与订单金额不符");
            data.put("return_code", WeiXinConfig.FILL);
            data.put("return_msg", WeiXinConfig.OK);

        }
        //执行回调逻辑
        ResultEntity resultEntity = accountRechargeService.weChatNotifyRecharge(notifyDate, null);

        //返回响应微信平台的执行信息
        String dataMsg = WXPayUtil.mapToXml(data);
        //返回给微信平台执行信息 写入响应流中 不然微信平台会重复执行回调
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(dataMsg.getBytes());
        out.flush();
        out.close();
    }
}
