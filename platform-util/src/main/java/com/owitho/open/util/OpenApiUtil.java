package com.owitho.open.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.owitho.open.model.RequestModel;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.util.validate.function.ValidateHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Random;

/**
 * @author young
 * @date 2018/5/22
 */
public class OpenApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiUtil.class);

    private static final Random RANDOM = new Random();

    private static final String CHARSET_NAME = "UTF-8";

    private static final long DEFAULT_UTC_TIMEOUT = 3 * 1000;

    /**
     * 调用远程接口
     * 返回序列化的data对象
     *
     * @param appId
     * @param url
     * @param accessToken
     * @param data
     * @param <T>
     * @param <R>
     * @return
     * @throws Exception
     */
    public static <T, R> R remoteInvokeReturnData(String appId, String url, String accessToken, T data) throws Exception {
        ResponseModel<R> response = remoteInvoke(appId, url, accessToken, data);
        if (!response.hasSuccess()) {
            logger.error("远程调用返回失败！response:{}", response);
            throw new RuntimeException("远程调用返回失败！");
        }
        return response.getData();
    }

    /**
     * 调用远程接口
     * 自动生成签名，填充请求体，发起HttpPost请求
     * 返回结果序列化成 ResponseModel<R>
     *
     * @param appId
     * @param data
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> ResponseModel<R> remoteInvoke(String appId, String url, String accessToken, T data) throws Exception {
        if (Objects.isNull(data)) {
            logger.error("data null!");
            throw new RuntimeException("data null!");
        }
        RequestModel request = signature(appId, accessToken, data);
        String result = HttpClientUtil.postHttpsRequest(url, JsonHelper.transObjToJsonString(request), CHARSET_NAME);
        ResponseModel<R> response = JsonHelper.transJsonStringToResp(result, new TypeReference<ResponseModel<R>>() {
        });

        return response;
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
        int salt = RANDOM.nextInt(9999 - 1000 + 1) + 1000;
        long utc = System.currentTimeMillis();
        //生成签名
        String signature = generateSignature(appId, accessToken, dataJson, salt, utc);

        RequestModel request = new RequestModel(appId, salt, signature, utc, dataJson);
        return request;
    }

    /**
     * 校验请求签名和utc时间
     * 返回data对应的clazz
     *
     * @param request
     * @param accessToken
     * @param <R>
     * @return
     */
    public static <R> R checkSignature(RequestModel request, String accessToken, Class<? extends R> clazz) {

        return checkSignature(request, accessToken, clazz, false, DEFAULT_UTC_TIMEOUT);
    }

    /**
     * 校验请求签名
     * 返回data对应的clazz
     *
     * @param request
     * @param accessToken
     * @param clazz
     * @param checkUtc    是否校验utc
     * @param timeOut     utc超时时间（防止重放攻击）
     * @param <R>
     * @return
     */
    public static <R> R checkSignature(RequestModel request, String accessToken, Class<? extends R> clazz, boolean checkUtc, long timeOut) {
        ValidateHelper.validate(request);

        if (checkUtc) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - request.getUtc() > timeOut) {
                logger.error("utc超时！request:{}", request);
                throw new RuntimeException("utc超时！");
            }
        }

        String signature = generateSignature(request.getAppId(), accessToken, request.getData(), request.getSalt(), request.getUtc());
        if (!StringUtils.equals(signature, request.getSignature())) {
            logger.error("验签失败！request:{}", request);
            throw new RuntimeException("验签失败！");
        }
        return JsonHelper.transJsonStringToObj(request.getData(), clazz);
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
        try {
            return DigestUtils.md5DigestAsHex(beforeMd5.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding utf-8失败！beforeMd5:{}", beforeMd5);
            throw new RuntimeException("Encoding utf-8失败！");
        }
    }
}
