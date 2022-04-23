package socket.chat;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class BlockingSocketClient {
    // 搭建客户端
    public static void main (String[] args) throws IOException {
        try {
            ExecutorService pool = Executors.newFixedThreadPool(2);

            // 1、创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("127.0.0.1", 9898);
            System.out.println("================ 客户端启动成功 =================");

            pool.execute(() -> BlockingSocketService.write(socket, "client1"));
            pool.execute(() -> BlockingSocketService.read(socket));

            pool.shutdown();
            boolean terminated = pool.isTerminated();
            while (!terminated) {
                terminated = pool.awaitTermination(2000, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }


}
