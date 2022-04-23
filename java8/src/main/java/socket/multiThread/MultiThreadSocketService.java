package socket.multiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用多线程方式处理各个请求的阻塞现象
 * 注意：代码每执行一次server.accept()，只能跟一个客户端建立连接，这里server.accept()在循环中，所以可以接受多个客户端的连接
 */
@SuppressWarnings("all")
public class MultiThreadSocketService {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    //搭建服务器端
    public static void main (String[] args) throws IOException {

        MultiThreadSocketService socketService = new MultiThreadSocketService();
        //1、a)创建一个服务器端Socket，即SocketService
        socketService.oneServer();
    }

    public void oneServer () {
        ServerSocket server = null;
        Socket socket = null;
        try {
            // 1、指定绑定的端口，并监听此端口。
            server = new ServerSocket(9898);
            System.out.println("================ 服务器启动成功 =================");
            while (true) {
                System.out.println("================ 阻塞等待客户端的连接 =================");
                socket = server.accept();// 为阻塞方法
                System.out.println("*********************** 【客户端已连接】 ***********************");

                threadPool.execute(new SocketProcess(socket));
            }
        } catch (Exception e) {//出错，打印出错信息
            System.out.println("Error." + e);
        } finally {
            try {
                assert socket != null;
                socket.close(); //关闭Socket
                assert server != null;
                server.close(); //关闭ServerSocket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class SocketProcess extends Thread {
        private Socket socket;

        public SocketProcess (Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run () {
            super.run();
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                // 2、与客户端交互
                BufferedReader clientInfoReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("================ 等待客户端的消息 =================");
                String clientInfo = clientInfoReader.readLine();
                while (true) {
                    // 返回信息给客户端
                    System.out.println("【收到客户端的消息】：" + clientInfo);// 入参
                    System.out.println("服务器处理.............");// 处理
                    String resp = String.format("处理“%s”的结果", clientInfo);// 处理结果
                    System.out.println("处理结果：" + resp);
                    writer.println(resp);// 返回给客户端
                    writer.flush();
                    if ("end".equalsIgnoreCase(clientInfo)) {
                        break;
                    }
                    // 持续接收客户端信息
                    System.out.println("================ 等待客户端的消息 =================");
                    clientInfo = clientInfoReader.readLine();
                }

                //5、关闭资源
                writer.close(); //关闭Socket输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
