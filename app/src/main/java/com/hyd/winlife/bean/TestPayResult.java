package com.hyd.winlife.bean;

import android.text.TextUtils;

import com.hyd.winlife.network.HttpRequest;

/**
 * 测试后台支付api
 * Created by lingxiaoming on 2019-01-03.
 */
public class TestPayResult {
    public String trade_amount;//实际支付金额
    public String discount_amount;//优惠金额
    public String trade_status;//交易状态 0：初始状态1：成交2：处理中-1：取消交易-3：支付失败
    public String order_num;//商户订单号
    public String trade_num;//交易号
    public String settlement_amount;//结算金额
    public String is_settlement;//是否已结算
    public String pay_title;//订单标题

    /**
     * 以下退款的字段
     */
    public String refund_num;//退款订单号(同请求)
    public String refund_amount;//退款金额
    public String refund_status;//退款状态 0 失败   1成功


    public String getTradeNum() {
        if (TextUtils.isEmpty(trade_num)) {
            return "";
        } else {
            return HttpRequest.decrypt3DES(trade_num);
        }
    }

    public String getOrderNum() {
        if (TextUtils.isEmpty(order_num)) {
            return "";
        } else {
            return HttpRequest.decrypt3DES(order_num);
        }
    }

    public String getTradeStatus() {
        if (TextUtils.isEmpty(trade_status)) {
            return "";
        } else {
            return HttpRequest.decrypt3DES(trade_status);
        }
    }

    public String getRefundAmount() {
        if (TextUtils.isEmpty(refund_amount)) {
            return "";
        } else {
            return HttpRequest.decrypt3DES(refund_amount);
        }
    }

    public String getRefundStatus() {
        if (TextUtils.isEmpty(refund_status)) {
            return "";
        } else {
            return HttpRequest.decrypt3DES(refund_status);
        }
    }
}
