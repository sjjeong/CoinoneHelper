package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 9. 5..
 */

public class CoinoneUserInfo {
    @SerializedName("userInfo")
    public UserInfo userInfo;

    public class UserInfo {
        @SerializedName("feeRate")
        public FeeRate feeRate;
        @SerializedName("virtualAccountInfo")
        public VirtualAccountInfo virtualAccountInfo;
        @SerializedName("mobileInfo")
        public MobileInfo mobileInfo;
        @SerializedName("bankInfo")
        public BankInfo bankInfo;
        @SerializedName("emailInfo")
        public EmailInfo emailInfo;
        @SerializedName("securityLevel")
        int securityLevel;

        public class FeeRate {
            @SerializedName("btc")
            public Fee btcFee;
            @SerializedName("bch")
            public Fee bchFee;
            @SerializedName("eth")
            public Fee ethFee;
            @SerializedName("etc")
            public Fee etcFee;
            @SerializedName("xrp")
            public Fee xrpFee;
            @SerializedName("qutm")
            public Fee qtumFee;


            public class Fee {
                @SerializedName("maker")
                public float maker;
            }

        }


        public class VirtualAccountInfo {
            @SerializedName("depositor")
            public String depositor;
            @SerializedName("accountNumber")
            public String accountNumber;
            @SerializedName("bankName")
            public String bankName;
        }

        public class MobileInfo {
            @SerializedName("userName")
            public String userName;
            @SerializedName("phoneNumber")
            public String phoneNumber;
            @SerializedName("phoneCorp")
            public String phoneCorp;
            @SerializedName("isAuthenticated")
            public boolean isAuthenticated;
        }

        public class BankInfo {
            @SerializedName("depositor")
            public String depositor;
            @SerializedName("bankCode")
            public String bankCode;
            @SerializedName("accountNumber")
            public String accountNumber;
            @SerializedName("isAuthenticated")
            public boolean isAuthenticated;
        }

        public class EmailInfo {
            @SerializedName("email")
            public String email;
            @SerializedName("isAuthenticated")
            public boolean isAuthenticated;
        }
    }

}
