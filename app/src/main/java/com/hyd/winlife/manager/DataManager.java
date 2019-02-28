package com.hyd.winlife.manager;

import android.text.TextUtils;

import com.hyd.winlife.bean.User;
import com.hyd.winlife.tools.PrefsUtils;

/**
 * 数据保存
 * Created by lingxiaoming on 2018-10-29.
 */
public class DataManager {
    private static String token;
    private static String mobile;
    private static String pwd;
    private static String id;


    private static User user = new User();


    public synchronized static void setToken(String token) {
        DataManager.token = token;
        PrefsUtils.getInstance().saveStringByKey(PrefsUtils.ACCESS_TOKEN, token);
    }

    public synchronized static String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = PrefsUtils.getInstance().getStringByKey(PrefsUtils.ACCESS_TOKEN);
        }
        return token;
    }



    public synchronized static void setId(String id) {
        DataManager.id = id;
        PrefsUtils.getInstance().saveStringByKey(PrefsUtils.LOGIN_ID, id);
    }

    public synchronized static String getId() {
        if (TextUtils.isEmpty(id)) {
            id = PrefsUtils.getInstance().getStringByKey(PrefsUtils.LOGIN_ID);
        }
        return id;
    }


    public synchronized static String getMobile() {
        if (TextUtils.isEmpty(mobile)) {
            mobile = PrefsUtils.getInstance().getStringByKey(PrefsUtils.MOBILE);
        }
        return mobile;
    }

    public synchronized static void setMobile(String mobile) {
        DataManager.mobile = mobile;
        PrefsUtils.getInstance().saveStringByKey(PrefsUtils.MOBILE, mobile);
    }


    public synchronized static String getPwd() {
        if (TextUtils.isEmpty(pwd)) {
            pwd = PrefsUtils.getInstance().getStringByKey(PrefsUtils.PWD);
        }
        return pwd;
    }

    public synchronized static void setPwd(String pwd) {
        DataManager.pwd = pwd;
        PrefsUtils.getInstance().saveStringByKey(PrefsUtils.PWD, pwd);
    }
}
