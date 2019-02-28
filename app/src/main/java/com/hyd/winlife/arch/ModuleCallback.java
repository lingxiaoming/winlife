package com.hyd.winlife.arch;

/**
 * Module异步调用的回调接口
 */
public interface ModuleCallback<T> {

    void onModuleCallback(ModuleResult<T> result);

}
