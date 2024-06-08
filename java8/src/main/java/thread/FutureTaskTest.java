package thread;

import java.util.concurrent.*;

public class FutureTaskTest {

    public static void main(String[] args) throws Exception {

        Task task = new Task();

        //测试FutureTask的阻塞获取结果
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);

        Thread thread = new Thread(integerFutureTask);
        thread.start();

        FutureTaskTools.callBack(integerFutureTask, integer -> {
            System.out.println("执行结果:" + integer);
            return integer;
        });

        System.out.println("执行完成");
    }

}

/**
 * 处理异步回调的工具类
 */
class FutureTaskTools {

    /**
     * futureTask的阻塞任务进行异步处理,丢到线程池中执行
     *
     * @param futureTask
     * @param futureTaskCallBack
     */
    public static void callBack(FutureTask futureTask, FutureTaskCallBack<Integer> futureTaskCallBack) {
        new Thread(new FutureResultTask(futureTaskCallBack, futureTask)).start();
    }


    /**
     * 1.将futureTask的get阻塞方法封装为一个异步任务
     * 2.调用get成功之后，使用回调函数发生回调
     *
     * @param <T>
     */
    private static final class FutureResultTask<T> implements Runnable {

        private FutureTaskCallBack<T> futureTaskCallBack;

        private FutureTask<T> futureTask;

        public FutureResultTask(FutureTaskCallBack<T> futureTaskCallBack, FutureTask<T> futureTask) {
            this.futureTaskCallBack = futureTaskCallBack;
            this.futureTask = futureTask;
        }

        @Override
        public void run() {
            try {
                //阻塞等待执行结果
                T t = futureTask.get();
                //回调，将结果反馈到主线程中
                futureTaskCallBack.executeWithResult(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * /事件回调接口，异步任务执行完后回调的类型
 *
 * @param <T>
 */
interface FutureTaskCallBack<T> {

    T executeWithResult(T t);

}

/**
 * 待执行的任务
 */
class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(10000);
        return 100;
    }
}
