package com.zk.annotation;

import java.lang.annotation.*;

/**
 * Created by zhukai on 2017/12/3.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamName {
    String name() default "";
}
