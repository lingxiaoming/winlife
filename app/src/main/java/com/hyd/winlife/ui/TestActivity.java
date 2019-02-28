package com.hyd.winlife.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyd.winlife.R;
import com.hyd.winlife.base.BaseActivity;
import com.hyd.winlife.bean.TestPayResult;
import com.hyd.winlife.network.HttpRequest;
import com.hyd.winlife.tools.LogUtils;
import com.hyd.winlife.tools.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 测试后台扫码支付接口
 * Created by lingxiaoming on 2019-01-03.
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.et_code)
    public EditText mEtQRCode;//二维码

    @BindView(R.id.et_order_num)
    public EditText mEtOrderNum;//商户订单号，支付查询用

    @BindView(R.id.et_trade_num)
    public EditText mEtTradeNum;//退款

    @BindView(R.id.et_trade_num_search)
    public EditText mEtTradeNumSearch;//退款查询

    @Override
    public void mOnCreate() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.initiateScan();
    }

    @OnClick(R.id.btn_pay)
    public void onClickPay(View view) {
        if (TextUtils.isEmpty(mEtQRCode.getText().toString())) {
            scan();
        } else {
            pay(mEtQRCode.getText().toString());
        }
    }

    @OnClick(R.id.btn_search)
    public void onClickSearchPay(View view) {
//        search("ZEJBWaCcKckWygnPGE8GEfVmuGsWUQni");
//        if (TextUtils.isEmpty(mEtOrderNum.getText().toString())) {
//            ToastUtils.showText("请输入商户订单号");
//        } else {
            search(mEtOrderNum.getText().toString());
//        }
    }

    @OnClick(R.id.btn_refund)
    public void onClickRefund(View view) {
//        if (TextUtils.isEmpty(mEtTradeNum.getText().toString())) {
//            ToastUtils.showText("请输入杭品生活订单号");
//        } else {
            refund(mEtTradeNum.getText().toString());
//        }
    }

    @OnClick(R.id.btn_refund_search)
    public void onClickSearchRefund(View view) {
//        if (TextUtils.isEmpty(mEtTradeNum.getText().toString())) {
//            ToastUtils.showText("请输入杭品生活订单号");
//        } else {
            refundSearch(mEtTradeNumSearch.getText().toString());
//        }
    }

    //支付
    private void pay(String code) {
        Observable<TestPayResult> payResultObservable = HttpRequest.getInstance().pay(code);
        payResultObservable.subscribe(new Observer<TestPayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d("TestActivity", "pay onSubscribe");
            }

            @Override
            public void onNext(TestPayResult testPayResult) {
                LogUtils.d("TestActivity", "pay onNext result:" + testPayResult);
                if (testPayResult != null) {
                    ToastUtils.showText("支付状态："+testPayResult.getTradeStatus());
                    mEtOrderNum.setText(testPayResult.getOrderNum());
                    mEtTradeNum.setText(testPayResult.getTradeNum());
                    mEtTradeNumSearch.setText(testPayResult.getTradeNum());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("TestActivity", "pay onError result:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d("TestActivity", "pay onComplete");
            }
        });
    }

    //支付查询
    private void search(String orderNum) {
        Observable<TestPayResult> payResultObservable = HttpRequest.getInstance().searchPayResult(orderNum);
        payResultObservable.subscribe(new Observer<TestPayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d("TestActivity", "search onSubscribe");
            }

            @Override
            public void onNext(TestPayResult testPayResult) {
                LogUtils.d("TestActivity", "search onNext result:" + testPayResult);
                if (testPayResult != null) {
                    ToastUtils.showText("查询支付状态："+testPayResult.getTradeStatus());
                    mEtOrderNum.setText(testPayResult.getOrderNum());
                    mEtTradeNum.setText(testPayResult.getTradeNum());
                    mEtTradeNumSearch.setText(testPayResult.getTradeNum());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("TestActivity", "search onError result:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d("TestActivity", "search onComplete");
            }
        });
    }

    //退款
    private void refund(String trade_num) {
        Observable<TestPayResult> payResultObservable = HttpRequest.getInstance().refund(trade_num);
        payResultObservable.subscribe(new Observer<TestPayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d("TestActivity", "refund onSubscribe");
            }

            @Override
            public void onNext(TestPayResult testPayResult) {
                LogUtils.d("TestActivity", "refund onNext result:" + testPayResult);
                if (testPayResult != null) {
                    ToastUtils.showText("退款金额："+testPayResult.getRefundAmount()+" 状态："+testPayResult.getRefundStatus());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("TestActivity", "refund onError result:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d("TestActivity", "refund onComplete");
            }
        });
    }

    //退款查询
    private void refundSearch(String trade_num) {
        Observable<TestPayResult> payResultObservable = HttpRequest.getInstance().refundSearch(trade_num);
        payResultObservable.subscribe(new Observer<TestPayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d("TestActivity", "refund onSubscribe");
            }

            @Override
            public void onNext(TestPayResult testPayResult) {
                LogUtils.d("TestActivity", "refund onNext result:" + testPayResult);
                if (testPayResult != null) {
                    ToastUtils.showText("查询退款金额："+testPayResult.getRefundAmount()+" 状态："+testPayResult.getRefundStatus());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("TestActivity", "refund onError result:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d("TestActivity", "refund onComplete");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ToastUtils.showText("取消扫描");
            } else {
                ToastUtils.showText("扫描内容:" + result.getContents());
                mEtQRCode.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
