package com.owitho.open.model;

import com.owitho.open.util.validate.annotation.Max;
import com.owitho.open.util.validate.annotation.Min;
import com.owitho.open.util.validate.annotation.NotNull;

/**
 * @author young
 * @date 2018/5/23
 */
public class RequestModel {

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

    /**
     * 请求参数内容
     */
    @NotNull
    private String data;

    public RequestModel() {
    }

    public RequestModel(String appId, int salt, String signature, long utc, String data) {
        this.appId = appId;
        this.salt = salt;
        this.signature = signature;
        this.utc = utc;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "appId='" + appId + '\'' +
                ", salt=" + salt +
                ", signature='" + signature + '\'' +
                ", utc=" + utc +
                ", data='" + data + '\'' +
                '}';
    }
}
