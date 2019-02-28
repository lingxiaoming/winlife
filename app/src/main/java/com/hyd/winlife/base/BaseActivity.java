package com.hyd.winlife.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyd.winlife.LifeApplication;
import com.hyd.winlife.R;
import com.hyd.winlife.tools.DeviceUtils;
import com.hyd.winlife.tools.StatusBarUtils;

import butterknife.ButterKnife;

/**
 * 基类activity
 * Created by lingxiaoming on 2018-10-29.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
//        initStatusBar();
        mOnCreate();
    }

    public abstract void mOnCreate();

    public abstract int getLayoutId();

    public void showTitle(boolean canBack, String title) {
        ImageView iv_title_left = findViewById(R.id.iv_title_left);
        TextView tv_title = findViewById(R.id.tv_title);

        if (canBack) {
            iv_title_left.setVisibility(View.VISIBLE);
        } else {
            iv_title_left.setVisibility(View.GONE);
        }

        tv_title.setText(title);

        iv_title_left.setOnClickListener(view -> onBackPressed());
    }

    public void showTitleRight(String titleRight) {
        TextView tv_title_right = findViewById(R.id.tv_title_right);

        if (!TextUtils.isEmpty(titleRight)) {
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setText(titleRight);
        }
    }

    /**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) LifeApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    private void initStatusBar(){
        StatusBarUtils.setRootViewFitsSystemWindows(this, false);
        StatusBarUtils.setTranslucentStatus(this);
        View status_bar = findViewById(R.id.view_status_bar);
        if(status_bar != null){
            ViewGroup.LayoutParams params = status_bar.getLayoutParams();
            params.height = DeviceUtils.getStatusBarHeight();
            status_bar.setLayoutParams(params);
            status_bar.setBackgroundColor(getResources().getColor(R.color.blue_0d4e97));
        }
    }

    public void setStatusBar(int color) {
        StatusBarUtils.setRootViewFitsSystemWindows(this, false);
        StatusBarUtils.setTranslucentStatus(this);
        View status_bar = findViewById(R.id.view_status_bar);
        if (status_bar != null) {
            ViewGroup.LayoutParams params = status_bar.getLayoutParams();
            params.height = DeviceUtils.getStatusBarHeight();
            status_bar.setLayoutParams(params);
            status_bar.setBackgroundColor(color);
        }
    }

}
