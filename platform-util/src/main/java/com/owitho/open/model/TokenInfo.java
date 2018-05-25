package com.owitho.open.model;

/**
 * @author young
 * @date 2018/5/25
 */
public class TokenInfo {

    private String accessToken;

    private long expireTime;

    public TokenInfo() {
    }

    public TokenInfo(String accessToken, long expireTime) {
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
