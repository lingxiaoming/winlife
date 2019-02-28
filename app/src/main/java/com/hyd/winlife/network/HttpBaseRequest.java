package com.hyd.winlife.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hyd.winlife.tools.LogUtils;
import com.hyd.winlife.tools.SignUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 网络请求基类，用于初始化一些必要的东西
 * Created by lingxiaoming on 2017/7/26 0026.
 */

public class HttpBaseRequest {
    private static final String TAG = "HttpBaseRequest";

    protected HttpInterface mHttpInterface;
    protected Retrofit mRetrofit;

    protected static final int TIMEOUT = 50;//20秒超时时间
    protected static final long RETRY_TIMES = 1;//重订阅次数

    protected Gson gson = new Gson();

    public HttpBaseRequest() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        //TODO true异常情况 重新请求接口; false 避免具有破坏性的重复请求
        //具体情况酌情true或false
        //1、Unreachable IP addresses；2、Stale pooled connections；3、Unreachable proxy servers(无法访问的代理服务器)；
        builder.retryOnConnectionFailure(true);
//        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(new BaseInterceptor());
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(ApiConstants.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE));
//        builder.addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY));
        //降级Https 不需要证书
        OkHttpClient okHttpClient = builder.protocols(Collections.singletonList(Protocol.HTTP_1_1)).build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(new CustomGsonConverterfactory(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mHttpInterface = mRetrofit.create(HttpInterface.class);
    }

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            try {
                StringReader reader = new StringReader(message);
                Properties properties = new Properties();
                properties.load(reader);
                properties.list(System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });


    public class CustomGsonConverterfactory extends Converter.Factory {
        private Gson gson;

        public CustomGsonConverterfactory(Gson gson) {
            this.gson = gson;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new CustomGsonResponseBodyConverter<>(gson, adapter);
        }
    }

    final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {//解析返回的结果
            String response = value.string();
            BaseResponse httpStatus = gson.fromJson(response, BaseResponse.class);
            if (!httpStatus.isSuccess()) {
                value.close();
//                if ("请登录账户".equals(httpStatus.rtn_msg)) {
//                    return adapter.fromJson("0");
//                } else {
//                    throw new ApiException(httpStatus.rtn_code, httpStatus.rtn_msg);
//                }
                throw new ApiException(httpStatus.error, httpStatus.msg);
            } else {
                try {
                    JSONObject json = new JSONObject(response);
                    HashMap<String, Object> map = SignUtils.jsonObjectToMap(json);
                    String sign = (String) map.get("sign");
                    map.remove("sign");
                    if(TextUtils.isEmpty(sign)){
                        throw new ApiException(ApiException.ERROR_CODE_SIGN, "签名为空");
                    }else {
                        String checkSign = SignUtils.createSign(map);
                        boolean checkSuccess = verifyRsaSign(checkSign, sign, getPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmVfnZZsoG+E8mqEXKHGWDjI7hr1prX/kF8gIQAjMkBC3DL3dXo3/hRne8rh17XFDveo4pas3audWwTxvD1k5jsTDfvkd63Uaxj7z0DGmE6QfOWJdlves1xpxt+32ExAnzcgS9u6HsRMGvWRkZYMaWaYJZkjN9otKo9C7T6Z/YV4QbvwVD9X3/F65bvdU85hZpUcSKPAzrmPSsWLGPE8gJBIzE5eiJAtdUoYPApJDT9SSOZ6aGZnc60xhaC+sXAocalsRLhKB6Fm3LnUMpveU4iJYBNKly6yKo+h60ota/ICUxXgZnbNEWyFkD2zk+uMZrkWrrTT/N09zErr5+YabMQIDAQAB"));
                        if(checkSuccess){
                            map.put("sign", checkSign);
                        }else {
                            throw new ApiException(ApiException.ERROR_CODE_SIGN, "RSA验签失败");
                        }
                    }

                    if (SignUtils.checkSign(map)) {//检查签名
//                        String body;
//                        if (httpStatus.body == null || TextUtils.isEmpty(httpStatus.body.toString()) || TextUtils.equals("{}", httpStatus.body.toString())) {
//                            body = "0";
//                        } else {
//                            body = httpStatus.body.toString();
//                        }
                        return adapter.fromJson(response);
                    } else {
                        throw new ApiException(ApiException.ERROR_CODE_SIGN, "签名不可信");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e(TAG, e.getMessage());
                    throw new ApiException(ApiException.ERROR_CODE_OTHER, "数据格式有误");
                } finally {
                    value.close();
                }
            }
        }
    }


    private Map<String, Object> getCommonMap() {//添加公共参数 TODO 测试扫码支付
        HashMap params = new HashMap<String, Object>();
//        params.put(ParamContants.ACCESS_TOKEN, DataManager.getToken());
//        params.put(ParamContants.NONCE, SignUtils.getRandomString(32));
//        params.put(ParamContants.TIMESTAMP, System.currentTimeMillis() + "");
//        params.put(ParamContants.VERSION, AppVersionUtils.getVersionCode() + "");
//        params.put(ParamContants.DEVICE_ID, AppVersionUtils.getVersionCode() + "");
//        params.put(ParamContants.SIGN_TYPE, ApiConstants.SIGN_TYPE);
//        params.put(ParamContants.DEVICE_TYPE, ApiConstants.DEVICE_TYPE);

        params.put("client_id", "tiantiancai11cvqw");
        params.put("format", "JSON");
        params.put("version", "1.0");
        params.put("timestamp", System.currentTimeMillis() + "");
        return params;
    }

    class BaseInterceptor implements Interceptor {//补齐公共参数并签名

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Map<String, Object> requestMap = getCommonMap();
//            requestMap.put(ParamContants.OPT, original.url().queryParameter(ParamContants.OPT));
//            requestMap.put(ParamContants.BODY, ((FormBody) original.body()).value(0));

            FormBody body = (FormBody) original.body();
            for (int i = 0; i < body.size(); i++) {
                requestMap.put(body.name(i), body.value(i));
            }

            //添加请求参数
            HttpUrl.Builder builder = original.url().newBuilder();
            Set<String> set = requestMap.keySet();
            FormBody.Builder newFormBody = new FormBody.Builder();

            //请求体定制：统一添加token参数
            if (original.body() instanceof FormBody) {
                for (String mapKey : set) {
                    newFormBody.add(mapKey, requestMap.get(mapKey).toString());
                }

                try {
                    byte[] tByte;
                    Signature signature = Signature.getInstance("SHA1WithRSA");
                    PrivateKey privateKey = getPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCZV+dlmygb4TyaoRcocZYOMjuGvWmtf+QXyAhACMyQELcMvd1ejf+FGd7yuHXtcUO96jilqzdq51bBPG8PWTmOxMN++R3rdRrGPvPQMaYTpB85Yl2W96zXGnG37fYTECfNyBL27oexEwa9ZGRlgxpZpglmSM32i0qj0LtPpn9hXhBu/BUP1ff8Xrlu91TzmFmlRxIo8DOuY9KxYsY8TyAkEjMTl6IkC11Shg8CkkNP1JI5npoZmdzrTGFoL6xcChxqWxEuEoHoWbcudQym95TiIlgE0qXLrIqj6HrSi1r8gJTFeBmds0RbIWQPbOT64xmuRautNP83T3MSuvn5hpsxAgMBAAECggEAUjdBR8bysFAJMFbilbqg26evJsTzniSN5PpJAhw1oaB0pI4/3GyB7Zk7YiPohpBN48tq59hu9BBpAV4npbEPHS7+Klvi4NV+r4AdNSkJhEpgu2G0EDdnIkV5Z0Zcw0fML5bwJBksA5LR8EWdVpf+TsapD4BFfcSdFkvZwhDhBxCU8ouaRKfHnxqxO+3GfiQTtZAG5gxhxP5sxTdzllnN31JGdOkbJMXzTsKivsO8mAqK2fSl4kCMHRF+xOl38QvmQa3nOzOj5hQ8hy/BTZJ/B4zrd3JCJYsIAb9pzIP0d65Rlqs4OhvHeTS9p5aocp6+esLCQ3Pa/aH2BoQGQz9+gQKBgQDVNqJuA5rdTLwJNI/ReXfIDrxsULMvthHL9tOdSEPdRAwE6yiN2SkC1ZBMNI7L4ILOF6FmhOTyRgfZv2OrwwmhosXnMc1+GmgVp9TthiQQEA1w4ZviTAydcypiK/aF/lkWWw6XKvTbgDdcpm7/oTfMfzgY0mAGEHfBxIc8g9p+yQKBgQC4HZQ8sPi7FBngr+m/Rw0hxZu1Dm7+6xpdx3twe/85TujHA9HiuPJLLurhQ0OKWYq32H75GbVwQD+8uXMvZXcJCmAiELTejFRCKX/Xzrgt1Kk+eWO38fDuP5hCJle+IiOjRf43RCF3lFDHONE/7fraD501Uj3LbmixOqlwnetlKQKBgDMbRnHyYUOcahIWzEZcxE5q4dVgvqK4FKn8PxsbQFEgR3VCXNUrcX4hbNYnXvcVmKUH2wM05aABE06pZp28QWnCF4fkUypf8AUI1qwclDZZcq9VFMV4jymKemVPvma94eLO7r9jZ4/Zuut1ZXPW1AYrlGm2u5eYiOwkTHo/7LapAoGADpjpw9YUImD5nELWOYAcnmzYZ4Z5bmSddon2U3jBR33mHsFCxsL7EtLpdGqs1C89PE5B4uYS6I9vfjsF0uAsi97yuECy1mIx2/0GUcnl+Fw//uAI8/COtZT9MWFw23ZBdRR7j04riRuNO/5ZOMRZeyh5wlx+4b4jNsn73R3oJ+ECgYAgOwWT6J/uAmrEQCAbgEBtL2V+w43edVmSLUa+hHUWdUh42/v/dwXMmN6dBHvSypEUJ2RpgfRR8Bu4mNYrGzDBp64LwtQfUTvKI3QpoXLWqDJZoZYB8oiCGDIpFOsh8iYu+8NZOX3JzlCje1eNE+KFP+unOflAMXAXST20lzv0/w==");
                    signature.initSign(privateKey);
                    String sign = SignUtils.createSign(requestMap);
                    signature.update(sign.getBytes("UTF-8"));
                    tByte = signature.sign();
                    String result = new String(Base64.encodeBase64(tByte));
                    LogUtils.d(TAG, "SIGN = " + result);
                    newFormBody.add(ParamContants.SIGN, result);
                } catch (Exception e) {

                }

            }

            HttpUrl url = builder.build();

            Request request = original.newBuilder()
                    .method(original.method(), newFormBody.build())
                    .url(url)
                    .build();
            return chain.proceed(request);
        }
    }


    public <T> T getProxy(Class<T> tClass) {//拿到代理类
        T t = mRetrofit.create(tClass);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[]{tClass}, new ProxyHandler(t, this));
    }

    /**
     * 创建私钥
     *
     * @param private_key
     * @return
     */
    private static PrivateKey getPrivateKey(String private_key) {
        KeyFactory kf;
        PrivateKey privateKey = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(private_key.getBytes()));
            privateKey = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            LogUtils.e(TAG, "密钥初始化失败:" + e.getMessage());
        }
        return privateKey;
    }


    /**
     * 获取公钥
     *
     * @param public_key
     * @return
     */
    private static PublicKey getPublicKey(String public_key) {
        KeyFactory kf;
        PublicKey publickey = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(public_key.getBytes()));
            publickey = kf.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "密钥初始化失败");
        }
        return publickey;
    }

    /**
     * 非对称验证sign
     *
     * @param inputStr
     * @param sign
     * @return
     */
    public static boolean verifyRsaSign(String inputStr, String sign, PublicKey publicKey) {

        boolean verifyResult = false;
        if (TextUtils.isEmpty(sign)) {
            LogUtils.d(TAG, "sign 为空验证签名失败！");
            return verifyResult;
        }
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(inputStr.getBytes("UTF-8"));
            verifyResult = signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "密钥初始化失败" + e.getMessage());
        }
        return verifyResult;
    }

}
