package com.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TomcatThreadPoolTest {

    public static void main(String[] args) {
        TaskQueue taskQueue = new TaskQueue(10);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 5, 300, TimeUnit.SECONDS, taskQueue, new TaskThreadFactory("zk-pool-", false, 5), new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        taskQueue.setParent(pool);

        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            pool.execute(()-> {
                try {
                    System.out.println("入参：" + finalI);
                    Thread.sleep(20000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
