package com.zk.custom;

import com.google.common.base.Predicate;
import springfox.documentation.RequestHandler;

import java.lang.annotation.Annotation;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/8/4.
 */
public class RequestHandlerSelectorsExt {
    public static Predicate<RequestHandler> withMethodAnnotations
            (final Class<? extends Annotation>... annotations) {
        return (RequestHandler input) -> {
            for (Class<? extends Annotation> annotation : annotations) {
                boolean annotatedWith = input.isAnnotatedWith(annotation);
                if (annotatedWith) return true;
            }
            return false;
        };
    }
}
