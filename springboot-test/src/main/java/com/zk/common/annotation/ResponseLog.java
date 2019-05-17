package com.zk.common.annotation;


import java.lang.annotation.*;

/**
 * 用于标记需要记录请求与返回日志的方法或类
 * 主要是对于Controller类
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.METHOD, ElementType.TYPE})
public @interface ResponseLog {

    /**
     * 某个添加{@Code @ResponseLog}注解的类，若想要排除某个方法，则在该方法上加上该注解，并将该属性设置为true
     */
    boolean isExcluded () default false;

}
