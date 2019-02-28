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
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.manager.DataManager;
import com.hyd.winlife.tools.StatusBarUtils;
import com.hyd.winlife.tools.ToastUtils;
import com.hyd.winlife.viewmodel.RegisterViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * Created by lingxiaoming on 2018-10-29.
 */
public class RegisterActivity extends BaseActivity {
    private RegisterViewModel mRegisterViewModel;

    @BindView(R.id.et_name)
    public EditText mEtName;

    @BindView(R.id.et_code)
    public EditText mEtCode;

    @BindView(R.id.et_pwd)
    public EditText mEtPwd;

    @BindView(R.id.btn_register)
    public Button mBtnRegister;

    public static void go(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void mOnCreate() {
        StatusBarUtils.setStatusBarDarkTheme(this, true);
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mRegisterViewModel.registerResult.observe(this, mRegisterObserver);
        mRegisterViewModel.getCodeResult.observe(this, mGetCodeObserver);
        initDatas();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    private void initDatas() {
        mEtName.setText(DataManager.getMobile());
//        mEtPwd.setText(DataManager.getPwd());
    }


    private Observer<ModuleResult<LoginResult>> mRegisterObserver = (result -> {
//        Toast.makeText(getApplicationContext(), "data=" + result.data + " e=" + result.error, Toast.LENGTH_SHORT).show();

        if (result.data == null) {
            ToastUtils.showText("注册失败:" + result.error.getMessage());
        } else {
            LoginResult loginResult = result.data;
            ToastUtils.showText("用户id:" + loginResult.id);
            DataManager.setId(loginResult.id);
            MainActivity.go(this);
        }

    });

    private Observer<ModuleResult<BaseResult>> mGetCodeObserver = (result -> {
//        Toast.makeText(getApplicationContext(), "data=" + result.data + " e=" + result.error, Toast.LENGTH_SHORT).show();

        if (result.data == null) {
            ToastUtils.showText("获取验证码失败:" + result.error.getMessage());
        } else {
            BaseResult baseResult = result.data;
            ToastUtils.showText("获取验证码成功");
        }

    });


    @OnClick({R.id.btn_register, R.id.tv_get_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
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
                mRegisterViewModel.register(mEtName.getText().toString(), mEtPwd.getText().toString(), mEtCode.getText().toString());
                break;
            case R.id.tv_get_code:
                if(TextUtils.isEmpty(mEtName.getText().toString())){
                    ToastUtils.showText("请输入手机号");
                    return;
                }

                if(mEtName.getText().toString().length() != 11){
                    ToastUtils.showText("请输入正确的手机号");
                    return;
                }
                mRegisterViewModel.getCode(mEtName.getText().toString());
                break;
        }
    }

}
