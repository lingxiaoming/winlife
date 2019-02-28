package com.hyd.winlife.tools;

import android.util.Log;

/**
 * Log管理类
 * Created by lingxiaoming on 2017/7/25 0025.
 */

public class LogUtils {
    private static final boolean DEBUG = true;


    public static void i(String tag, String content){
        if(DEBUG) Log.i(tag, content);
    }

    public static void d(String tag, String content){
        if(DEBUG) Log.d(tag, content);
    }

    public static void e(String tag, String content){
        if(DEBUG) Log.e(tag, content);
    }

    public static void v(String tag, String content){
        if(DEBUG) Log.v(tag, content);
    }

    public static void w(String tag, String content){
        if(DEBUG) Log.w(tag, content);
    }
}
