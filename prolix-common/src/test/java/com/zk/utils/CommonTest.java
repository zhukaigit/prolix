package com.zk.utils;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.cache.*;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.IsEqual.equalTo;

public class CommonTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    System.out.println("线程" + finalI + "执行...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "线程" + finalI).start();
        }


        try {
            System.out.println("预备...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始...");
        countDownLatch.countDown();
        System.out.println("ok");
    }

    @Test
    public void testSort2() {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Model.builder().filed1(RandomUtil.getRandomScope(1, 10)).filed2(RandomUtil.getRandomScope(1, 5)).build());
        }

        System.out.println("\n=========== 排序后 =============");
        list.sort(Comparator.comparing(Model::getFiled1).thenComparing(Model::getFiled2));
        for (Model e : list) {
            System.out.println(e.toString());
        }

    }

    @Data
    @Builder
    public static class Model {
        private int filed1;
        private int filed2;
    }
    /**
     * 获取指定范围的随机数
     * @param min 最小值
     * @param max 最大值
     * @return 返回结果大于等于min，小于max
     */
    public static int getRandomScope(int min, int max) {
        SecureRandom s = new SecureRandom();
        s.setSeed(UUID.randomUUID().toString().getBytes(Charset.forName("utf-8")));
        int i = s.nextInt((max - min));// 0 <= i < (max-min)
        return i + min;
    }


    @Test
    public void testSort() {
        SortClass e1 = SortClass.builder().name("e1").conditionSeat("000").build();
        SortClass e2 = SortClass.builder().name("e2").conditionSeat("001").build();
        SortClass e3 = SortClass.builder().name("e3").conditionSeat("011").build();
        SortClass e3_3 = SortClass.builder().name("e3_3").conditionSeat("011").build();
        SortClass e4 = SortClass.builder().name("e4").conditionSeat("010").build();
        SortClass e4_4 = SortClass.builder().name("e4_4").conditionSeat("010").build();
        SortClass e5 = SortClass.builder().name("e5").conditionSeat("110").build();
        SortClass e6 = SortClass.builder().name("e6").conditionSeat("111").build();
        ArrayList<SortClass> list = Lists.newArrayList(e1, e2, e3, e4, e5, e6, e3_3, e4_4);

        System.out.println("\n=========== 只根据conditionSeat排序后 =============");
        list.sort(Comparator.comparing(SortClass::getConditionSeat));
        for (SortClass sortClass : list) {
            System.out.println(sortClass.toString());
        }


        System.out.println("\n=========== 只conditionSeat、field2排序后 =============");
        list.sort(Comparator.comparing(SortClass::getConditionSeat).thenComparing(SortClass::getField2));
        for (SortClass sortClass : list) {
            System.out.println(sortClass.toString());
        }

        System.out.println("\n=========== 新增险种组合 =============");
        e3.setField2(2);

        e4.setField2(4);
        e4_4.setField2(1);
        list.sort(Comparator.comparing(SortClass::getConditionSeat).thenComparing(SortClass::getField2));
        for (SortClass sortClass : list) {
            System.out.println(sortClass.toString());
        }
    }

    @Data
    @Builder
    public static class SortClass {
        private String name;
        private String conditionSeat;
        private int field2;
    }

    @Test
    public void test3() {
//        for (int ncpus = 1; ncpus < 50; ncpus++) {
//            int i = (ncpus <= 8) ? ncpus : 3 + ((ncpus * 5) / 8);
//            System.out.println("cpu=" + ncpus + ", 线程数=" + i);
//        }
//        for (int ParallelGCThreads = 1; ParallelGCThreads < 50; ParallelGCThreads++) {
//            int i = (ParallelGCThreads + 3) / 4;
//            System.out.println("ParallelGCThreads=" + ParallelGCThreads + ", ParallelCMSThreads=" + i);
//        }


        int _1MB = 1024 * 1024;

        ArrayList<Object> objects = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            objects.add(new byte[_1MB]);
        }



    }


    private static boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1 instanceof CharSequence && obj2 instanceof CharSequence) {
            return obj1.toString().equals(obj2.toString());
        }
        return false;
    }

    @Test
    public void test2() throws InterruptedException {
        System.gc();
        System.out.println("最初剩余空间 =============》"+ Runtime.getRuntime().freeMemory() / 1024 / 1024 + "");
        System.out.println("\r\n =========== 开始添加 =============\r\n");
        List list = new ArrayList();
        while (true) {
//        for (int i = 0; i < 2000; i++) {
            list.add(new byte[1024 * 1024]);
        }
//        System.out.println("\r\n =========== 结束添加 =============\r\n");
//        System.out.println("剩余空间 =============》"+ Runtime.getRuntime().freeMemory() / 1024 / 1024 + "");
//        list = null;
//        sleep(1000);
//        System.gc();

    }

    @Test
    public void testCallerRunsPolicy() throws InterruptedException {
        int threadSize = 1;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(threadSize, threadSize,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1), new ThreadPoolExecutor.CallerRunsPolicy());

        println("我是老板");

        CountDownLatch countDownLatch = new CountDownLatch(2);
        // 目的：没有空余线程
        pool.execute(() -> {
            sleep(5000);
            countDownLatch.countDown();
        });
        pool.execute(() -> {
            sleep(5000);
            countDownLatch.countDown();
        });

        sleep(200);
        println("我是老板");

        pool.execute(() -> {
            println("占用线程任务3s");
            sleep(3000);
            println("占用线程任务3s，结束");
            countDownLatch.countDown();
        });

        countDownLatch.await();

    }

    /**
     * 测试场景：在主线程更新，同一线程每次都能获取到新的值
     * {@link TransmittableThreadLocal}
     * {@link InheritableThreadLocal}
     * @throws InterruptedException
     */
    @Test
    public void testTransmittableThreadLocal() throws InterruptedException {
        int threadSize = 1;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(threadSize, threadSize,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1), new ThreadPoolExecutor.CallerRunsPolicy());
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(pool);

        // 目的：没有空余线程
        ttlExecutorService.submit(() -> {// 占据当前活跃线程
            sleep(10* 1000);
        });
        ttlExecutorService.submit(() -> {// 占据排队队列
            sleep(10* 1000);
        });


        InheritableThreadLocal<U> threadLocal = new TransmittableThreadLocal<>();
        threadLocal.set(new U(10));
        System.out.println("父线程获取数据：" + threadLocal.get());


        CountDownLatch countDownLatch = new CountDownLatch(2);
        ttlExecutorService.submit(() -> {
            System.out.println("start 1");
            println(threadLocal.get());
            sleep(1000);
            threadLocal.get().setAge(12);
            println(threadLocal.get());
            threadLocal.remove();
            println(threadLocal.get());
            countDownLatch.countDown();
            sleep(3000);
            println("finish");
        });

        sleep(500);
        System.out.println("父线程获取数据2：" + threadLocal.get());

        ttlExecutorService.submit(() -> {
            System.out.println("start 2");
            println(threadLocal.get());
            sleep(5000);
            println(threadLocal.get());
            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @AllArgsConstructor
    @Data
    private static class U {
        private int age;
    }

    private static class MyCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            println("提交线程去执行任务...");
            super.rejectedExecution(r, e);
        }
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void println(Object msg) {
        Thread t = Thread.currentThread();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("【" + time + "】 " + t.getName() + "[" + t.hashCode() + "]： " + msg);
    }

    @Test
    public void testThreadLocal() throws InterruptedException {
        InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set(6);
        System.out.println("父线程获取数据：" + threadLocal.get());

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Thread.sleep(2000);
        threadLocal.set(6);
        executorService.submit(() -> {
            System.out.println("第一次从线程池中获取数据：" + threadLocal.get());
//            threadLocal.remove();
        });

        Thread.sleep(2000);
        threadLocal.set(7);
        executorService.submit(() -> {
            System.out.println("第二次从线程池中获取数据：" + threadLocal.get());
        });

//        System.out.println("父线程获取数据：" + threadLocal.get());
    }

    LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30L, TimeUnit.MILLISECONDS)
            .build(createCacheLoader());


    public static CacheLoader<String, Employee> createCacheLoader() {
        return new CacheLoader<String, Employee>() {
            @Override
            public Employee load(String key) {
                System.out.println("===========> 加载创建key：【" + key + "】");
                return new Employee(key, key + "dept", key + "id");
            }
        };
    }

    @Data
    @AllArgsConstructor
    public static class Employee {
        private String name;
        private String empID;
        private String Dept;
    }

    @Test
    public void testSize() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build(createCacheLoader());
        cache.getUnchecked("wangji");
        cache.getUnchecked("wangwang");
        cache.getUnchecked("old wang");
        Assert.assertThat(cache.size(), equalTo(3L));

        cache.getUnchecked("new wang");
        Employee employee = cache.getIfPresent("wangji"); //不会重新加载创建cache
        System.out.println("最新的把老的替换掉：" + (employee == null ? "是的" : "否"));
        Employee newEmployee = cache.getIfPresent("new wang 2"); //不会重新加载创建cache
        System.out.println("获取结果：" + newEmployee);
    }

    /**
     * TTL->time to live
     * Access time => Write/Update/Read
     * <p>
     * 注意：expireAfterAccess：超过指定时间没有访问该key则过期
     */
    @Test
    public void testEvictionByAccessTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS)
                .build(createCacheLoader());
        cache.getUnchecked("wangji");
        TimeUnit.SECONDS.sleep(3);
        Employee employee = cache.getIfPresent("wangji"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结果：" + cache.getIfPresent("guava"));
        TimeUnit.SECONDS.sleep(1);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));


    }

    /**
     * Write time => write/update
     * <p>
     * 注意：expireAfterWrite：数据写入到缓存后超过指定时间就过期
     */
    @Test
    public void testEvictionByWriteTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(createCacheLoader());
        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结果：" + cache.getIfPresent("guava"));
        TimeUnit.SECONDS.sleep(1);
        Employee employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

        cache.put("guava", new Employee("guava", "guava" + "dept", "guava" + "id")); //手动插入
        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

        cache.put("guava", new Employee("guava", "guava" + "dept", "guava" + "id"));
        TimeUnit.SECONDS.sleep(2);
        employee = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (employee == null ? "是的" : "否"));

    }

    @Test
    public void testCacheRemovedNotification() throws ExecutionException {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        RemovalListener<String, String> listener = notification ->
        {
            if (notification.wasEvicted()) {
                RemovalCause cause = notification.getCause();
                System.out.println("remove cacase is :" + cause.toString());
                System.out.println("key:" + notification.getKey() + "value:" + notification.getValue());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .removalListener(listener)// 添加删除监听
                .build(loader);
        cache.getUnchecked("wangji");
        cache.getUnchecked("wangwang");
        cache.getUnchecked("guava");
        cache.getUnchecked("test");
        cache.getUnchecked("test1");
    }

    @Test
    public void testGet() throws ExecutionException {

        Cache<String, String> cache = CacheBuilder.newBuilder().build();

        String keyNo = cache.getIfPresent("keyNo");
        System.out.println("keyNo = " + keyNo);

        String keyNo2 = cache.get("keyNo2", () -> "不存在");
        System.out.println("keyNo2 = " + keyNo2);

        String keyNo3 = cache.get("keyNo2", () -> "存在");
        System.out.println("keyNo2 = " + keyNo3);
    }
}
