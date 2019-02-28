package com.hyd.winlife.base;


import com.hyd.winlife.network.ApiException;
import com.hyd.winlife.tools.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * rxjava的回调封装下，返回结果做统一处理
 * Created by lingxiaoming on 2017/7/25 0025.
 */

public abstract class MyObserver<T> implements Observer<T> {
    private static final String TAG = "MyObserver";


    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        LogUtils.d(TAG, "onNext: " + t.toString());
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtils.e(TAG, "onError: " + e.getMessage() + Thread.currentThread().getName());
        String errorMessage = e.getMessage();
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            LogUtils.e(TAG, "ApiException onError: " + apiException.errorCode + " " + e.getMessage());
        } else if (e instanceof HttpException) {
            errorMessage = "服务器内部错误";
        } else if (e instanceof IOException) {
            errorMessage = "您的网络不给力<br>请稍后再试";
        }
    }

    public void onOtherError(int error_code) {

    }
}
