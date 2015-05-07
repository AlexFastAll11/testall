package com.ibm.cn.cdl.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {
    String value() default "";
    boolean require() default true;
}
