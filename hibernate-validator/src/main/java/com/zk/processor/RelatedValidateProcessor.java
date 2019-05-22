package com.zk.processor;

import com.zk.annotation.RelatedValidate;
import com.zk.annotation.sub.ProcessorHandler;
import com.zk.exception.ValidateException;
import com.zk.processor.sub.IProcessor;
import com.zk.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段依赖校验
 */
@Slf4j
public class RelatedValidateProcessor implements ConstraintValidator<RelatedValidate, Object> {

    @Override
    public boolean isValid (Object object, ConstraintValidatorContext constraintValidatorContext) {
        Field[] fields = object.getClass().getDeclaredFields();
        // 遍历每一个字段
        for (Field f : fields) {
            f.setAccessible(true);
            Annotation[] annotations = f.getAnnotations();

            // 遍历字段上的每一个注解
            for (Annotation annotation : annotations) {
                // 通过判断该注解类型上是否有@ProcessorHandler注解，来确定是否需要处理
                ProcessorHandler processorHandler = annotation.annotationType().getAnnotation(ProcessorHandler.class);
                if (processorHandler == null) {
                    continue;
                }

                // 实例化对应处理器
                Class<? extends IProcessor> processor = processorHandler.value();
                IProcessor subProcessor = null;
                try {
                    subProcessor = processor.newInstance();
                } catch (Exception e) {
                    log.error("实例化{}处理器异常", processor.getName(), e);
                    return false;
                }

                // 处理器校验
                try {
                    subProcessor.valid(annotation, object);
                } catch (ValidateException e) {
                    log.error("属性{}不符合规范", f.getName());
                    ValidatorUtil.ERR_MSG_CONTAINER.get().put(f.getName(), "不符合规范");
                    return false;
                } catch (Exception e) {
                    log.error("内部异常", e);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void initialize (RelatedValidate constraintAnnotation) {

    }
}
