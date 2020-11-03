package com.zk.utils.pool;

import com.zk.utils.ExceptionStackTraceUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 测试后关注点：
 * 【一】 returnObject时，若此时池中的空闲对象大于maxIdle，那么该返回的池对象会被直接销毁
 * 【二】 若timeBetweenEvictionRunsMillis大于0，且minEvictableIdleTimeMillis、softMinEvictableIdleTimeMillis至少有一个不为负数
 * ，那么空余空余对象最终会降到minIdle个。
 * 【三】 若minEvictableIdleTimeMillis、softMinEvictableIdleTimeMillis都为负数，或者timeBetweenEvictionRunsMillis为负数，那么当
 * 池中连接数量大于maxIdle时，最终池中空闲保持maxIdle个
 * 【四】 timeBetweenEvictionRunsMillis和minEvictableIdleTimeMillis都大于0，若池对象空闲时间达到minEvictableIdleTimeMillis时，
 * 可能会被销毁（不一定是时间一达到就立即销毁，因为要检测线程执行检察时，判断满足条件才会去销毁），不管当前池对象的空闲对象是否小于
 * minIdle，所以这个设置可以保证空闲的池对象存活时间一般不会超过minEvictableIdleTimeMillis
 * 【五】softMinEvictableIdleTimeMillis和minEvictableIdleTimeMillis比较（前提是生效，即timeBetweenEvictionRunsMillis大于0）：
 *  相同点：都会让空闲池对象数量降低到minIdle
 *  不同点：minEvictableIdleTimeMillis：只要时间达到minEvictableIdleTimeMillis，池对象就会销毁（若销毁后空闲池对象数量小于minIdle，会在create一个）；
 *         softMinEvictableIdleTimeMillis：池对象空闲时间达到softMinEvictableIdleTimeMillis，且空闲池对象数量大于minIdle，才会销毁该池对象
 */
public class MyPoolObjectPoolTest {


