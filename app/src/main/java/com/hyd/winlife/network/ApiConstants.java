package com.hyd.winlife.network;

/**
 * 接口名字定义
 * Created by lingxiaoming on 2018-10-25.
 */
public class ApiConstants {
    public static final boolean DEBUG = true;

    /**s
     * 服务器地址
     */
    public static final String MAIN_URL = "https://appservice.huayingdai.net";//正式服务器地址
//    public static final String MAIN_URL_TEST = "http://192.168.3.209:7000";//测试服务器地址
    public static final String MAIN_URL_TEST = "http://192.168.3.53:9000";//小明服务器地址


    /**
     * 接口子地址
     */
    public static final String GET_TOKEN = "/app/services/v2?OPT=9000";
    public static final String GET_USER = "/app/services/v2?OPT=1009";
    public static final String LOGIN = "/app/services/v2?OPT=1001";
    public static final String REGISTER = "/app/services/v2?OPT=1002";
    public static final String GET_BANNER = "/app/services/v2?OPT=1023";
    public static final String GET_MOUDLE = "/app/services/v2?OPT=6010";
    public static final String GET_CODE = "/app/services/v2?OPT=6005";
    public static final String BASE_URL = MAIN_URL_TEST;


    /**
     * token用
     */
    public static final String PARTNER_ID = "111111";
    public static final String APP_KEY = "20160321";
    public static final String SCOPE = "hyd-app-advance";

    /**
     * 通用：1安卓，2iOS
     */
    public static final String DEVICE_TYPE = "1";//1(android)、2(ios)、3(web)
//    public static final String MD5_KEY = "oof5jGuvXxeznqRW";//签名用
    public static final String MD5_KEY = "OqxsqDFSFGDASHVkw";//签名用 TODO 测试
    public static final String SIGN_TYPE = "MD5"; //签名类型，1(MD5)
}
