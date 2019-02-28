package com.hyd.winlife.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.hyd.winlife.arch.BaseViewModel;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.User;
import com.hyd.winlife.module.MainModule;

/**
 * 主页面viewmodel
 * Created by lingxiaoming on 2018-10-29.
 */
public class MainViewModel extends BaseViewModel {
    public final MutableLiveData<ModuleResult<User>> userResult = new MutableLiveData<>();
    public final MutableLiveData<ModuleResult<AdvListWapper>> advListResult = new MutableLiveData<>();
    public final MutableLiveData<ModuleResult<MoudleWapper>> moudleResult = new MutableLiveData<>();

    public void getUserInfo() {
        ModuleCall<User> userCall = getModule(MainModule.class).getUserInfo();
        userCall.enqueue(result ->
                userResult.setValue(result)
        );
    }


    public void getBanner(){
        ModuleCall<AdvListWapper> advListCall = getModule(MainModule.class).getBanner();
        advListCall.enqueue(result ->
                advListResult.setValue(result)
        );
    }


    public void getMoudle(){
        ModuleCall<MoudleWapper> advListCall = getModule(MainModule.class).getMoudle();
        advListCall.enqueue(result ->
                moudleResult.setValue(result)
        );
    }
}
