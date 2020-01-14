package util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import config.WeiXinConfig;
import model.WeChatUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName WeChatJsDoLoginUtils
 * @Description 微信jsApi支付获取用户openId 工具类
 * @Author jzm
 * @Date : 2020/1/7 13:38
 **/
public class WeChatJsDoLoginUtils {
    private static final Logger log = LoggerFactory.getLogger(WeChatJsDoLoginUtils.class);

    /**
     * 公众号jsApi支付时
     * 获取用户openId（与快捷登录不一样）
     *
     * @param code
     * @return
     */
    public static WeChatUserInfo getOpenId(String code) {
        WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
        //用户同意授权，获取code
        if (code != null) {
            String appId = WeiXinConfig.appid;
            String secret = WeiXinConfig.appSecret;
            //通过code换取网页授权access_token
            SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(appId, secret, code);
            String token = snsAccessToken.getAccessToken();
            String openId = snsAccessToken.getOpenid();
            //拉取用户信息(需scope为 snsapi_userinfo)
            ApiResult apiResult = SnsApi.getUserInfo(token, openId);
            log.warn("getUserInfo:" + snsAccessToken);
            log.info("apiResult -- " + apiResult);
            if (apiResult != null) {
                JSONObject jsonObject = JSON.parseObject(apiResult.getJson());
                String nickName = jsonObject.getString("nickname");
                //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
                int sex = jsonObject.getIntValue("sex");
                //城市
                String city = jsonObject.getString("city");
                //省份
                String province = jsonObject.getString("province");
                //国家
                String country = jsonObject.getString("country");
                String headImgUrl = jsonObject.getString("headimgurl");
                String unionId = jsonObject.getString("unionid");
                //返回用户在公众号中的openId
                weChatUserInfo.setCity(city);
                weChatUserInfo.setSex(sex);
                weChatUserInfo.setProvince(province);
                weChatUserInfo.setCountry(country);
                weChatUserInfo.setNickName(nickName);
                weChatUserInfo.setUnionId(unionId);
                weChatUserInfo.setHeadImgUrl(headImgUrl);
                weChatUserInfo.setOpenId(openId);
                weChatUserInfo.setState(0);
                weChatUserInfo.setMessage("操作成功");
                return weChatUserInfo;
            }
            weChatUserInfo.setOpenId(openId);
            weChatUserInfo.setMessage("获取成功");
            weChatUserInfo.setState(1);
            return weChatUserInfo;
        } else {
            weChatUserInfo.setMessage("code is null");
            weChatUserInfo.setState(1);
            return weChatUserInfo;
        }
    }
}
