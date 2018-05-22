package com.owitho.open.util;

import com.owitho.open.model.ApiRequest;
import com.owitho.open.model.ApiResponse;

/**
 * @author young
 * @date 2018/5/22
 */
public class OpenApiUtil {

//    @CheckSignature
//    public static <T> ApiResponse<Void> businessInterface(ApiRequest<T> request) {
//        return null;
//    }

    /**
     * 校验请求签名
     *
     * @param request
     * @param <T>
     * @return
     */
    public static <T> boolean checkSignature(ApiRequest<T> request) {
        return true;
    }

    /**
     * 调用第三方接口
     *
     * @param appId
     * @param data
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> ApiResponse<R> invokeThird(String appId, T data, Class<? extends R> clazz) {
        return null;
    }

    public static <T> ApiRequest<T> generateSignature(String appId, T data) {
        return null;
    }

    private static String generateSignature(String appId, String data, int salt, long utc) {
        return "";
    }
}
