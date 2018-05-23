package com.owitho.open.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.owitho.open.model.RequestModel;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.util.validate.function.ValidateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author young
 * @date 2018/5/22
 */
public class OpenApiUtil {

    private static final Random random = new Random();

    private static final String charsetName = "UTF-8";

    public static <T, R> R remoteInvokeReturnData(String appId, String url, String accessToken, T data, Class<? extends R> clazz) throws Exception {

        ResponseModel<R> response = remoteInvoke(appId, url, accessToken, data, clazz);
        if (!response.hasSuccess()) {
//            throw new RemoteInvokeException;
        }
        return response.getData();
    }

    /**
     * 调用远程接口
     *
     * @param appId
     * @param data
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> ResponseModel<R> remoteInvoke(String appId, String url, String accessToken, T data, Class<? extends R> clazz) throws Exception {
        String result = remoteInvoke(appId, url, accessToken, data);
        //todo 序列化失败
        ResponseModel<R> response = JsonHelper.transJsonStringToResp(result, new TypeReference<ResponseModel<R>>() {
        });

        return response;
    }

    /**
     * 远程调用接口
     * @param appId 调用方
     * @param url
     * @param accessToken
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String remoteInvoke(String appId, String url, String accessToken, T data) throws Exception {
        RequestModel request = signature(appId, accessToken, data);
        return HttpClientUtil.postHttpsRequest(url, JsonHelper.transObjToJsonString(request), charsetName);
    }

    /**
     * 校验请求签名和utc时间
     *
     * @param request
     * @param accessToken
     * @param allowDelay  允许收到请求的延迟时间（防止重放攻击）
     * @param <R>
     * @return
     */
    public static <R> R checkSignatureAndUtc(RequestModel request, String accessToken, long allowDelay, Class<? extends R> clazz) {
        //todo 系统时间误差
        long currentTime = System.currentTimeMillis();
        if (currentTime - request.getUtc() > allowDelay) {
//            throw new UtcTimeOutException;
        }
        return checkSignature(request, accessToken, clazz);
    }

    /**
     * 校验请求签名,返回data对应的clazz
     *
     * @param request
     * @param accessToken
     * @param clazz
     * @param <R>
     * @return
     */
    public static <R> R checkSignature(RequestModel request, String accessToken, Class<? extends R> clazz) {
        ValidateHelper.validate(request);

        String signature = generateSignature(request.getAppId(), accessToken, request.getData(), request.getSalt(), request.getUtc());

        if (!StringUtils.equals(signature, request.getSignature())) {
//            throw new CheckSignatureException;
        }
        return JsonHelper.transJsonStringToObj(request.getData(), clazz);
    }

    /**
     * 生成md5签名，返回请求体
     *
     * @param appId
     * @param accessToken
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RequestModel signature(String appId, String accessToken, T data) {
        String dataJson = JsonHelper.transObjToJsonString(data);
        int salt = random.nextInt(9999 - 1000 + 1) + 1000;
        long utc = System.currentTimeMillis();
        //生成签名
        String signature = generateSignature(appId, accessToken, dataJson, salt, utc);

        RequestModel request = new RequestModel(appId, salt, signature, utc, dataJson);
        return request;
    }

    /**
     * 生成md5签名，encode失败则返回null
     *
     * @param appId
     * @param accessToken
     * @param data
     * @param salt
     * @param utc
     * @return
     */
    private static String generateSignature(String appId, String accessToken, String data, int salt, long utc) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=").append(appId).append("&accessToken=").append(accessToken).append("&data=").append(data).append("&salt=").append(salt).append("&utc=").append(utc);
        String beforeMd5 = sb.toString();
        String afterMd5 = null;
        try {
            afterMd5 = DigestUtils.md5DigestAsHex(beforeMd5.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            //todo
        }
        return afterMd5;
    }
}
