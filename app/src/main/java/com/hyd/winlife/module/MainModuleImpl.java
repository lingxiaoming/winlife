package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModuleImpl;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.User;
import com.hyd.winlife.network.HttpRequest;

import io.reactivex.Observable;

/**
 * 主页面
 */

public class MainModuleImpl extends BaseModuleImpl {

    public String getUserName() {
        return "fusang";
    }

    public boolean isLogin() {
        return true;
    }

    public Observable<User> getUserInfo() {
        // 模拟实现
//        LoginResult result = new LoginResult();
//        result.success = true;
//        result.userid = username;
//
//        LoginResult result2 = new LoginResult();
//        result2.success = false;
//        result2.userid = password;
//
        return HttpRequest.getInstance().getUserInfo();
//        return Observable.just(result, result2);
    }

    public Observable<AdvListWapper> getBanner(){
        return HttpRequest.getInstance().getBanner();
    }

    public Observable<MoudleWapper> getMoudle(){
        return HttpRequest.getInstance().getMoudle();
    }

}
