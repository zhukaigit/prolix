package com.zk.processor.sub;

import java.lang.annotation.Annotation;

public interface IProcessor<T extends Annotation> {

    void valid (T annotation, Object object) throws Exception;
}
