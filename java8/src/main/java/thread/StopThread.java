package thread;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.client.RedisClient;

import java.util.concurrent.Semaphore;

public class StopThread {
    private static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Redisson redisson = null;
        RedisClient redisClient;
        RLock lock = redisson.getLock("");
        lock.lock();
    }

    public static void main1(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        semaphore.acquire();
        semaphore.acquire(1);

        semaphore.release();
        semaphore.release(1);

        semaphore.tryAcquire(1);


    }

    private static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
