package com.owitho.open.model;

/**
 * @author young
 * @date 2018/5/22
 */
public class ApiResponse<E> {

    public static final String SUCCESS = "200";
    public static final String FAILURE = "500";

    private String code;
    private String msg;
    private E data;


    @Override
    public String toString() {
        return "ApiResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static String getFAILURE() {
        return FAILURE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public ApiResponse() {
    }

    public ApiResponse(String code, String msg, E data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <E> ApiResponse<E> success(String msg) {
        return new ApiResponse<E>("200", msg, null);
    }

    public static <E> ApiResponse<E> success(E resultList) {
        return new ApiResponse<E>("200", "success", resultList);
    }


    public static <E> ApiResponse<E> fail(String msg) {
        return new ApiResponse<E>("500", msg, null);
    }

    public static <E> ApiResponse<E> fail(E resultList) {
        return new ApiResponse<E>("500", "failed", resultList);
    }

    public static <E> ApiResponse<E> fail(String resultCode, String msg, E resultList) {
        return new ApiResponse<E>(resultCode, msg, resultList);
    }

    public static <E> ApiResponse<E> fail(String msg, E resultList) {
        return new ApiResponse<E>("500", msg, resultList);
    }
}
