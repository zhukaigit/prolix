import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class TransmittableThreadLocalTest {

    private static final int _1Mb = 1024 * 1024;

    /**
     * 打印当前堆空闲内存（单位：MB）
     * @return
     */
    private String freeMemory() {
        return Runtime.getRuntime().freeMemory() / 1024 / 1024 + "";
    }

    /**
     * 背景：当线程池满了且线程池的RejectedExecutionHandler使用的是CallerRunsPolicy时，提交到线程池的任务会在提交线程中直接执行，
     * ThreadLocal.remove操作清理提交线程的上下文导致上下文丢失。
     * <p>
     * 测试场景：TransmittableThreadLocal不会出现这种情况
     * <p>
     * {@link TransmittableThreadLocal}
     * {@link InheritableThreadLocal}
     *
     * @throws InterruptedException
     */
    @Test
    public void testTransmittableThreadLocal_gc() throws InterruptedException {
        /**
         * 创建线程池
         * RejectedExecutionHandler策略：java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
         */
        int threadSize = 1;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(threadSize, threadSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), new MyCallerRunsPolicy());
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(pool);
//        pool.execute(()->sleep(4000));
//        pool.execute(()->sleep(4000));

        /**
         * 设置初始值
         */
        System.gc();
        println("父线程-位置0", "最开始 - 剩余空间大小 = " + freeMemory());
        InheritableThreadLocal<byte[]> threadLocal = new TransmittableThreadLocal<>();
        threadLocal.set(new byte[50 * 1024 * 1024]);
        println("父线程-位置1 ", "主线程赋值50m后 - 剩余空间大小 =" + freeMemory());

        /**
         * 此时线程池中的任务是在主线程中执行的，在这里执行remove方法，后面验证主线程的threadLocal是否会被清空调
         */
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ttlExecutorService.submit(() -> {
            String flag = "任务1";
            threadLocal.set(new byte[20 * 1024 * 1024]);
            println(flag, concat("重新赋值20m后，剩余空间大小 = ", freeMemory()));
            countDownLatch.countDown();
        });

        countDownLatch.await();
        sleep(1000);
        System.gc();
        println("父线程-位置2", "gc后剩余空间大小 = " + freeMemory());


        threadLocal.set(new byte[10 * 1024 * 1024]);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        ttlExecutorService.submit(() -> {
            String flag = "任务2";
            println(flag, concat("剩余空间大小 = ", freeMemory()));
            countDownLatch2.countDown();
        });

        countDownLatch2.await();
        sleep(2000);
        threadLocal.remove();
        System.gc();
        println("父线程-位置3", "主线程remove且gc后剩余空间大小 = " + freeMemory());
    }

    /**
     * 背景：当线程池满了且线程池的RejectedExecutionHandler使用的是CallerRunsPolicy时，提交到线程池的任务会在提交线程中直接执行，
     * ThreadLocal.remove操作清理提交线程的上下文导致上下文丢失。
     * <p>
     * 测试场景：TransmittableThreadLocal不会出现这种情况
     * <p>
     * {@link TransmittableThreadLocal}
     * {@link InheritableThreadLocal}
     *
     * @throws InterruptedException
     */
    @Test
    public void testTransmittableThreadLocal() throws InterruptedException {
        /**
         * 创建线程池
         * RejectedExecutionHandler策略：java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
         */
        int threadSize = 1;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(threadSize, threadSize,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), new MyCallerRunsPolicy());
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(pool);

        /**
         * 目的：消耗线程池中的所有线程以及队列，以便后续进来的任务会执行RejectedExecutionHandler策略
         */
        ttlExecutorService.submit(() -> sleep(2 * 1000));
        ttlExecutorService.submit(() -> sleep(2 * 1000));

        /**
         * 设置初始值
         */
        InheritableThreadLocal<U> threadLocal = new TransmittableThreadLocal<>();
        threadLocal.set(new U("初始值"));
        println("父线程-位置1", threadLocal.get());

        /**
         * 此时线程池中的任务是在主线程中执行的，在这里执行remove方法，后面验证主线程的threadLocal是否会被清空调
         */
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ttlExecutorService.submit(() -> {
            String flag = "线程池任务1";
            println(flag, "初始值：" + threadLocal.get());
            threadLocal.get().setRemark("线程池1任务中修改了对象属性值");
            println(flag, concat("修改属性后的值 : ", threadLocal.get()));
            threadLocal.remove();
            println(flag, concat("remove后的值 : ", threadLocal.get()));
            println(flag, "finish");
            countDownLatch.countDown();
        });

        countDownLatch.await();
        sleep(1000);

        /**
         * 这里threadLocal.get()对应： TransmittableThreadLocalTest.U(remark=线程池1任务中修改了对象属性值)
         * 结论如下：
         * 1、线程池中会拿到原始对象，不会做深拷贝，若对原始对象属性做修改，会影响到其他线程
         * 2、在其他线程中调用remove方法，不会对别的线程造成影响
         */
        println("父线程-位置2", threadLocal.get());
    }

    @AllArgsConstructor
    @Data
    @ToString
    private static class U {
        private String remark;
    }

    private static class MyCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            println("rejectedExecution", "触发了RejectedExecutionHandler，交给提交线程去执行任务...");
            super.rejectedExecution(r, e);
        }
    }

    private void sleep(long mills) {
        long start = System.currentTimeMillis();
        long end;
        do {
            end = System.currentTimeMillis();
        } while (end - start <= mills);
    }

    private static void println(String flag, Object msg) {
        Thread t = Thread.currentThread();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String thread = t.getName() + "[" + t.hashCode() + "]";
        String s = wrapperString(time, "thread=" + thread, "flag=" + flag);
        System.out.println(s + " msg -> " + msg);
    }

    private static String wrapperString(String... params) {
        if (params == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (String ele : params) {
            sb.append("【").append(ele).append("】");
        }
        return sb.toString();
    }

    private static String concat(Object... params) {
        if (params == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object ele : params) {
            if (ele != null) {
                sb.append(ele);
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }


}
