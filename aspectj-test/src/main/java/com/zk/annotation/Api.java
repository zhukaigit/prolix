package com.zk.annotation;

import java.lang.annotation.*;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {
    boolean required() default false;
}
