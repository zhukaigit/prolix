package com.zk.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class AssertUtil {

    public static void notBlank (String data, String errMsg) throws RuntimeException {
        if (StringUtils.isBlank(data)) {
            throw new RuntimeException(errMsg);
        }
    }
    public static void notNull (Object data, String errMsg) throws RuntimeException {
        if (data == null) {
            throw new RuntimeException(errMsg);
        }
    }
    public static void notEmpty (Collection coll, String errMsg) throws RuntimeException {
        if (coll==null || coll.isEmpty()) {
            throw new RuntimeException(errMsg);
        }
    }
    public static void assertTrue (boolean condition, String errMsg) throws RuntimeException {
        if (!condition) {
            throw new RuntimeException(errMsg);
        }
    }
}
