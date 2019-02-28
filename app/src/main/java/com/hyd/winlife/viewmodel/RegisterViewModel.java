package com.hyd.winlife.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.hyd.winlife.arch.BaseViewModel;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.module.RegisterModule;

/**
 * 注册viewmodel
 * Created by lingxiaoming on 2018-10-29.
 */
public class RegisterViewModel extends BaseViewModel {
    public final MutableLiveData<ModuleResult<LoginResult>> registerResult = new MutableLiveData<>();
    public final MutableLiveData<ModuleResult<BaseResult>> getCodeResult = new MutableLiveData<>();

    public void register(String username, String password, String code) {
        ModuleCall<LoginResult> registerCall = getModule(RegisterModule.class).register(username, password, code);
        registerCall.enqueue(result ->
                registerResult.setValue(result)
        );
    }


    public void getCode(String username) {
        ModuleCall<BaseResult> getCodeCall = getModule(RegisterModule.class).getCode(username);
        getCodeCall.enqueue(result ->
                getCodeResult.setValue(result)
        );
    }
}
