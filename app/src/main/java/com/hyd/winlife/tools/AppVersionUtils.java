package com.hyd.winlife.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hyd.winlife.LifeApplication;


/**
 * 获取app版本号
 * Created by lingxiaoming on 2017/9/6 0006.
 */

public class AppVersionUtils {

    public static int getVersionCode() {
        try {
            PackageManager pm = LifeApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(LifeApplication.getInstance().getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName() {
        try {
            PackageManager pm = LifeApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(LifeApplication.getInstance().getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
