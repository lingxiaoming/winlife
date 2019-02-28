package com.hyd.winlife.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.hyd.winlife.network.ApiConstants;

public class UrlUtils {
    public static final String TAG = "PushDemoActivity";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static String getImageUrl(String url){
        if (!url.startsWith("http")) {
            if (url.startsWith("/")) {
                url = ApiConstants.BASE_URL + url;
            } else {
                url = ApiConstants.BASE_URL + "/" + url;
            }
        }
    	return url;
    }
}
