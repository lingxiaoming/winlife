package com.hyd.winlife;

import android.app.Application;
import android.os.Environment;

import com.hyd.winlife.tools.CrashHandler;
import com.tencent.smtt.sdk.QbSdk;

/**
 * application
 * Created by lingxiaoming on 2017/7/25 0025.
 */

public class LifeApplication extends Application {
    private static LifeApplication application;

    public static synchronized LifeApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        CrashHandler.getInstance().init(this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/winlife");
        QbSdk.initX5Environment(getApplicationContext(),  null); //x5内核初始化接口
    }
}
