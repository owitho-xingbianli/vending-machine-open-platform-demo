package com.owitho.open.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author young
 * @date 2018/5/22
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final long DEFAULT_CONNECT_TIMEOUT = 3 * 1000;

    public static String postHttpsRequest(String url, String message, String charset) throws Exception {
        return postHttpsRequest(url, message, charset, DEFAULT_CONNECT_TIMEOUT);
    }

    /**
     * http post请求
     *
     * @param url
     * @param message
     * @param charset
     * @param connectTimeout
     * @return
     * @throws Exception
     */
    public static String postHttpsRequest(String url, String message, String charset, long connectTimeout) throws Exception {
        HttpClient httpClient;
        HttpPost httpPost;
        httpClient = wrapClient(connectTimeout);

        StringEntity se = new StringEntity(message, charset);
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));

        httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(se);

        HttpResponse response = httpClient.execute(httpPost);
        if (response != null && response.getEntity() != null) {
            String result = EntityUtils.toString(response.getEntity(), charset);
            return result;
        } else {
            logger.error("response null!httpPost:{}", httpPost.toString());
            throw new RuntimeException("response null!");
        }
    }

    /**
     * 发送get请求
     *
     * @param url     链接地址
     * @param params  参数
     * @param charset 字符编码，若为null则默认utf-8
     * @return
     */
    public static String getHttpsRequest(String url, Map<String, String> params, String charset, long connectTimeout) throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new InvalidParameterException("参数不能为空");
        }
        if (null == charset) {
            charset = "utf-8";
        }

        StringBuilder strParams = new StringBuilder("");
        for (Map.Entry<String, String> stringEntry : params.entrySet()) {
            strParams.append(stringEntry.getKey() + "=" + stringEntry.getValue());
            strParams.append("&");
        }
        String urlParams = strParams.replace(0, strParams.length() - 1, strParams.toString()).toString();

        if (url.contains("?")) {
            if (url.lastIndexOf("?") == url.length() - 1) {
                url += urlParams;
            }
        } else {
            url = url + "?" + urlParams;
        }

        HttpClient httpClient;
        HttpGet httpGet;
        String result = null;

        httpClient = wrapClient(connectTimeout);
        httpGet = new HttpGet(url);

        HttpResponse response = httpClient.execute(httpGet);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }

    /**
     * @return HttpClient
     * @Description 创建一个不进行正式验证的请求客户端对象 不用导入SSL证书
     */
    private static HttpClient wrapClient(long connectTimeout) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).setConnectionTimeToLive(connectTimeout, TimeUnit.MILLISECONDS).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }
}
