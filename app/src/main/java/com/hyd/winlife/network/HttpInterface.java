package com.hyd.winlife.network;


import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.BaseResult;
import com.hyd.winlife.bean.LoginResult;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.TestPayResult;
import com.hyd.winlife.bean.Token;
import com.hyd.winlife.bean.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 网络请求接口定义
 * Created by lingxiaoming on 2017/7/21 0021.
 */

public interface HttpInterface {

    @FormUrlEncoded
    @POST(ApiConstants.GET_TOKEN)
    Observable<Token> getToken(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.GET_USER)
    Observable<User> getUser(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN)
    Observable<LoginResult> login(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.REGISTER)
    Observable<LoginResult> register(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.GET_CODE)
    Observable<BaseResult> getCode(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.GET_BANNER)
    Observable<AdvListWapper> getBanner(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST(ApiConstants.GET_MOUDLE)
    Observable<MoudleWapper> getMoudle(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST("/hangpin/pay/order")
    Observable<TestPayResult> pay(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST("/hangpin/pay/orderQuery")
    Observable<TestPayResult> orderQuery(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST("/hangpin/pay/refund")
    Observable<TestPayResult> refund(@FieldMap Map<String, String> parm);

    @FormUrlEncoded
    @POST("/hangpin/pay/refundQuery")
    Observable<TestPayResult> refundQuery(@FieldMap Map<String, String> parm);

}
