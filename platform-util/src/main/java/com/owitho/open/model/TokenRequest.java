package com.owitho.open.model;

import com.owitho.open.util.validate.annotation.Max;
import com.owitho.open.util.validate.annotation.Min;
import com.owitho.open.util.validate.annotation.NotNull;

/**
 * @author young
 * @date 2018/5/23
 */
public class TokenRequest {

    /**
     * 第三方平台appId
     */
    @NotNull
    private String appId;

    /**
     * 1000-9999内随机数
     */
    @NotNull
    @Max(9999)
    @Min(1000)
    private int salt;

    /**
     * 签名
     */
    @NotNull
    private String signature;

    /**
     * 请求时间
     */
    @NotNull
    private long utc;


    public TokenRequest() {
    }

    public TokenRequest(String appId, int salt, String signature, long utc) {
        this.appId = appId;
        this.salt = salt;
        this.signature = signature;
        this.utc = utc;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getUtc() {
        return utc;
    }

    public void setUtc(long utc) {
        this.utc = utc;
    }

    @Override
    public String toString() {
        return "TokenRequest{" +
                "appId='" + appId + '\'' +
                ", salt=" + salt +
                ", signature='" + signature + '\'' +
                ", utc=" + utc +
                '}';
    }
}
