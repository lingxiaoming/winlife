package com.hyd.winlife.network;

/**
 * User: xiaoming
 * Date: 2016-06-05
 * Time: 16:52
 * 所有接口的key常量保存这里吧
 */

public class ParamContants {
    //公共请求参数
    public static final String OPT = "OPT"; //api具体方法
    public static final String BODY = "body"; //api请求体
    public static final String ACCESS_TOKEN = "access_token"; //令牌
    public static final String NONCE = "nonce"; //32位请求随机字符串，用于标识签名的唯一性
    public static final String TIMESTAMP = "timestamp"; //客户端请求时间戳
    public static final String VERSION = "version"; //客户端请求的API版本号：1.0
    public static final String DEVICE_ID = "device_id"; //设备唯一标识
    public static final String SIGN_TYPE = "sign_type"; //签名的摘要算法：1(MD5)
    public static final String DEVICE_TYPE = "device_type"; //设备类型：1(android)、2(ios)、3(web)


    public static final String SIGN = "sign"; //把当前所有请求参数(sign除外)使用指定的算法进行处理

    //接口请求参数key
    public static final String CURRENT_PAGE = "page_no";//当前页数，默认第1页
    public static final String PAGE_SIZE = "page_size";//分页条数，默认10条

    /**
     * 登录/注册
     */
    public static final String NAME = "name";
    public static final String PASSWORD = "pwd";
    public static final String MOBILECODE = "mobileCode";
    public static final String TYPE = "type";



    public static final String USERNAME = "mobile";
    public static final String DEVICEID = "device_id";


    /**
     * 获取token
     */
    public static final String PARTNER_ID = "partner_id";
    public static final String APP_KEY = "app_key";
    public static final String SCOPE = "scope";
    public static final String DEVICE_SCREEN = "device_screen";


    /**
     * 获取用户信息
     */
    public static final String ID = "id";
    public static final String USER_ID = "userId";

    /**
     * 获取banner
     */
    public static final String ADVNAME = "advName";//banner广告传参

    /**
     * 获取模块位
     */
    public static final String MODULENAME = "moduleName";//模块传参






}
