package com.hyd.winlife.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.TestPayResult;
import com.hyd.winlife.bean.Token;
import com.hyd.winlife.bean.User;
import com.hyd.winlife.manager.DataManager;
import com.hyd.winlife.module.MainModule;
import com.hyd.winlife.tools.DeviceUtils;
import com.hyd.winlife.tools.SignUtils;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * http请求
 * Created by lingxiaoming on 2018-10-29.
 */
public class HttpRequest extends HttpBaseRequest {
    private Gson gson;
    protected static HttpRequest mHttpRequest;

    public static HttpRequest getInstance() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }

    private HttpRequest() {
        super();
        gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    }

    public void release() {
        if (mHttpRequest != null) {
            mHttpRequest = null;
        }
    }

//    public static String KEY_DATA = "WJucOR9PGq7DMAKg";
    public static String KEY_DATA = "OqxsqDFSFGDASHVkw";//TODO 测试支付api

    public static String encrypt3DES(String input) {
        return com.shove.security.Encrypt.encrypt3DES(input, KEY_DATA);
    }

    public static String decrypt3DES(String input) {
        return com.shove.security.Encrypt.decrypt3DES(input, KEY_DATA);
    }


    /**
     * 获取令牌（support/getToken）（主动获取）
     *
     * @return
     */
    public Observable<Token> getToken() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.DEVICEID, DeviceUtils.getDeviceId());
        String jsonString = getJsonString(hashMap);
        return getProxy(HttpInterface.class).getToken(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 同步手机号登录，做自动登录用（user/login）
     *
     * @param username
     * @param password
     * @return
     */
    public Observable<LoginResult> autoLogin(String username, String password) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.USERNAME, username);
        hashMap.put(ParamContants.PASSWORD, encrypt3DES(password));
        return getProxy(HttpInterface.class).login(hashMap);
    }


    /**
     * 同步获取令牌，做自动获取token和重连上次请求用（support/getToken）
     *
     * @return
     */
    public Observable<Token> getTokenSync() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.PARTNER_ID, ApiConstants.PARTNER_ID);
        hashMap.put(ParamContants.APP_KEY, ApiConstants.APP_KEY);
        hashMap.put(ParamContants.SCOPE, ApiConstants.SCOPE);
        hashMap.put(ParamContants.DEVICE_SCREEN, DeviceUtils.getScreenHeight() + "*" + DeviceUtils.getScreenWidth() + "*" + DeviceUtils.getDPI());
        return getProxy(HttpInterface.class).getToken(hashMap);
    }

    /**
     * 登录（support/login）
     *
     * @return
     */
    public Observable<LoginResult> login(String mobile, String pwd) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.NAME, mobile);
        hashMap.put(ParamContants.PASSWORD, encrypt3DES(pwd));
        hashMap.put("deviceType", "1");
        return getProxy(HttpInterface.class).login(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册（support/register）
     *
     * @return
     */
    public Observable<LoginResult> register(String mobile, String pwd, String code) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.NAME, mobile);
        hashMap.put(ParamContants.PASSWORD, encrypt3DES(pwd));
        hashMap.put(ParamContants.MOBILECODE, code);
        return getProxy(HttpInterface.class).register(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册获取验证码（support/getCode）
     *
     * @return
     */
    public Observable<BaseResult> getCode(String mobile) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.NAME, mobile);
        hashMap.put(ParamContants.TYPE, 1 + "");
        return getProxy(HttpInterface.class).getCode(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取用户信息（support/getToken）（主动获取）
     *
     * @return
     */
    public Observable<User> getUserInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.ID, DataManager.getId());
        return getProxy(HttpInterface.class).getUser(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取banner广告信息
     *
     * @return
     */
    public Observable<AdvListWapper> getBanner() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.USER_ID, DataManager.getId());
        hashMap.put(ParamContants.ADVNAME, MainModule.BANNER_LIFE_TOP + "," + MainModule.BANNER_HOME_PREFERENCE_ONE + "," + MainModule.BANNER_HOME_PREFERENCE_TWO);
        return getProxy(HttpInterface.class).getBanner(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取首页模块位
     *
     * @return
     */
    public Observable<MoudleWapper> getMoudle() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParamContants.USER_ID, DataManager.getId());
        hashMap.put(ParamContants.MODULENAME, MainModule.MODULE_HOME_MIDDLE);
        return getProxy(HttpInterface.class).getMoudle(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 测试扫码支付
     *
     * @return
     */
    public Observable<TestPayResult> pay(String qrcode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("qrcode", encrypt3DES(qrcode));
        hashMap.put("trade_amount", encrypt3DES("99.98"));
        hashMap.put("order_num", encrypt3DES(SignUtils.getRandomString(32)));
        hashMap.put("order_expire_time", encrypt3DES(System.currentTimeMillis() + 1000 * 60 * 10 + ""));
        hashMap.put("pay_title", encrypt3DES("订单标题，订单号是:"+hashMap.get("order_num")));
        return getProxy(HttpInterface.class).pay(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 测试扫码支付查询结果
     *
     * @return
     */
    public Observable<TestPayResult> searchPayResult(String order_num) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("order_num", encrypt3DES(order_num));
        return getProxy(HttpInterface.class).orderQuery(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 测试退款
     *
     * @return
     */
    public Observable<TestPayResult> refund(String trade_num) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("refund_amount", encrypt3DES("2.51"));
        hashMap.put("trade_num", encrypt3DES(trade_num));
        hashMap.put("refund_num", encrypt3DES(SignUtils.getRandomString(32)));
        return getProxy(HttpInterface.class).refund(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 测试退款查询
     *
     * @return
     */
    public Observable<TestPayResult> refundSearch(String trade_num) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("trade_num", encrypt3DES(trade_num));
        return getProxy(HttpInterface.class).refundQuery(hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private String getJsonString(HashMap<String, String> map) {
        if (gson == null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        }

        if (map == null || map.size() == 0) {
            map = new HashMap<>();
        }

        return gson.toJson(map);
    }
}
