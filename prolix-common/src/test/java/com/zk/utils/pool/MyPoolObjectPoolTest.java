package com.zk.utils.pool;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPoolObjectPoolTest {



    @Test
    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        MyPoolObjectPool pool = new MyPoolObjectPool(new MyPoolObjectFactory());
        for (int i = 0; i < 8; i++) {
            int finalI = i;
            executorService.execute(()->{
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject + ", i =" + finalI);
                sleep(10000);
                pool.returnObject(myPoolObject);
            });
            sleep(2000);
        }

        sleep(10000);
        System.out.println(StringUtils.center("分隔线", 70, "-"));

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            executorService.execute(()->{
                MyPoolObject myPoolObject = pool.borrowObject();
                System.out.println("获取到的object=" + myPoolObject + ", i =" + finalI);
                sleep(10000);
                pool.returnObject(myPoolObject);
            });
            sleep(2000);
        }
    }


    private void sleep (long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
