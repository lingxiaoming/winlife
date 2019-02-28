package com.hyd.winlife.network;

/**
 * 返回数据基类
 * Created by lingxiaoming on 2017/7/25 0025.
 */

public class BaseResponse<T> {
    public String version;//当前最新的API版本号
    public T body;

    public String opt;//api具体方法
    public String msg;//操作结果描述信息
    public int sign_type;//签名的摘要算法：1（MD5）
    public String nonce;//32位请求随机字符串，用于标识签名的唯一性
    public int error = -1;//操作结果返回码，给个默认值避免默认是0成功
    public String timestamp;//服务端返回时间戳
    public String sign;//把当前所有请求参数（sign除外）使用指定的算法进行处理


    public boolean isSuccess(){
        return error == ApiException.SUCCESS_CODE;
    }
}
