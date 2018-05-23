package com.owitho.open.util.validate.annotation;

import java.lang.annotation.*;

/**
 * @author siliang.zheng
 * Date : 2018/1/4
 * Describle 字符串是否为数字
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsNumber {
}
