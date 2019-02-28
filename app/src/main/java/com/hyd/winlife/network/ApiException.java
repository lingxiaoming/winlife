package com.hyd.winlife.network;


/**
 * 接口返回的数据有问题
 * Created by lingxiaoming on 2017/7/25 0025.
 */

public class ApiException extends RuntimeException {
    public static final int ERROR_TOKEN_EXPIRE = -999;//token过期
    public static final int ERROR_TOKEN_NULL = 9012;//token不能传null
    public static final int ERROR_UNLOGIN = 1001;//未登录
//    public static final int ERROR_HASREGISTER = 1002;//此手机号已注册
//    public static final int ERROR_UNREGISTER = 1014;//此手机号未注册
    public static final int ERROR_OTHER_LOGIN = 1049;//账号在其他地方登陆，被迫下线
    public static final int ERROR_UN_SAFE = 1053;//风控不通过，进入失败页面


    public static final int SUCCESS_CODE = 0;//成功

    public static final int ERROR_CODE_OTHER = -1;//其他错误
    public static final int ERROR_CODE_SIGN = -2;//签名校验失败
//    public static final int ERROR_CODE_AUTOLOGIN = -3;//自动登录失败



    public int errorCode;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }




}
