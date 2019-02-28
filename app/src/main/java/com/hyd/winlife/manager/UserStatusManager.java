package com.hyd.winlife.manager;

import android.text.TextUtils;

/**
 * 用户状态判断类：是否登录，是否开户，是否绑卡，是否设置密码
 * Created by lingxiaoming on 2017/5/16 0016.
 */

public class UserStatusManager {
    public static boolean hasLogin() {
        return !TextUtils.isEmpty(DataManager.getId());
    }
}
