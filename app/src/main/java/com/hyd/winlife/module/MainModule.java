package com.hyd.winlife.module;


import com.hyd.winlife.arch.BaseModule;
import com.hyd.winlife.arch.ModuleCall;
import com.hyd.winlife.arch.ProxyTarget;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.User;

/**
 * 主页面
 */
@ProxyTarget(MainModuleImpl.class)
public interface MainModule extends BaseModule {

    public static final String BANNER_LIFE_TOP = "WINLIFE_TOP_ADV";//赢生活页面上方(美食图片)
    public static final String BANNER_HOME_PREFERENCE_ONE = "HOME_PREFERENCE_ONE";//新版首页杭品优选一
    public static final String BANNER_HOME_PREFERENCE_TWO = "HOME_PREFERENCE_TWO";//新版首页杭品优选二

    public static final String MODULE_HOME_MIDDLE = "APP_HOME_MIDDLE_MD";//首页中间模块位

    String getUserName();

    boolean isLogin();

    ModuleCall<User> getUserInfo();//用户信息

    ModuleCall<AdvListWapper> getBanner();//广告图

    ModuleCall<MoudleWapper> getMoudle();//模块

}
