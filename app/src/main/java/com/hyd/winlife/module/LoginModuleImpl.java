package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModuleImpl;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.network.HttpRequest;

import io.reactivex.Observable;

/**
 * 登录
 */

public class LoginModuleImpl extends BaseModuleImpl {

    public String getUserName() {
        return "fusang";
    }

    public boolean isLogin() {
        return true;
    }

    public Observable<LoginResult> login(String username, String password) {
        return HttpRequest.getInstance().login(username, password);
//        return Observable.just(result, result2);
    }

    public Observable<LoginResult> autoLogin(String username, String password) {
        return HttpRequest.getInstance().login(username, password);
//        return Observable.just(result, result2);
    }

}
