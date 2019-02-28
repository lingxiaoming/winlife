package com.hyd.winlife.network;

import android.text.TextUtils;

import com.hyd.winlife.base.MyObserver;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.bean.Token;
import com.hyd.winlife.manager.DataManager;
import com.hyd.winlife.tools.ActivityManager;
import com.hyd.winlife.tools.LogUtils;
import com.hyd.winlife.ui.LoginActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 用代理的方式拦截方法，做token刷新和重新请求数据
 * Created by lingxiaoming on 2017/7/26 0026.
 */

public class ProxyHandler implements InvocationHandler {
    private Object mProxyObject;
    private HttpBaseRequest mHttpBaseRequest;

    public ProxyHandler(Object proxyObject, HttpBaseRequest httpBaseRequest) {
        this.mProxyObject = proxyObject;
        this.mHttpBaseRequest = httpBaseRequest;
    }


    @Override
    public Object invoke(Object o, final Method method, final Object[] args) throws Throwable {
        return Observable.just("").flatMap((Function<Object, Observable<?>>) o1 -> {
            try {
                return (Observable<?>) method.invoke(mProxyObject, args);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).retryWhen(observable -> observable.flatMap(throwable -> {
            if (throwable instanceof ApiException) {
                ApiException apiException = (ApiException) throwable;
                if (apiException.errorCode == ApiException.ERROR_TOKEN_EXPIRE || apiException.errorCode == ApiException.ERROR_TOKEN_NULL) {//token过期或为空
                    return refreshTokenWhenTokenInvalid(new HashMap<>());
                } else if (apiException.errorCode == ApiException.ERROR_UNLOGIN) {//用户没有登录
                    // 当本地有用户登录信息时，自动登录，否则跳转登录页
                    if (!TextUtils.isEmpty(DataManager.getMobile()) && !TextUtils.isEmpty(DataManager.getPwd()))
                        return autoLogin(new HashMap<>());
                    else
                        LoginActivity.go(ActivityManager.getAppManager().currentActivity());
                }
            }
            return Observable.error(throwable);
        }));
    }

    private static boolean getTokenSuccess = false;//防止多次请求token

    private Observable<?> refreshTokenWhenTokenInvalid(HashMap<String, Throwable> map) {
        getTokenSuccess = false;
        synchronized (ProxyHandler.class) {
            if (getTokenSuccess) return Observable.just(true);

            HttpRequest.getInstance().getTokenSync().subscribe(new MyObserver<Token>() {
                @Override
                public void onNext(@NonNull Token token) {
                    super.onNext(token);
                    getTokenSuccess = true;
                    DataManager.setToken(token.access_token);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);
                    map.put("error", e);
                    LogUtils.e("ProxyHandler", "自动获取Token失败 e: " + e);
                }
            });
            if (map.containsKey("error")) {
                return Observable.error(map.get("error"));
            } else {
                return Observable.just(true);
            }
        }
    }

    private static boolean autoLoginSuccess = false;//防止多次请求自动

    private Observable<?> autoLogin(HashMap<String, Throwable> map) {
        autoLoginSuccess = false;
        synchronized (ProxyHandler.class) {
            if (autoLoginSuccess) return Observable.just(true);
            if (TextUtils.isEmpty(DataManager.getMobile()) || TextUtils.isEmpty(DataManager.getPwd()))
                return Observable.just(true);
            HttpRequest.getInstance().autoLogin(DataManager.getMobile(), DataManager.getPwd()).subscribe(new MyObserver<LoginResult>() {
                @Override
                public void onNext(@NonNull LoginResult result) {
                    super.onNext(result);
                    autoLoginSuccess = true;
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);
                    map.put("error", e);
                    LogUtils.e("ProxyHandler", "自动登录失败 e: " + e);
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        if (apiException.errorCode == apiException.ERROR_OTHER_LOGIN) {
                            LogUtils.e("ProxyHandler", "其他地方登录 e: " + e);
                            return;
                        }
                    }
                }
            });
            if (map.containsKey("error")) {
                return Observable.error(map.get("error"));
            } else {
                return Observable.just(true);
            }
        }
    }
}
