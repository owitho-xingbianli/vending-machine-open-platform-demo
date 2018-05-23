package com.owitho.open.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owitho.open.exception.JsonException;
import com.owitho.open.model.ResponseModel;

import java.io.IOException;
import java.util.List;

/**
 * @author di 2015年09月12日02:30:03
 */
public final class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将一个对象，转换为json
     *
     * @param obj
     * @return
     */
    public static String transObjToJsonString(Object obj) {
        return transObjToJsonString(obj, false);
    }

    public static String transObjToJsonString(Object obj, boolean throwOrNot) {
        String str;
        try {
            str = mapper.writeValueAsString(obj);
            return str;
        } catch (IOException e) {
            if (throwOrNot) {
                throw new JsonException(e);
            }
        }
        return "";// return 空字符串是为了保证调用方的safety
    }

    /**
     * Json转换为对象
     *
     * @param json
     * @param cla
     * @return
     */
    public static <T> T transJsonStringToObj(String json, Class<T> cla, boolean throwOrNot) {
        try {
            T t = mapper.readValue(json, cla);
            return t;
        } catch (IOException e) {
            if (throwOrNot) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    public static <T> T transJsonStringToObj(String json, Class<T> cla) {
        return transJsonStringToObj(json, cla, false);
    }

    public static <T> List<T> transJsonStringToObj(String json, TypeReference<List<T>> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static <T> ResponseModel<T> transJsonStringToResp(String json, TypeReference<ResponseModel<T>> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

}
