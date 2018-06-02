package com.owitho.open.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.owitho.open.model.RequestModel;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.model.TokenRequest;
import com.owitho.open.util.validate.function.ValidateHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

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
    public static <T, R> R remoteInvokeReturnData(String appId, String url, String accessToken, T data, Class<? extends R> clazz) throws Exception {
        ResponseModel<R> response = remoteInvoke(appId, url, accessToken, data, clazz);

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
    public static <T, R> ResponseModel<R> remoteInvoke(String appId, String url, String accessToken, T data, Class<? extends R> clazz) throws Exception {
        ResponseModel response = remoteInvoke(appId, url, accessToken, data);
        if (!response.hasSuccess()) {
            logger.error("远程调用返回失败！response:{}", response);
            throw new RuntimeException("远程调用返回失败！");
        }
        LinkedHashMap dataMap = (LinkedHashMap) response.getData();
        response.setData(JsonHelper.transJsonStringToObj(JsonHelper.transObjToJsonString(dataMap), clazz));

        return response;
    }

    /**
     * 调用远程接口（用于data为空的返回）
     * 自动生成签名，填充请求体，发起HttpPost请求
     * 返回HttpResponse的result
     *
     * @param appId
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseModel remoteInvoke(String appId, String url, String accessToken, T data) throws Exception {
        return remoteInvoke(appId, url, Lists.newArrayList(accessToken), data);
    }

    /**
     * 调用远程接口(多个可用token)
     *
     * @param appId
     * @param url
     * @param accessTokens
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ResponseModel remoteInvoke(String appId, String url, List<String> accessTokens, T data) throws Exception {
        if (data == null) {
            logger.error("data null!");
            throw new RuntimeException("data null!");
        }
        RequestModel request = signature(appId, accessTokens, data);
        String result = HttpClientUtil.postHttpsRequest(url, JsonHelper.transObjToJsonString(request), CHARSET_NAME);
        ResponseModel response = JsonHelper.transJsonStringToObj(result, ResponseModel.class);
        return response;
    }

    /**
     * 生成md5签名，返回请求体
     *
     * @param appId
     * @param accessTokens
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RequestModel signature(String appId, List<String> accessTokens, T data) {
        String dataJson = JsonHelper.transObjToJsonString(data);
        int salt = RANDOM.nextInt(9999 - 1000 + 1) + 1000;
        long utc = System.currentTimeMillis();
        //生成签名

        List<String> signatures = generateSignature(appId, accessTokens, dataJson, salt, utc);

        RequestModel request = new RequestModel(appId, salt, signatures, utc, dataJson);
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
     * 校验请求签名(如果没有有效的accessToken，使用accessToken = secretKey)
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
        Boolean isSuccess = checkSignature(request, Lists.newArrayList(accessToken), checkUtc, timeOut);
        if (!isSuccess) {
            logger.error("验签失败！request:{}", request);
            throw new RuntimeException("验签失败！");
        }

        return JsonHelper.transJsonStringToObj(request.getData(), clazz);
    }

    public static boolean checkSignature(RequestModel request, List<String> accessTokens, boolean checkUtc, long timeOut) {
        ValidateHelper.validate(request);
        if (checkUtc) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - request.getUtc() > timeOut) {
                logger.error("utc超时！request:{}", request);
                throw new RuntimeException("utc超时！");
            }
        }
        List<String> signatures = generateSignature(request.getAppId(), accessTokens, request.getData(), request.getSalt(), request.getUtc());
        for (String signature : request.getSignatures()) {
            if (signatures.contains(signature)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> generateSignature(String appId, List<String> accessTokens, String data, int salt, long utc) {
        List<String> signatures = new ArrayList<>();
        for (String accessToken : accessTokens) {
            String signature = generateSignature(appId, accessToken, data, salt, utc);
            signatures.add(signature);
        }
        return signatures;
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
    public static String generateSignature(String appId, String accessToken, String data, int salt, long utc) {
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

    /**
     * 获取token
     *
     * @param appId
     * @param url
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static ResponseModel<TokenInfo> getAccessToken(String appId, String url, String secretKey) throws Exception {
        TokenRequest request = secretKeySignature(appId, secretKey);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", request.getAppId());
        params.put("salt", String.valueOf(request.getSalt()));
        params.put("utc", String.valueOf(request.getUtc()));
        params.put("signature", request.getSignature());
        String result = HttpClientUtil.getHttpsRequest(url, params, CHARSET_NAME);
        ResponseModel<TokenInfo> response = JsonHelper.transJsonStringToResp(result, new TypeReference<ResponseModel<TokenInfo>>() {
        });
        return response;
    }

    /**
     * 拼接secretKey生成md5签名，返回请求体
     * 用于获取请求token请求
     *
     * @param appId
     * @param secretKey
     * @return
     */
    public static TokenRequest secretKeySignature(String appId, String secretKey) {
        int salt = RANDOM.nextInt(9999 - 1000 + 1) + 1000;
        long utc = System.currentTimeMillis();
        //生成签名
        String signature = generateSecretKeySignature(appId, salt, utc, secretKey);

        TokenRequest request = new TokenRequest(appId, salt, signature, utc);
        return request;
    }


    /**
     * 拼接secretKey生成md5签名，encode失败则返回null
     *
     * @param appId
     * @param salt
     * @param utc
     * @param secretKey
     * @return
     */
    public static String generateSecretKeySignature(String appId, int salt, long utc, String secretKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=").append(appId).append("&salt=").append(salt).append("&utc=").append(utc).append("&secretKey=").append(secretKey);
        String beforeMd5 = sb.toString();
        try {
            return DigestUtils.md5DigestAsHex(beforeMd5.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding utf-8失败！beforeMd5:{}", beforeMd5);
            throw new RuntimeException("Encoding utf-8失败！");
        }
    }
}
