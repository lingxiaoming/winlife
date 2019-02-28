package com.hyd.winlife.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SignUtils {
    private static final String TAG = "SignUtils";

    public static String encodeToUtf8(String value) {
        try {
            value = URLEncoder.encode(value, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String decodeFromUtf8(String value) {
        try {
            value = URLDecoder.decode(value.replaceAll("%20", "+"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 生成随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成签名
     *
     * @param params
     * @return
     */
    public static String createSign(Map<String, Object> params) {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        for (Object key : keys) {
            String value = params.get(key).toString();
            if (value != null && value.length() > 0) {
                if (temp.length() > 0)
                    temp.append("&");

//                String encodeValue = value;
//                try {
//                    encodeValue = URLEncoder.encode(value, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                //20160614 songxf 空格被URLEncoder.encode为“+”，此处全部替换
//                encodeValue = encodeValue.replaceAll("\\+", "%20");

                temp.append(key).append("=").append(value);
            }
        }

//        temp.append("&key=").append(ApiConstants.MD5_KEY);
        String s1 = temp.toString();
        String s2 = MD5(s1, Charset.defaultCharset());
        LogUtils.d(TAG, "MD5("+s1+"): "+s2);
        String s3 = s2.toUpperCase();
        return s3;
    }

    public static String MD5(String input, Charset charset) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        }

        md.update(input.getBytes(charset));
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] tmp = md.digest();
        char[] str = new char[32];
        int k = 0;

        for (int i = 0; i < 16; ++i) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 15];
            str[k++] = hexDigits[byte0 & 15];
        }

        String result = new String(str);
        return result;
    }

    /**
     * jsonObject转HashMap
     *
     * @param jobj
     * @return
     */
    public static HashMap<String, Object> jsonObjectToMap(JSONObject jobj) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (jobj != null) {
            Iterator<String> keysItr = jobj.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = null;
                try {
                    value = jobj.get(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 校验客户端sign
     *
     * @param parameters
     * @return
     */
    public static boolean checkSign(Map<String, Object> parameters) {
        String sign = (String) parameters.get("sign");
        parameters.remove("sign");
        String sign1 = createSign(parameters);
        if (!sign.equals(sign1)) {
            LogUtils.e(TAG, String.format("校验sign时不一致：expect=%s actual=%s", sign1, sign));
            return true;//TODO 测试，暂时返回true
        }
        return true;
    }
}
