package model;



import lombok.Data;
import java.util.Date;

/**
 * 用户(User)表实体类
 *
 * @author Donfy
 * @since 2018-11-08 15:21:20
 */
@Data
public class User  {

    /**
     * 微信openId,
     */
    private String wechatOpenId;

    /**
     * 手机号,
     */
    private String phone;

    /**
     * 用户名,
     */
    private String userName;

    /**
     * 昵称,
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;



    /**
     * 密码盐
     */
    private String salt;

    /**
     * 支付密码,
     */
    private String payPassword;

    /**
     * 头像图片,
     */
    private String avatarUrl;

    /**
     * 邮箱,
     */
    private String email;

    /**
     * 会员等级
     */
    private Integer vipLevel;

    /**
     * 最后登录ip,
     */
    private String lastLoginIp;

    /**
     * 最后登录时间,
     */
    private Date lastLoginDate;

    /**
     * 失败登录次数,
     */
    private Integer loginFailCount;

    /**
     * 登录缓存信息
     */
    private String sid;

    /**
     * 版本号
     */
    private Integer version;
    /**
     * 双乾商户号
     */
    private String qyfNo;



    /**
     * 禁用标识 0 未禁用 1 禁用
     */
    private Integer blackListFlag;

}