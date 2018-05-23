package com.owitho.open.model;

import java.util.Objects;

/**
 * @author young
 * @date 2018/5/22
 */
public class ResponseModel<E> {

    public static final String SUCCESS = "200";
    public static final String FAILURE = "500";

    private String code;
    private String msg;
    private E data;


    @Override
    public String toString() {
        return "ResponseModel{" +
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

    public ResponseModel() {
    }

    public ResponseModel(String code, String msg, E data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean hasSuccess() {
        return Objects.equals(this.code, SUCCESS);
    }

    public static <E> ResponseModel<E> success(String msg) {
        return new ResponseModel<E>("200", msg, null);
    }

    public static <E> ResponseModel<E> success(E resultList) {
        return new ResponseModel<E>("200", "success", resultList);
    }


    public static <E> ResponseModel<E> fail(String msg) {
        return new ResponseModel<E>("500", msg, null);
    }

    public static <E> ResponseModel<E> fail(E resultList) {
        return new ResponseModel<E>("500", "failed", resultList);
    }

    public static <E> ResponseModel<E> fail(String resultCode, String msg, E resultList) {
        return new ResponseModel<E>(resultCode, msg, resultList);
    }

    public static <E> ResponseModel<E> fail(String msg, E resultList) {
        return new ResponseModel<E>("500", msg, resultList);
    }
}
