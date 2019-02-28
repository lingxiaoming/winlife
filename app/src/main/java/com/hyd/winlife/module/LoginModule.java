package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModule;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ProxyTarget;
import com.hyd.winlife.bean.LoginResult;

/**
 * 登录
 */
@ProxyTarget(LoginModuleImpl.class)
public interface LoginModule extends BaseModule {

    String getUserName();

    boolean isLogin();

    ModuleCall<LoginResult> login(String username, String password);

}
