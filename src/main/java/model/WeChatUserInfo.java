package model;

import lombok.Data;

/**
 * @ClassName WeChatUserInfo
 * @Description 微信用户信息详情model
 * @Author jzm
 * @Date : 2020/1/7 16:43
 **/
@Data
public class WeChatUserInfo {
    /**
     * 微信唯一标识
     */
    private String openId;
    /**
     * 用户名
     */
    private String nickName;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private int sex;

    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;
    /**
     * 头像
     */
    private String headImgUrl;

    /**
     * 有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    private String unionId;

    /**
     * 是否关注
     */
    private int subscribe;
    /**
     * 操作状态 0 正常 1 异常
     */
    private int state;

    /**
     * 操作信息
     */
    private String message;

    public WeChatUserInfo() {
    }

    public WeChatUserInfo(int state, String message) {
        this.state = state;
        this.message = message;
    }
}
