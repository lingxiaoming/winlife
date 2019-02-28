package com.hyd.winlife.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 用户信息
 */
public class User implements Serializable {
    private static final long serialVersionUID = 5727947273484909537L;

    public String username;//用户名，手机号
    public String realityName;//真实名字
    public String idNumber;//身份证
    public String headImg;//头像
    public int user_type;//用户类型（0个人 1企业）
    public int user_role;//用户角色（0 未确定 1 借款会员 2 出借会员 3 复合会员）
    public boolean isAddBaseInfo;//是否完善自己资料
    public double accountAmount;//账户余额（可用+冻结）
    public double availableBalance;//可用余额
    public int creditScore;//信用积分
    public double receive_corpus;//待收本金
    public double receive_interest;//待收利息
    public String ipsAcctNo;//汇付账户（空表示未开通）
    public int auditmaticStatu;//自动投标状态（0关闭 1开启）
    public int auditmaticId;//自动投标参数id
    public boolean hasEmail;//是否完成邮箱验证
    public boolean hasMobile;//是否完成手机验证
    public boolean isShareholder;//用户是否是股东
    public String busi_code;//营业执照号码
    public String company;//企业名称
    public String questionnaire;//用户风险测评类型描述
    public Long marketCampaignId;//	用户关联活动id
    public String marketCampaignName;//	用户关联活动名称，当活动失效、停用时，不返回该字段
    public double score;//华赢币数量
    public boolean hasUnreadMessages;//是否有未读消息
    public String gesturePassword;//手势密码
    public boolean payScoreIsNotNeedOpenAccount;//用户华赢币支付是否不需要开户 true:不需要 false:需要
    public String userQrcode;//用户邀请二维码图片地址


    public String getPhoneNum() {
        if (TextUtils.isEmpty(username)) {
            return "";
        } else {
            return username;
        }
    }

    public String getIpsAcctNo() {
        if (TextUtils.isEmpty(ipsAcctNo)) {
            return "";
        } else {
            return ipsAcctNo;
        }
    }

    public String getRealityName() {
        if (user_type == 0) {//个人
            if (TextUtils.isEmpty(realityName)) {
                return "";
            } else {
                return realityName;
            }
        } else {//企业
            if (TextUtils.isEmpty(company)) {
                return "";
            } else {
                return company;
            }
        }
    }

    public String getIdNumber() {
        if (user_type == 0) {//个人
            if (TextUtils.isEmpty(idNumber)) {
                return "";
            } else {
                return idNumber;
            }
        } else {
            if (TextUtils.isEmpty(busi_code)) {
                return "";
            } else {
                return busi_code;
            }
        }

    }
}
