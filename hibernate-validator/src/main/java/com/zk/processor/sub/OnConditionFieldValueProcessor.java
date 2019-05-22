package com.zk.processor.sub;

import com.zk.annotation.sub.IsNullOnConditionFieldValue;
import com.zk.exception.ValidateException;

import java.lang.reflect.Field;

public class OnConditionFieldValueProcessor implements IProcessor<IsNullOnConditionFieldValue> {
    @Override
    public void valid (IsNullOnConditionFieldValue annotation, Object object) throws Exception {
        String name = annotation.fieldName();
        String value = annotation.fieldValue();
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new ValidateException("不存在该属性：" + name, e);
        }
        field.setAccessible(true);
        Object o = field.get(object);
        if (o != null && value.equals(o.toString())) {
            return;
        }
        throw new ValidateException("不匹配");
    }
}
