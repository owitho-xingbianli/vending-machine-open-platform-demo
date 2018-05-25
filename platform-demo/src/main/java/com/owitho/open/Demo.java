package com.owitho.open;

import com.owitho.open.model.RequestData;
import com.owitho.open.model.ResponseData;
import com.owitho.open.model.ResponseModel;
import com.owitho.open.model.TokenInfo;
import com.owitho.open.util.OpenApiUtil;

/**
 * @author young
 * @date 2018/5/25
 */
public class Demo {

    public static final String APPID = "3bbdeb4ffa90585a4e87a1aa34e5c644";

    public static final String URL = "https://open.owitho.intra.im";

    public static void main(String[] args) {
        try {
            //获取token
            ResponseModel<TokenInfo> response = OpenApiUtil.getAccessToken(APPID, URL + "/getAccessToken", "bc568e9e-159f-40ce-a9c5-3fba2d3482e8");
            System.out.println(response);

            //远程调用
            ResponseData data = OpenApiUtil.remoteInvokeReturnData(APPID, URL + "/testSign", response.getData().getAccessToken(), new RequestData("XBL001"), ResponseData.class);
            System.out.println(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
