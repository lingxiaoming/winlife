package com.hyd.winlife.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyd.winlife.R;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.base.BaseActivity;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.manager.DataManager;
import com.hyd.winlife.tools.StatusBarUtils;
import com.hyd.winlife.tools.ToastUtils;
import com.hyd.winlife.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 * Created by lingxiaoming on 2018-10-29.
 */
public class LoginActivity extends BaseActivity {
    private LoginViewModel mLoginViewModel;

    @BindView(R.id.et_name)
    public EditText mEtName;

    @BindView(R.id.et_pwd)
    public EditText mEtPwd;

    @BindView(R.id.btn_login)
    public Button mBtnLogin;

    public static void go(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void mOnCreate() {
        StatusBarUtils.setStatusBarDarkTheme(this, true);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.loginResult.observe(this, mLoginObserver);
        initDatas();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    private void initDatas() {
        mEtName.setText(DataManager.getMobile());
        mEtPwd.setText(DataManager.getPwd());
    }


    private Observer<ModuleResult<LoginResult>> mLoginObserver = (result -> {
//        Toast.makeText(getApplicationContext(), "data=" + result.data + " e=" + result.error, Toast.LENGTH_SHORT).show();

        if (result.data == null) {
            ToastUtils.showText("登录失败:" + result.error.getMessage());
        } else {
            LoginResult loginResult = result.data;
            ToastUtils.showText("用户id:" + loginResult.id);
            DataManager.setId(loginResult.id);
            MainActivity.go(this);
        }

    });


    @OnClick({R.id.btn_login, R.id.tv_forget, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if(TextUtils.isEmpty(mEtName.getText().toString())){
                    ToastUtils.showText("请输入手机号");
                    return;
                }

                if(mEtName.getText().toString().length() != 11){
                    ToastUtils.showText("请输入正确的手机号");
                    return;
                }

                if(TextUtils.isEmpty(mEtPwd.getText().toString())){
                    ToastUtils.showText("请输入密码");
                    return;
                }

                if(TextUtils.isEmpty(mEtPwd.getText().toString())){
                    ToastUtils.showText("请输入正确的密码");
                    return;
                }

                DataManager.setMobile(mEtName.getText().toString());
                DataManager.setPwd(mEtPwd.getText().toString());
                mLoginViewModel.login(mEtName.getText().toString(), mEtPwd.getText().toString());
                break;
            case R.id.tv_forget:
                ToastUtils.showText("forget pwd");
                break;
            case R.id.btn_register:
                RegisterActivity.go(this);
                break;
        }
    }

}
