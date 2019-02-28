package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModule;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ProxyTarget;
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;

/**
 * 注册
 */
@ProxyTarget(RegisterModuleImpl.class)
public interface RegisterModule extends BaseModule {

    ModuleCall<LoginResult> register(String username, String password, String code);
    ModuleCall<BaseResult> getCode(String username);

}
