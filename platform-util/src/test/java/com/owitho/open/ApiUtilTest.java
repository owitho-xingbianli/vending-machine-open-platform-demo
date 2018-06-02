package com.owitho.open;

import com.google.common.collect.Lists;
import com.owitho.open.model.RequestData;
import com.owitho.open.model.ResponseData;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.util.OpenApiUtil;
import junit.framework.TestCase;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

import java.util.List;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class ApiUtilTest
        extends TestCase {
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public ApiUtilTest(String testName) {
//        super(testName);
//    }
//
//    /**
//     * @return the suite of tests being tested
//     */
//    public static Test suite() {
//        return new TestSuite(ApiUtilTest.class);
//    }

    /**
     * Rigourous Test :-)
     */
    public static final String APPID = "annzu";

    public static final String SECRETKEY = "huyu";

    public static final String URL = "https://open.owitho.com";

    public void test() throws Exception {

//        String before = "appId=annzu&accessToken=27dbb0d5-1ec7-4ec3-9c25-1e867b7c5a19&data={\"machineCode\": \"99000055\"}&salt=3264&utc=1527670432524";
//        String after = DigestUtils.md5DigestAsHex(before.getBytes("UTF-8"));
//        System.out.println(after);

        List<String> sign = OpenApiUtil.generateSignature("annzu", Lists.newArrayList("27dbb0d5-1ec7-4ec3-9c25-1e867b7c5a19"),"{\"machineCode\":\"99000055\"}",4103,1527676932653l);
        System.out.println(sign);
    }

    public void testGetAccessToken() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", SECRETKEY);
        System.out.println(response);
    }

    public void testRemoteInvoke() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", SECRETKEY);
        ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL + "/testSign", response.getData().getAccessToken(), new RequestData("XBL001"), ResponseData.class);
        System.out.println(data);
    }

}
