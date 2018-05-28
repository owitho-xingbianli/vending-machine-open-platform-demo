package com.owitho.open;

import com.owitho.open.model.RequestData;
import com.owitho.open.model.ResponseData;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.util.OpenApiUtil;
import org.springframework.util.DigestUtils;

/**
 * @author young
 * @date 2018/5/25
 */
public class Demo {

    //测试appId
    public static final String APPID = "2922b949feae2e8f870414e9442b0611";

    //测试secretKey
    public static final String SECRETKEY = "a0fdb1a8-5202-4306-b818-67bbbcff1ba0";

    public static final String URL = "https://open.owitho.intra.im";

    public static void main(String[] args) {
        try {
            //获取token
            ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", SECRETKEY);
            System.out.println(response);

            //远程调用
            ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL + "/testSign", response.getData().getAccessToken(), new RequestData("XBL001"), ResponseData.class);
            System.out.println(data);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
