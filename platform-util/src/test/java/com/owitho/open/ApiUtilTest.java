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
    public static final String APPID = "2922b949feae2e8f870414e9442b0611";

    public static final String URL = "https://open.owitho.intra.im";

    public void test() throws Exception {


    }

    public void testGetAccessToken() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", "a0fdb1a8-5202-4306-b818-67bbbcff1ba0");
        System.out.println(response);
    }

    public void testRemoteInvoke() throws Exception {
        ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", "a0fdb1a8-5202-4306-b818-67bbbcff1ba0");
        ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL + "/testSign", response.getData().getAccessToken(), new RequestData("XBL001"), ResponseData.class);
        System.out.println(data);
    }
}
