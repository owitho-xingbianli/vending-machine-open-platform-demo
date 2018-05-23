package com.owitho.open.util.validate.function.handlers;

import com.owitho.open.util.validate.ValidateException.ValidateException;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;

/**
 * @author siliang.zheng
 * Date : 2018/1/4
 * Describle
 */
public class IsNumberHandler extends AbstractHandler {

    @Override
    public <T, F extends Field, E extends RuntimeException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();

        boolean isValid = false;
        try {
            field.setAccessible(true);
            Object obj = field.get(originBean);
            if (!(obj instanceof String)) {
                throw new ValidateException(String.format("%s's field %s is not a String!", beanName, fieldName));
            }
            if (!NumberUtils.isNumber((String) obj)) {
                isValid = false;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!isValid) {
            throw forceException ? exception : new ValidateException(String.format("%s's field %s is not a valid number!", beanName, fieldName));
        }
    }
}
