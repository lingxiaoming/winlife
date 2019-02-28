//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hyd.winlife.tools;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {
    private static byte[] pwd = null;
    private static byte[] iv = "nmeug.f9/Om+L823".getBytes();
    private static final String UTF_8 = "UTF-8";
    private static boolean isencry = true;

    public AesHelper() {
    }

    public static String encryptNoPadding(String var0, String var1) throws Exception {
        Cipher var2 = Cipher.getInstance("AES/CBC/NoPadding");
        int var3 = var2.getBlockSize();
        byte[] var4 = var0.getBytes(var1);
        int var5 = var4.length;
        if(var5 % var3 != 0) {
            var5 += var3 - var5 % var3;
        }

        byte[] var6 = new byte[var5];
        System.arraycopy(var4, 0, var6, 0, var4.length);
        SecretKeySpec var7 = new SecretKeySpec(pwd, "AES");
        IvParameterSpec var8 = new IvParameterSpec(iv);
        var2.init(1, var7, var8);
        byte[] var9 = var2.doFinal(var6);
        return Base64.encodeBase64String(var9);
    }

    public static String decryptNoPadding(String var0, String var1) throws Exception {
        Cipher var2 = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec var3 = new SecretKeySpec(pwd, "AES");
        IvParameterSpec var4 = new IvParameterSpec(iv);
        var2.init(2, var3, var4);
        byte[] var5 = var2.doFinal(Base64.decodeBase64(var0));
        return new String(var5, var1);
    }

    public static void setPassword(String var0) {
        if(!TextUtils.isEmpty(var0)) {
            String var1 = md5(var0);
            if(var1.length() >= 16) {
                var1 = var1.substring(0, 16);
            }

            pwd = var1.getBytes();
        }

    }

    public static byte[] getBytesUtf8(String var0) {
        return getBytesUnchecked(var0, "UTF-8");
    }

    public static byte[] getBytesUnchecked(String var0, String var1) {
        if(var0 == null) {
            return null;
        } else {
            try {
                return var0.getBytes(var1);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(var1, var3);
            }
        }
    }

    private static IllegalStateException newIllegalStateException(String var0, UnsupportedEncodingException var1) {
        return new IllegalStateException(var0 + ": " + var1);
    }

    public static String newString(byte[] var0, String var1) {
        if(var0 == null) {
            return null;
        } else {
            try {
                return new String(var0, var1);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(var1, var3);
            }
        }
    }

    public static String newStringUtf8(byte[] var0) {
        return newString(var0, "UTF-8");
    }

    public static String md5(String var0) {
        if(var0 == null) {
            return null;
        } else {
            try {
                byte[] var1 = var0.getBytes();
                MessageDigest var2 = MessageDigest.getInstance("MD5");
                var2.reset();
                var2.update(var1);
                byte[] var3 = var2.digest();
                StringBuffer var4 = new StringBuffer();

                for(int var5 = 0; var5 < var3.length; ++var5) {
                    var4.append(String.format("%02X", new Object[]{Byte.valueOf(var3[var5])}));
                }

                return var4.toString();
            } catch (Exception var6) {
                return var0.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
            }
        }
    }
}
