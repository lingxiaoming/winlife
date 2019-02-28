package com.hyd.winlife.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hyd.winlife.LifeApplication;

/**
 * 获取设备唯一id
 * Created by lingxiaoming on 2017/9/8 0008.
 */

public class DeviceUtils {
    /**
     * 获取设备类型
     *
     * @return
     */
    public static String getDeviceId() {
        return Settings.Secure.getString(LifeApplication.getInstance().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getMobileInfo() {
        return Build.MODEL + ":" + Build.VERSION.RELEASE;
    }

    public static int getScreenHeight() {
        return getDisplay().getHeight();
    }


    private static Display getDisplay() {
        return ((WindowManager) LifeApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static int getScreenWidth() {
        return getDisplay().getWidth();
    }

    /**
     * @方法说明:获取状态栏高度
     * @返回值:int
     */
    public static int getStatusBarHeight() {
        int resourceId = LifeApplication.getInstance().getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (resourceId > 0) {
                return LifeApplication.getInstance().getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static int getDPI() {
        DisplayMetrics dm = new DisplayMetrics();
        getDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dp2px(float dipValue) {
        final float scale = LifeApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getVersionName() {
        PackageInfo pkg = null;
        try {
            pkg = LifeApplication.getInstance().getApplicationContext().getPackageManager().getPackageInfo(LifeApplication.getInstance().getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pkg.versionName;// 版本号显示用
//        versionCode = pkg.versionCode;// 版本号比较服务器用
        return versionName;
    }
}
