package com.hyd.winlife.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.hyd.winlife.arch.BaseViewModel;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.module.LoginModule;

/**
 * 登录viewmodel
 * Created by lingxiaoming on 2018-10-29.
 */
public class LoginViewModel extends BaseViewModel {
    public final MutableLiveData<ModuleResult<LoginResult>> loginResult = new MutableLiveData<>();

    public void login(String username, String password) {
        ModuleCall<LoginResult> loginCall = getModule(LoginModule.class).login(username, password);
        loginCall.enqueue(result ->
                loginResult.setValue(result)
        );
    }
}
