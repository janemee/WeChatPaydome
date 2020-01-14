package model;

import lombok.Data;

/**
 * @ClassName ResultEntity
 * @Description TODO
 * @Author jzm
 * @Date : 2020/1/10 11:03
 **/
@Data
public class ResultEntity<T> {
    public static final int SUCCESS = 200;
    public static final int LOADING = 300;
    public static final int FAIL = 400;
    //未注册
    public static final int NO_REGISTER = 2001;
    //手机未认证
    public static final int NO_MOBILE_AUTH = 201;
    //实名未认证
    public static final int NO_REALNAME_AUTH = 202;
    //实名认证中
    public static final int REALNAME_AUTHING = 203;
    //支付密码未设置
    public static final int PAY_PASSWOR_NOT_SET = 204;
    //没有绑定任何银行卡
    public static final int NO_BANK_CARD_AUTH = 205;

    //业务错误报错
    public static final int BUSINESS_ERROR = 2002;

    private int code;
    private String message = "";
    private String url = "";
    private T data;
    private String uuid;

    public static ResultEntity success() {
        ResultEntity restResult = new ResultEntity();
        restResult.code = SUCCESS;
        restResult.message = "操作成功";
        return restResult;
    }

    public static ResultEntity success(Object data) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = SUCCESS;
        restResult.message = "操作成功";
        restResult.data = data;
        return restResult;
    }
    public static ResultEntity success(String msg) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = ResultEntity.SUCCESS;
        restResult.message = msg;
        return restResult;
    }
    public static ResultEntity success(Object data,String msg) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = ResultEntity.SUCCESS;
        restResult.message = msg;
        restResult.data = data;
        return restResult;
    }

    public static ResultEntity success(String msg,Object data) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = ResultEntity.SUCCESS;
        restResult.message = msg;
        restResult.data = data;
        return restResult;
    }

    public static ResultEntity fail() {
        ResultEntity restResult = new ResultEntity();
        restResult.code = FAIL;
        restResult.message = "操作失败";
        return restResult;
    }

    public static ResultEntity fail(String msg) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = FAIL;
        restResult.message = msg;
        return restResult;
    }
    public static ResultEntity fail(String msg,int code) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = code;
        restResult.message = msg;
        return restResult;
    }

}
