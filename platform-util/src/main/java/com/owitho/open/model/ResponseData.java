package com.owitho.open.model;

/**
 * @author young
 * @date 2018/5/23
 */
public class ResponseData {

    private String machineCode;

    private String machineInfo;

    public ResponseData() {
    }

    public ResponseData(String machineCode, String machineInfo) {
        this.machineCode = machineCode;
        this.machineInfo = machineInfo;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineInfo() {
        return machineInfo;
    }

    public void setMachineInfo(String machineInfo) {
        this.machineInfo = machineInfo;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "machineCode='" + machineCode + '\'' +
                ", machineInfo='" + machineInfo + '\'' +
                '}';
    }
}
