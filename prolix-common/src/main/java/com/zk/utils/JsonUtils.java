package com.zk.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Json工具类
 *
 * @author leon
 */
@SuppressWarnings("all")
public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static final String EMPTY_JSON = "{}";
    private static final String EMPTY_JSONS = "[]";

    private static final ObjectMapper MAPPER;

    private static final ObjectMapper MAPPER_HAS_NULL;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setSerializationInclusion(Include.NON_NULL);

        MAPPER_HAS_NULL = new ObjectMapper();
        MAPPER_HAS_NULL.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_HAS_NULL.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * java实体对象转换成json字符串 </br>
     * 默认对象中出现值为null时 隐藏json中的key
     */
    public static String toJsonNotNullKey(Object obj) {
        return JsonUtils.toJson(obj, MAPPER);
    }

    /**
     * java实体对象转换成json字符串 </br>
     * 默认对象中出现值为null时 显示json中的key,其值为null
     */
    public static String toJsonHasNullKey(Object obj) {
        return JsonUtils.toJson(obj, MAPPER_HAS_NULL);
    }

    /**
     * java实体对象转换成json字符串
     */
    private static String toJson(Object obj, ObjectMapper mapper) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        try {
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, obj);
            return writer.toString();
        } catch (IOException e) {
            LOGGER.error("对象转json字符串异常", e);
            throw new RuntimeException("对象转json字符串异常");
        }
    }

    /**
     * json字符串转换为对应的java实体对象
     */
    public static <T> T toObj(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return newInstance(objClass);
        }

        try {
            return MAPPER.readValue(jsonStr, objClass);
        } catch (IOException e) {
            LOGGER.error("jsonStr转换{}对象异常, jsonStr={}", objClass.getName(), jsonStr, e);
            throw new RuntimeException("jsonStr转换" + objClass.getName() + "对象异常");
        }
    }

    private static <T> T newInstance(Class<T> objClass) {
        try {
            return objClass.newInstance();
        } catch (Exception e) {
            LOGGER.error("failed to create new instance", e);
            throw new RuntimeException("cannot create new instance", e);
        }
    }

    public static <T> List<T> toObjList(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return Collections.emptyList();
        }

        try {
            return MAPPER.readValue(jsonStr,
                    MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, objClass));
        } catch (IOException e) {
            LOGGER.error("JsonUtils transformation string to objList has Eroor!", e);
        }
        return Collections.emptyList();
    }

    public static boolean isEmpty(String content) {
        return StringUtils.isBlank(content) || EMPTY_JSON.equals(content) || EMPTY_JSONS.equals(content);
    }

    public static boolean isNotEmpty(String content) {
        return !isEmpty(content);
    }

    public static JavaType getCollectionType(Class<?> paramClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(paramClass, elementClasses);
    }

    /**
     * 字符串转model对象，支持泛型使用
     *
     * @param jsonStr      如：[{"cn":"中国农业银行","dn":"103"},{"cn":"中国银行","dn":"104"}]
     * @param paramClass   ： List.class
     * @param elementClass BankType.class
     * @param <T>
     * @return 返回类型如： List<BankType>
     */
    public static <T> T parse(String jsonStr, Class<T> paramClass, Class<?>... elementClass) {
        try {
            return MAPPER.readValue(jsonStr, getCollectionType(paramClass, elementClass));
        } catch (IOException e) {
            LOGGER.error("parse error: ", e);
        }
        return null;
    }

    public static <T> T parse(String jsonStr, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            LOGGER.error("parse error: ", e);
        }
        return null;
    }

    /**
     * 将json字符串转换为ObjectNode（允许是个数组）
     *
     * @param jsonString
     * @return
     */
    public static JsonNode tree(String jsonString) {
        try {
            return MAPPER.readTree(jsonString);
        } catch (IOException e) {
            LOGGER.error("jsonString to ObjectNode error: ", e);
        }
        return null;
    }

    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> keyClass, Class<V> valueClass) {
        try {
            return MAPPER.readValue(jsonStr,
                    MAPPER.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * 创建新ObjectNode
     *
     * @return
     */
    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    /**
     * 创建新ArrayNode
     *
     * @return
     */
    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static Map<String, Object> toMap(String jsonStr) {
        return toMap(jsonStr, String.class, Object.class);
    }

    /**
     * 示例如下：
     *
     * @param jsonStr 如：{"success":true,"value":{"name":"zhang san"}}
     * @param keys    如："value", "name"
     * @return 返回：zhang san
     */
    public static String getValueDeeply(String jsonStr, String... keys) {
        if (keys.length == 0) {
            return jsonStr;
        }
        return getValueDeeply(toMap(jsonStr), keys, 0);
    }

    private static String getValueDeeply(Map<String, Object> map, String[] keys, int indexKey) {
        try {
            String key = keys[indexKey];
            if (indexKey == keys.length - 1) {
                Object value = map.get(key);
                if (value == null) {
                    return null;
                }
                if (value instanceof String && isJsonStr((String) value)) {
                    return (String) value;
                }
                return value instanceof Map || value instanceof List ? toJsonNotNullKey(value) : value.toString();
            } else {
                Map<String, Object> subMap = (Map<String, Object>) map.get(key);
                if (subMap == null || subMap.isEmpty()) {
                    return null;
                }
                return getValueDeeply(subMap, keys, ++indexKey);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public final static boolean isJsonStr(String jsonInString) {
        boolean b1 = jsonInString.startsWith("{") && jsonInString.endsWith("}");
        boolean b2 = jsonInString.startsWith("[") && jsonInString.endsWith("]");
        if (!(b1 || b2)) {
            return false;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
