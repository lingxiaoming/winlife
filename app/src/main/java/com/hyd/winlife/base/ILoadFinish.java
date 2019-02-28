package com.hyd.winlife.base;

/**
 * http请求返回结果
 * Created by lingxiaoming on 2016/7/8 0008.
 */
public interface ILoadFinish<T> {
    void success(T object);
    void fail(String errorMsg);
}
