package com.owitho.open;

import com.fasterxml.jackson.core.type.TypeReference;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.util.JsonHelper;
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

    public static final String URL = "https://device-bind.owitho.intra.im/test/sign";

    public void test() throws Exception {
//        ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL, TOKEN, new RequestData("XBL001"));
//
//        System.out.println(data);

        ResponseModel responseModel = OpenApiUtil.remoteInvoke(APPID, URL, TOKEN, new RequestData("XBL001"));
        String response = JsonHelper.transObjToJsonString(responseModel);
        ResponseModel<ResponseData> responseModel1 = JsonHelper.transJsonStringToResp(response, new TypeReference<ResponseModel<ResponseData>>() {
        });
        ResponseData data = responseModel1.getData();
        System.out.println(data);

    }
}
