package com.owitho.open;

import com.owitho.open.model.RequestData;
import com.owitho.open.model.ResponseData;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.util.OpenApiUtil;
import junit.framework.TestCase;
import org.springframework.util.DigestUtils;
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
    public static final String APPID = "bluesky";

    public static final String SECRETKEY = "sss";

    public static final String URL = "https://open.owitho.intra.im";

    public void test() throws Exception {

        String before = "appId%3Dbluesky%26salt%3D1817%26utc%3D1527577804587%26secretKey%3Dsss";
        String after = DigestUtils.md5DigestAsHex(before.getBytes("UTF-8"));
        System.out.println(after);
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
