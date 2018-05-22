package com.owitho.open.model;

/**
 * @author young
 * @date 2018/5/22
 */
public class ApiRequest<T> {

    /**
     * 第三方平台appId
     */
    private String appId;

    /**
     * 1000-9999内随机数
     */
    private int salt;

    /**
     * 签名
     */
    private String signature;

    /**
     * 请求时间
     */
    private long utc;

    /**
     * 请求参数内容
     */
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiRequest{" +
                "appId='" + appId + '\'' +
                ", salt=" + salt +
                ", signature='" + signature + '\'' +
                ", utc=" + utc +
                ", data=" + data +
                '}';
    }
}
