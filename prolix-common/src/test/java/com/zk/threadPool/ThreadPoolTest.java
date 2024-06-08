package com.zk.threadPool;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    /**
     * 队列容量大小：调小
     */
    @Test
    public void test4() {
        int capacity = 5;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS, new ResizeLinkedBlockingQueue<>(capacity));
        printThreadPoolExecutorDetail("位置A", pool);
        for (int i = 1; i <= capacity; i++) {
            int finalI = i;
            pool.execute(() -> doing(finalI * 1000, finalI + ""));
            printThreadPoolExecutorDetail("内循环" + finalI, pool);
        }
        ResizeLinkedBlockingQueue queue = (ResizeLinkedBlockingQueue) pool.getQueue();
        printThreadPoolExecutorDetail("位置B", pool);

        int remainingCapacity = queue.remainingCapacity();
        Assert.assertTrue(remainingCapacity == 1);

        queue.setCapacity(capacity - 1);
        System.out.println("调小队列大小，减1个");
        printThreadPoolExecutorDetail("位置C", pool);

        System.out.println("即将新增一个任务到线程池，线程池会触发拒绝策略");
        runWithCatchException(() -> pool.execute(() -> doing(7000, "7")));

        System.out.println("重置队列大小为2");
        queue.setCapacity(2);
        printThreadPoolExecutorDetail("位置D", pool);

        waitThreadPoolExecutedFinish(pool);
        printThreadPoolExecutorDetail("位置E", pool);
        Assert.assertTrue(queue.remainingCapacity() == 2);

    }

    /**
     * 队列容量大小：调大
     */
    @Test
    public void test3() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS, new ResizeLinkedBlockingQueue<>(5));
        printThreadPoolExecutorDetail("位置A", pool);
        for (int i = 1; i <= 6; i++) {
            int finalI = i;
            pool.execute(() -> doing(finalI * 1000, finalI + ""));
            printThreadPoolExecutorDetail("内循环" + finalI, pool);
        }
        ResizeLinkedBlockingQueue queue = (ResizeLinkedBlockingQueue) pool.getQueue();
        printThreadPoolExecutorDetail("位置B", pool);

        System.out.println("下面报错");
        runWithCatchException(() -> pool.execute(() -> doing(7000, "7")));

        System.out.println("重置队列大小");
        queue.setCapacity(6);
        runWithCatchException(() -> pool.execute(() -> doing(8000, "8")));
        System.out.println("没有报错");
        printThreadPoolExecutorDetail("位置C", pool);

        waitThreadPoolExecutedFinish(pool);
        printThreadPoolExecutorDetail("位置D", pool);

    }

    private void runWithCatchException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            System.out.println("【错误信息】" + e.getMessage());
        }
    }

    private void waitThreadPoolExecutedFinish(ThreadPoolExecutor pool) {
        sleep(100);
        pool.shutdown();
        boolean terminated = pool.isTerminated();
        while (!terminated) {
            try {
                terminated = pool.awaitTermination(300, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 动态修改线程池参数：
     * MaximumPoolSize
     * CorePoolSize
     * KeepAliveTime
     */
    @Test
    public void test2() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 3, 3, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
        printThreadPoolExecutorDetail("初始位置", pool);

        pool.execute(() -> doing(3000));
        pool.execute(() -> doing(3000));
        pool.execute(() -> doing(3000));

        pool.setCorePoolSize(1);
        pool.setKeepAliveTime(4, TimeUnit.SECONDS);
        pool.setMaximumPoolSize(1);

        printThreadPoolExecutorDetail("初始位置2", pool);

        sleep(100);
        for (int i = 1; i < 4; i++) {
            sleep(1000);
            printThreadPoolExecutorDetail("内循环" + i, pool);
        }

    }

    @Test
    public void test() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, 3, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
        printThreadPoolExecutorDetail("初始位置", pool);
        pool.execute(() -> doing(3000));
        printThreadPoolExecutorDetail("位置1", pool);
        pool.execute(() -> doing(4000));
        printThreadPoolExecutorDetail("位置2", pool);
        pool.execute(() -> doing(5000));
        printThreadPoolExecutorDetail("位置3", pool);
        pool.execute(() -> doing(6000));
        printThreadPoolExecutorDetail("位置4", pool);
        pool.execute(() -> doing(7000));
        printThreadPoolExecutorDetail("位置5", pool);
        pool.execute(() -> doing(8000));
        printThreadPoolExecutorDetail("位置6", pool);

        sleep(100);
        for (int i = 1; i < 10; i++) {
            sleep(1000);
            printThreadPoolExecutorDetail("内循环" + i, pool);
            if (i == 2) {
                System.out.println("此时任务逐渐结束了");
            }
            if (i == 5) {
                System.out.println("此时非核心线程开始消亡");
            }
        }
    }

    private void printThreadPoolExecutorDetail(String flag, ThreadPoolExecutor pool) {
        BlockingQueue<Runnable> queue = pool.getQueue();
        StringBuilder sb = new StringBuilder()
                // 最大线程数
                .append("MaximumPoolSize").append(": ").append(pool.getMaximumPoolSize()).append(", ")
                // 核心线程数
                .append("CorePoolSize").append(" : ").append(pool.getCorePoolSize()).append(", ")
                // 非核心线程空闲时间
                .append("KeepAliveTime").append(" : ").append(pool.getKeepAliveTime(TimeUnit.SECONDS)).append("秒,")
                // 线程池历史最大的线程数量
                .append("LargestPoolSize").append(" : ").append(pool.getLargestPoolSize()).append(", ")
                // 线程池当前线程数量
                .append("PoolSize").append(" : ").append(pool.getPoolSize()).append(", ")
                // 线程池当前活动线程数量（即正在执行任务的线程数量）
                .append("ActiveCount").append(" : ").append(pool.getActiveCount()).append(", ")
                // 返回已完成执行的任务的大致总数（近似值）
                .append("CompletedTaskCount").append(" : ").append(pool.getCompletedTaskCount()).append(", ")
                // 已计划执行的任务的大致总数（近似值）
                .append("TaskCount").append(" : ").append(pool.getTaskCount()).append(", ")
                .append("remainingCapacity").append(" : ").append(queue.remainingCapacity()).append(", ")
                .append("queueSize").append(" : ").append(queue.size()).append(", ")
//                .append("RejectedExecutionHandler").append(" : ").append(pool.getRejectedExecutionHandler().getClass().getSimpleName()).append(", ")
//                .append("ThreadFactory").append(" : ").append(pool.getThreadFactory().getClass().getSimpleName()).append(", ")
                ;
        printKeyAndValue(flag, sb, 10);

    }


    private void printKeyAndValue(String key, Object value, int keyLength) {
        StringBuilder sb = new StringBuilder()
                .append(StringUtils.rightPad(key, keyLength))
                .append(StringUtils.rightPad("==>", 5))
                .append(value);
        System.out.println(sb);
    }

    private void doing(long mills, String msg) {
        long start = System.currentTimeMillis();
        long end;
        do {
            end = System.currentTimeMillis();
        } while ((end - start) < mills);
        System.out.println("执行完成：" + msg);
    }

    private void doing(long mills) {
        long start = System.currentTimeMillis();
        long end;
        do {
            end = System.currentTimeMillis();
        } while ((end - start) < mills);
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
