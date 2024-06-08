package com.zk.guavaCache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.zk.guavaCache.CacheLoaderCreator.Employee;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaCacheTest {

    private static String calculate(String key) {
        System.out.println(String.format("计算key=%s", key));
        return "v_" + key;
    }

    //创建 Callable
    @Test
    public void test1() throws ExecutionException {
        Cache<Object, Object> cache = CacheBuilder.newBuilder().maximumSize(100).build();
        String key = "name";
        System.out.println("========= 第一次获取 =========");
        Object v1 = cache.get(key, () -> GuavaCacheTest.calculate(key));
        System.out.println(v1);
        System.out.println("========= 第二次获取 =========");
        Object v2 = cache.get(key, () -> GuavaCacheTest.calculate(key));
        System.out.println(v2);
    }

    //缓存大小，当达到maximumSize值时，最久没有被访问的key会被回收掉
    @Test
    public void testSize() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build(CacheLoaderCreator.createCacheLoader());
        cache.getUnchecked("key1");
        cache.getUnchecked("key2");
        cache.getUnchecked("key3");
        log.info("cache size = {}", cache.size());

        cache.getUnchecked("key4");
        Employee employee = cache.getIfPresent("key1"); //不会重新加载创建cache
        log.info("最新的把老的替换掉：" + (employee == null ? "是的" : "否"));
        Employee newEmployee = cache.getIfPresent("key4"); //不会重新加载创建cache
        log.info("是否在缓存中：{}", newEmployee == null ? "否" : "是");
    }

    //定时回收

    /**
     * TTL->time to live
     * Access time => Write/Update/Read
     */
    @Test
    public void testEvictionByAccessTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS)
                .build(CacheLoaderCreator.createCacheLoader());
        cache.getUnchecked("wangji");
        TimeUnit.SECONDS.sleep(3);
        Employee employee = cache.getIfPresent("wangji"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));
        cache.getUnchecked("guava");

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

    }

    /**
     * Write time => write/update
     */
    @Test
    public void testEvictionByWriteTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(CacheLoaderCreator.createCacheLoader());
        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(2);
        Employee employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        cache.put("guava", new Employee("guava", "guava" + "dept", "guava" + "id")); //手动插入
        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

        cache.put("guava", new Employee("guava", "guava" + "dept", "guava" + "id"));
        TimeUnit.SECONDS.sleep(1);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        log.info("被销毁：" + (employee == null ? "是的" : "否"));

    }


    /**
     * 显式清除
     *
     * 任何时候，你都可以显式地清除缓存项，而不是等到它被回收：
     * 个别清除：Cache.invalidate(key)
     * 批量清除：Cache.invalidateAll(keys)
     * 清除所有缓存项：Cache.invalidateAll()
     */

    //=================== 分割线 ===================

    @Test
    public void test() throws ExecutionException {
        HashMap<String, Integer> map = Maps.newHashMap();
        map.put("k1", 1);
        map.put("k2", 2);
        map.put("k3", 3);

        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
                    log.info("{} is loading ...", key);
                    return map.get(key);
                }
            });

        log.info("k1 = {}", cache.get("k1"));
        log.info("k2 = {}", cache.get("k2"));
        log.info("k3 = {}", cache.get("k3"));
        log.info("k1 = {}", cache.get("k1"));

        System.out.println("====================== 分割线 ======================");

        //修改k1的值
        map.put("k1", 11);
        log.info("k1 = {}", cache.get("k1"));//结果发现取的是缓存中的，不是最新的值
        System.out.println("====================== 分割线 ======================");

        //刷新k1的值
        cache.refresh("k1");
        log.info("k1值已刷新");
        log.info("k1 = {}", cache.get("k1"));//结果发现取的是缓存中的，不是最新的值
        System.out.println("====================== 分割线 ======================");

        cache.invalidateAll();
        log.info("cache.invalidateAll()已执行");
        log.info("k1 = {}", cache.get("k1"));
        log.info("k2 = {}", cache.get("k2"));
        log.info("k3 = {}", cache.get("k3"));


    }

    @Test
    public void test_cache() {
        Cache<String, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build();
        cache.put("key1", "value1");
        System.out.println(cache.asMap().containsKey("key1"));
        System.out.println(cache.getIfPresent("key1"));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(cache.getIfPresent("key1"));
        System.out.println(cache.asMap().containsKey("key1"));
    }

}
