package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModuleImpl;
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.network.HttpRequest;

import io.reactivex.Observable;

/**
 * 注册
 */

public class RegisterModuleImpl extends BaseModuleImpl {

    public Observable<LoginResult> register(String username, String password, String code) {
        return HttpRequest.getInstance().register(username, password, code);
    }

    public Observable<BaseResult> getCode(String username) {
        return HttpRequest.getInstance().getCode(username);
    }
}
