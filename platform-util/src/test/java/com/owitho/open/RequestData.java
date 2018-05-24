package com.owitho.open;

/**
 * @author young
 * @date 2018/5/23
 */
public class RequestData {

    private String machineCode;

    public RequestData() {
    }

    public RequestData(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}
