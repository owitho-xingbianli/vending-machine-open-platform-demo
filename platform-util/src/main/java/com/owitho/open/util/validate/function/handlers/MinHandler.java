package com.owitho.open.util.validate.function.handlers;

import com.owitho.open.util.validate.ValidateException.ValidateException;
import com.owitho.open.util.validate.annotation.Min;

import java.lang.reflect.Field;

/**
 * Created by pengfei.feng on 2017/9/12
 */
public class MinHandler extends AbstractHandler {

    public <T, F extends Field, E extends RuntimeException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) {
        if (isDeep) deepCheck(originBean, field, forceException, exception);
        else normalCheck(originBean, field, forceException, exception);
    }

    private static <T, F extends Field, E extends RuntimeException> void deepCheck(T originBean, F field, boolean forceException, E exception) {
        // TODO: 20/7/2016 日后支持深度
        System.out.println("not support deep check now.");
    }


    private static <T, F extends Field, E extends RuntimeException> void normalCheck(T originBean, F field, boolean forceException, E exception) {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();
        boolean flag = false;

        field.setAccessible(true);

        try {
            Object o = field.get(originBean);
            if (o == null) throw new IllegalAccessException();

            Min minAnnotation = field.getAnnotation(Min.class);
            if (minAnnotation != null) {
                if (o instanceof Integer) {
                    if ((Integer) o < minAnnotation.value()) flag = true;
                } else if (o instanceof Long) {
                    if ((Long) o < minAnnotation.value()) flag = true;
                } else {
                    throw new ClassCastException();
                }
                if (flag)
                    throw forceException ? exception : new ValidateException(String.format("%s 's field :%s is smaller than min :%s!", beanName, fieldName, minAnnotation.value()));
            }
        } catch (IllegalAccessException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s can not be null!", beanName, fieldName));
        } catch (ClassCastException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s is not num!", beanName, fieldName));
        }
    }
}
