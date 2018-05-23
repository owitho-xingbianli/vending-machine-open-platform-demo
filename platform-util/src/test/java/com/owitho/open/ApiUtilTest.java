package com.owitho.open;

import com.owitho.open.util.OpenApiUtil;
import junit.framework.TestCase;

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

    public static final String TOKEN = "f23fd74c-fe43-4e38-801b-3c5b9d87b7c1";

    public static final String URL = "https://awesome-crm.owitho.intra.im/test/sign";

    public void test() throws Exception {
        ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL, TOKEN, new RequestData("XBL001"));
    }
}
