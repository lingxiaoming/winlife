package com.hyd.winlife.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyd.winlife.R;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.base.BaseActivity;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.manager.DataManager;
import com.hyd.winlife.manager.UserStatusManager;
import com.hyd.winlife.network.ApiConstants;
import com.hyd.winlife.tools.DeviceUtils;
import com.hyd.winlife.tools.LogUtils;
import com.hyd.winlife.tools.ToastUtils;
import com.hyd.winlife.viewmodel.LoginViewModel;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;



public class WebViewActivity extends BaseActivity {
    private String TAG = "CommonWebViewActivity";

    private String url;
    private WebView mWebView;
    private TextView tv_right;
    private ImageView mIvCancel;//返回按钮右边的关闭按钮X

    private View tv_progress, progress_bar;//加载中布局

    private final String mainUrlPart = "/front/scoreMall/goodsListApp";  //商城首页
    private final String buyHistoryUrlPart = "/front/scoreMall/userOrdersApp";  //用户购买记录
    private final String safe = "/wealthinfomation/safeguard";  //安全保障
    private final String biLL = "/front/wealthinfomation/userBillInfo";  // 年度账单

    private boolean needClearHistory = false;
    private String title = "";

    private BroadcastReceiver receiver;

    /**
     * 加载Html页面（仅加载，不判断）
     *
     * @param context 上下文
     * @param url     html地址
     */
    public static void goToTarget(Context context, String url) {
        if (!url.startsWith("http")) {
            if (url.startsWith("/")) {
                url = ApiConstants.BASE_URL + url;
            } else {
                url = ApiConstants.BASE_URL + "/" + url;
            }
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 跳转至Html页面（html需要登录验证）
     *
     * @param context   上下文
     * @param singleUrl 指定页面的简短url，如服务协议 "/index/protocol"
     */
    public static void goToLoginTarget(Context context, String singleUrl) {
        if (singleUrl.contains("?")) {
            goToTarget(context, singleUrl + "&signId="
                    + DataManager.getId() + (singleUrl.contains("id=") ? "" : "&id=" + DataManager.getId()));
        } else {
            goToTarget(context, singleUrl + "?signId="
                    + DataManager.getId() + (singleUrl.contains("id=") ? "" : "&id=" + DataManager.getId()));
        }
    }

    public static void goToTargetNoHttp(Context context, String url) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme() == null) {
            ToastUtils.showText("无效的二维码：" + uri);
        } else if (uri.getScheme().equals("gotohyd")) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWebView.loadUrl(intent.getStringExtra("url"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    public void mOnCreate() {
        url = getIntent().getStringExtra("url");
        findViewById(R.id.iv_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_progress = findViewById(R.id.tv_progress);
        progress_bar = findViewById(R.id.progress_bar);
        mIvCancel = findViewById(R.id.iv_cancel);
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_right = (TextView) findViewById(R.id.tv_title_right);

        mWebView = findViewById(R.id.mainWebview);
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        String userAgent = settings.getUserAgentString();//加唯一标识
        settings.setUserAgentString(userAgent + " hyd/" + DeviceUtils.getVersionName() + " resolution/" + DeviceUtils.getScreenWidth() + "*" + DeviceUtils.getScreenHeight() + " device_id/" + DeviceUtils.getDeviceId());

        settings.setGeolocationEnabled(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        mWebView.addJavascriptInterface(this, "webkit");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ToastUtils.showText("网络请求超时");
                view.stopLoading();
                onBackPressed();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.d(TAG, "onPageFinished:" + url);

                if (mWebView.canGoBack()) {
                    mIvCancel.setVisibility(View.VISIBLE);
                } else {
                    mIvCancel.setVisibility(View.GONE);
                }

                tv_progress.setVisibility(View.GONE);
                progress_bar.setVisibility(View.GONE);

                mWebView.setVisibility(View.VISIBLE);
                if (mWebView.getUrl().contains(mainUrlPart)) {
                    tv_right.setVisibility(View.VISIBLE);
                    tv_right.setText("购买记录");
                } else {
                    tv_right.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(TAG, "shouldOverrideUrlLoading:" + url);
                if (url.contains("phoneErrorInfo") && url.contains("RespCode=-1000") && url.contains("msg")) {
                    if (UserStatusManager.hasLogin() && !TextUtils.isEmpty(DataManager.getPwd())) {
                        autoLogin(DataManager.getMobile(), DataManager.getPwd());
                        return true;
                    } else {
                        LoginActivity.go(WebViewActivity.this);
                        finish();
                    }
                }

                if (url.startsWith("https://ttc.h5cb.com")) {//天天彩
                    findViewById(R.id.ic_top).setVisibility(View.GONE);
                }

                Uri uri = Uri.parse(url);
                if(!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    needClearHistory = false;
                    mWebView.clearHistory();//清除历史记录
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (mWebView.canGoBack()) {
                    mIvCancel.setVisibility(View.VISIBLE);
                } else {
                    mIvCancel.setVisibility(View.GONE);
                }
                super.onReceivedTitle(view, title);
                showTitle(true, title);
                WebViewActivity.this.title = title;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }


            @Override
            public void onProgressChanged(WebView webView, int i) {
                LogUtils.d(TAG, "progress:" + i);
                if (i > 80) {
                    tv_progress.setVisibility(View.GONE);
                    progress_bar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(webView, i);
            }
        });

        if (!isNetworkConnected()) {
            ToastUtils.showText("无法连接到网络");
            return;
        }

        mWebView.loadUrl(url);
        mWebView.setVisibility(View.INVISIBLE);
    }

    @JavascriptInterface
    public void hideTop(final boolean hide) {
        LogUtils.d(TAG, "hideTop arg:" + hide);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.ic_top).setVisibility(hide ? View.GONE : View.VISIBLE);
            }
        });
    }

    @JavascriptInterface
    public void autoLogin(String mobile, String pwd) {
        LoginViewModel mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.loginResult.observe(this, mLoginObserver);
        mLoginViewModel.login(mobile, pwd);
    }

    private Observer<ModuleResult<LoginResult>> mLoginObserver = (result -> {
//        Toast.makeText(getApplicationContext(), "data=" + result.data + " e=" + result.error, Toast.LENGTH_SHORT).show();

        if (result.data == null) {
            ToastUtils.showText("登录失败:" + result.error.getMessage());
            LoginActivity.go(this);
            WebViewActivity.this.finish();
        } else {
            LoginResult loginResult = result.data;
            ToastUtils.showText("用户id:" + loginResult.id);
            DataManager.setId(loginResult.id);
            needClearHistory = true;
            if (url.lastIndexOf("signId=") < 0) {
                url = url + "&signId=" + loginResult.id;
            } else {
                url = url.substring(0, url.lastIndexOf("signId=")) + "signId=" + loginResult.id;
            }
            LogUtils.d(TAG, "relogin end and load url:" + url);
            mWebView.loadUrl(url);
        }

    });

    @JavascriptInterface
    public void jsBackFinish() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mWebView == null || mWebView.getUrl() == null) {
            finish();
            return;
        }

        LogUtils.d(TAG, "onBackPressed:" + mWebView.getUrl());
        if (mWebView.getUrl().startsWith("https://ttc.h5cb.com")) {//天天彩
            mWebView.evaluateJavascript("javascript:window.webkit.back()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    LogUtils.d("彩票页面返回值", s);
                }
            });
            return;
        }

        if (findViewById(R.id.ic_top).getVisibility() == View.GONE) {
            mWebView.loadUrl("javascript:HydInterceptBack()");
            return;
        }

        if (mWebView.getUrl().contains(buyHistoryUrlPart)) {
            mWebView.loadUrl(url);
            return;
        }
        if (!mWebView.canGoBack()) {
            finish();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
        }
    }


    @Override
    protected void onDestroy() {
        //清楚缓存cookie
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
        super.onDestroy();
    }
}
