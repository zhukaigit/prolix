package com.zk.guavaCache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheLoaderCreator {

    public static com.google.common.cache.CacheLoader<String, Employee> createCacheLoader() {
        return new com.google.common.cache.CacheLoader<String, Employee>() {
            @Override
            public Employee load(String key) {
                log.info("不在缓存中，此处一般从数据库中获取，或者经过复杂计算得到。 入参key = {}", key);
                return new Employee(key, key + "_dept", key + "_id");
            }
        };
    }

    @Data
    @AllArgsConstructor
    @ToString
    public static class Employee {

        private String key;
        private String dept;
        private String id;
    }

}
