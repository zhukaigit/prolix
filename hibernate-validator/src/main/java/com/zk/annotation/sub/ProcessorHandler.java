package com.zk.annotation.sub;

import com.zk.processor.sub.IProcessor;

import java.lang.annotation.*;

/**
 * 描述：修饰注解类。用于指定{@package com.zk.annotation.sub}包下自定义注解的{@link IProcessor}实现类
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessorHandler {
  Class<? extends IProcessor> value ();
}