    // 测试对象的复用，若对象池没有对象，则等待
    @Test
    public void test1 () {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(1).maxIdle(1).minIdle(1)
                .blockWhenExhausted(true).maxWaitMillis(-1)// 等待连接超时时间
                .minEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池中对象的空闲时间大于minEvictableIdleTimeMillis，该池对象会被销毁，即使当前池中对象数量是否小于minIdle
                .softMinEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池对象空闲时间大于softMinEvictableIdleTimeMillis且池中对象数量大于minIdle，该池对象销毁
                .timeBetweenEvictionRunsMillis(100)
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        final ArrayList<MyPoolObject> container = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            executorService.execute(() -> {
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject + ", i =" + finalI);
                container.add(myPoolObject);
                sleep(6000);
                pool.returnObject(myPoolObject);
            });
            sleep(1000);
        }

        sleep(10000);
        Assert.assertTrue(container.get(0) == container.get(1));
    }

    // 测试对象池没有对象时，在等待时间内超时
    @Test
    public void test2 () {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(1).maxIdle(1).minIdle(1)
                .blockWhenExhausted(true).maxWaitMillis(1)// 等待连接超时时间
                .minEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池中对象的空闲时间大于minEvictableIdleTimeMillis，该池对象会被销毁，即使当前池中对象数量是否小于minIdle
                .softMinEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池对象空闲时间大于softMinEvictableIdleTimeMillis且池中对象数量大于minIdle，该池对象销毁
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        AtomicReference<String> errorMsg = new AtomicReference<>();
        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            executorService.execute(() -> {
                MyPoolObject myPoolObject = null;
                try {
                    myPoolObject = pool.borrowObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg.set(ExceptionStackTraceUtil.getStackTrace(e));
                }
                System.out.println("获取到的object=" + myPoolObject + ", i =" + finalI);
                sleep(6000);
                pool.returnObject(myPoolObject);
            });
            sleep(1000);
        }

        sleep(2000);
        assert errorMsg.get() != null;
        Assert.assertTrue(errorMsg.get().contains("Caused by: java.util.NoSuchElementException: Timeout waiting for idle object"));

    }

    /**
     * 池对象被销毁只需要满足以下任意一个条件即可：
     * 1 在空闲检测时，池对象空闲时间大于softMinEvictableIdleTimeMillis且池中的空闲连接数大于minIdle，该池对象销毁【使得空闲对象最终降到minIdle】
     * 2 在空闲检测时，池对象空闲时间大于minEvictableIdleTimeMillis，该池对象销毁【使得空闲对象最终降到minIdle】
     * 3 在空闲检测时，对象池空闲对象数量大于maxIdle，若是FIFO，则根据顺序销毁池对象
     *
     * 测试目标：因为minEvictableIdleTimeMillis和softMinEvictableIdleTimeMillis都设置为-1，那么在空闲大于maxIdle
     * 情况下，会删除多余空闲连接【使得空闲对象最终降到maxIdle】
     */
    @Test
    public void testMinEvictableIdleTimeMillis_bothNegative () {
        int minIdle = 1;
        int maxIdle = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(4).maxIdle(maxIdle).minIdle(minIdle)
                .blockWhenExhausted(false).maxWaitMillis(-1)// 当blockWhenExhausted为false时，没有空余对象直接报错，且设置的maxWaitMillis不生效，
                .minEvictableIdleTimeMillis(-1)
                .softMinEvictableIdleTimeMillis(-1)
                .timeBetweenEvictionRunsMillis(100L)// 空闲连接检测的周期
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        for (int i = 1; i <= 4; i++) {
            executorService.execute(() -> {
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject);
                sleep(12000);
                pool.returnObject(myPoolObject);
                System.out.println("归还池对象：" + myPoolObject + ", 归还时间：" + getNow());
            });
            sleep(1500);
        }
        long start = System.currentTimeMillis();

        long time = System.currentTimeMillis() - start;
        while (time < 30000) {
            sleep(2000);
            System.out.println(String.format("getNumActive=%s, time=%s", pool.getNumActive(), time));
            System.out.println(String.format("getNumIdle=%s, time=%s", pool.getNumIdle(), time));
            System.out.println("=======================");
            System.out.println();
            time = System.currentTimeMillis() - start;
        }
        Assert.assertTrue(maxIdle == pool.getNumIdle());

    }


    /**
     * 池对象被销毁只需要满足以下任意一个条件即可：
     * 1 在空闲检测时，池对象空闲时间大于softMinEvictableIdleTimeMillis且池中的空闲连接数大于minIdle，该池对象销毁
     * 2 在空闲检测时，池对象空闲时间大于minEvictableIdleTimeMillis，该池对象销毁
     * 3 在空闲检测时，对象池空闲对象数量大于maxIdle，若是FIFO，则根据顺序销毁池对象
     *
     * 测试目标：首先创建任务将池对象创建数量达到maxTotal，当returnObject时，若此时池中空闲池对象达到maxIdle，则直接销毁该return的池对象。对于池中空余的池对象，再根据
     * minEvictableIdleTimeMillis时间删除空闲池对象，直到空闲池对象降低到minIdle
     */
    @Test
    public void testMinEvictableIdleTimeMillis_positive () {
        int minIdle = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(5).maxIdle(3).minIdle(minIdle)
                .blockWhenExhausted(false).maxWaitMillis(-1)// 当blockWhenExhausted为false时，没有空余对象直接报错，且设置的maxWaitMillis不生效，
                .minEvictableIdleTimeMillis(3000)
                .softMinEvictableIdleTimeMillis(-1)
                .timeBetweenEvictionRunsMillis(100L)//空闲连接检测的周期
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        for (int i = 1; i <= 5; i++) {
            executorService.execute(() -> {
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject);
                sleep(12000);
                pool.returnObject(myPoolObject);
                System.out.println("归还池对象：" + myPoolObject + ", 归还时间：" + getNow());
            });
            sleep(1000);
        }
        long start = System.currentTimeMillis();

        long time = System.currentTimeMillis() - start;
        while (time < 30000) {
            sleep(2000);
            System.out.println(String.format("getNumActive=%s, time=%s", pool.getNumActive(), time));
            System.out.println(String.format("getNumIdle=%s, time=%s", pool.getNumIdle(), time));
            System.out.println("=======================");
            System.out.println();
            time = System.currentTimeMillis() - start;
        }
        Assert.assertTrue(minIdle == pool.getNumIdle());

    }

    /**
     * 池对象被销毁只需要满足以下任意一个条件即可：
     * 1 在空闲检测时，池对象空闲时间大于softMinEvictableIdleTimeMillis且池中的空闲连接数大于minIdle，该池对象销毁
     * 2 在空闲检测时，池对象空闲时间大于minEvictableIdleTimeMillis，该池对象销毁
     * 3 在空闲检测时，对象池空闲对象数量大于maxIdle，若是FIFO，则根据顺序销毁池对象
     *
     * 测试目标：首先创建任务将池对象创建数量达到maxTotal，当returnObject时，若此时池中空闲池对象达到maxIdle，则直接销毁该return的池对象。对于池中空余的池对象，再根据
     * softMinEvictableIdleTimeMillis时间删除空闲池对象，直到空闲池对象降低到minIdle
     */
    @Test
    public void testMinEvictableIdleTimeMillis_negative () {
        int minIdle = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(3).maxIdle(3).minIdle(minIdle)
                .blockWhenExhausted(false).maxWaitMillis(-1)// 当blockWhenExhausted为false时，没有空余对象直接报错，且设置的maxWaitMillis不生效，
                .minEvictableIdleTimeMillis(-1)
                .softMinEvictableIdleTimeMillis(5000L)
                .timeBetweenEvictionRunsMillis(100L)//空闲连接检测的周期
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        for (int i = 1; i <= 2; i++) {
            executorService.execute(() -> {
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject);
                sleep(8000);
                pool.returnObject(myPoolObject);
                System.out.println("归还池对象：" + myPoolObject + ", 归还时间：" + getNow());
            });
            sleep(1000);
        }
        long start = System.currentTimeMillis();

        long time = System.currentTimeMillis() - start;
        while (time < 30000) {
            sleep(2000);
            System.out.println(String.format("getNumActive=%s, time=%s", pool.getNumActive(), time));
            System.out.println(String.format("getNumIdle=%s, time=%s", pool.getNumIdle(), time));
            System.out.println("=======================");
            System.out.println();
            time = System.currentTimeMillis() - start;
        }
        Assert.assertTrue(minIdle == pool.getNumIdle());

    }

    /**
     * 池对象被销毁只需要满足以下任意一个条件即可：
     * 1 在空闲检测时，池对象空闲时间大于softMinEvictableIdleTimeMillis且池中的空闲连接数大于minIdle，该池对象销毁
     * 2 在空闲检测时，池对象空闲时间大于minEvictableIdleTimeMillis，该池对象销毁
     * 3 在空闲检测时，对象池空闲对象数量大于maxIdle，若是FIFO，则根据顺序销毁池对象
     *
     * 测试目标：若timeBetweenEvictionRunsMillis=-1,则softMinEvictableIdleTimeMillis和minEvictableIdleTimeMillis设置不会生效，
     * 若池中对象超过maxIdle，最终也只会下降到maxIdle，也是在returnObject时销毁的
     */
    @Test
    public void testTimeBetweenEvictionRunsMillis_negative () {
        int minIdle = 1;
        int maxIdle = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(4).maxIdle(maxIdle).minIdle(minIdle)
                .blockWhenExhausted(false).maxWaitMillis(-1)// 当blockWhenExhausted为false时，没有空余对象直接报错，且设置的maxWaitMillis不生效，
                .minEvictableIdleTimeMillis(-1)
                .softMinEvictableIdleTimeMillis(5000L)
                .timeBetweenEvictionRunsMillis(-1)//空闲连接检测的周期
                .build();

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory(), config);

        for (int i = 1; i <= 4; i++) {
            executorService.execute(() -> {
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject);
                sleep(8000);
                pool.returnObject(myPoolObject);
                System.out.println("归还池对象：" + myPoolObject + ", 归还时间：" + getNow());
            });
            sleep(1000);
        }
        long start = System.currentTimeMillis();

        long time = System.currentTimeMillis() - start;
        while (time < 30000) {
            sleep(2000);
            System.out.println(String.format("getNumActive=%s, time=%s", pool.getNumActive(), time));
            System.out.println(String.format("getNumIdle=%s, time=%s", pool.getNumIdle(), time));
            System.out.println("=======================");
            System.out.println();
            time = System.currentTimeMillis() - start;
        }
        Assert.assertTrue(maxIdle == pool.getNumIdle());

    }

    @Test
    public void test() {
        GenericObjectPoolConfig config = MyPoolConfig.builder()
                .maxTotal(1).maxIdle(1).minIdle(1)
                .blockWhenExhausted(true).maxWaitMillis(-1)// 等待连接超时时间
                .minEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池中对象的空闲时间大于minEvictableIdleTimeMillis，该池对象会被销毁，即使当前池中对象数量是否小于minIdle
                .softMinEvictableIdleTimeMillis(-1)// 在空闲线程检测时，若池对象空闲时间大于softMinEvictableIdleTimeMillis且池中对象数量大于minIdle，该池对象销毁
                .timeBetweenEvictionRunsMillis(100)
                .build();
        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory2(), config);
        MyPoolObject myPoolObject = pool.borrowObject();
        System.out.println("获取到的池对象：" + myPoolObject);
        pool.returnObject(myPoolObject);

    }


    private void sleep (long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private String getNow () {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm:ss"));
    }
}
