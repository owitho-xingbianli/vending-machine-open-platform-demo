package com.owitho.open;

import com.owitho.open.model.RequestData;
import com.owitho.open.model.ResponseData;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.util.OpenApiUtil;
import junit.framework.TestCase;
import sun.security.provider.MD5;

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
    public static final String APPID = "3bbdeb4ffa90585a4e87a1aa34e5c644";

    public static final String URL = "https://open.owitho.intra.im";

    public void test() throws Exception {


    }

    public void testGetAccessToken() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", "bc568e9e-159f-40ce-a9c5-3fba2d3482e8");
        System.out.println(response);
    }

    public void testRemoteInvoke() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", "bc568e9e-159f-40ce-a9c5-3fba2d3482e8");
        ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL + "/testSign", response.getData().getAccessToken(), new RequestData("XBL001"), ResponseData.class);
        System.out.println(data);
    }
}
