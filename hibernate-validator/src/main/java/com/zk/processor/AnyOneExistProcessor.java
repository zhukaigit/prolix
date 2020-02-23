package com.zk.processor;

import com.zk.annotation.AnyoneExist;
import com.zk.annotation.FieldGroup;
import com.zk.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 字段依赖校验
 */
@Slf4j
public class AnyOneExistProcessor implements ConstraintValidator<AnyoneExist, Object> {

    @Override
    public boolean isValid (Object object, ConstraintValidatorContext constraintValidatorContext) {
        Field[] fields = object.getClass().getDeclaredFields();

        HashMap<String, Boolean> groupResult = new HashMap<>();
        HashMap<String, List<String>> groupField = new HashMap<>();

        // 遍历每一个字段
        for (Field f : fields) {
            f.setAccessible(true);
            Annotation[] annotations = f.getAnnotations();

            // 遍历字段上的每一个注解
            for (Annotation annotation : annotations) {
                if (annotation instanceof FieldGroup) {
                    FieldGroup fieldGroup = (FieldGroup) annotation;
                    String groupKey = fieldGroup.value();
                    groupField.putIfAbsent(groupKey, new ArrayList<>());
                    // 默认为false
                    groupResult.putIfAbsent(groupKey, false);

                    // 将field加入到对应group的集合中
                    groupField.get(groupKey).add(f.getName());

                    try {
                        Object o = f.get(object);
                        if (o != null) {
                            if (CharSequence.class.isAssignableFrom(f.getType())) {
                                if (o.toString().trim().length() > 0) {
                                    groupResult.put(groupKey, true);
                                }
                            } else {
                                groupResult.put(groupKey, true);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        log.warn("无法获取属性值", e);
                    }
                }
            }
        }

        boolean result = true;
        StringBuilder errorMsg = new StringBuilder();

        for (String group : groupResult.keySet()) {
            if (!groupResult.get(group)) {
                result = false;
                errorMsg.append("字段").append(groupField.get(group)).append("至少存在一个不能为空; ");
            }
        }

        if (!result) {
            ValidatorUtil.ERR_MSG_CONTAINER.get().append(errorMsg.toString());
        }

        return result;
    }

}
