package socket.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class BlockingSocketService {

    //搭建服务器端
    public static void main (String[] args) throws IOException {
        BlockingSocketService socketService = new BlockingSocketService();
        //1、a)创建一个服务器端Socket，即SocketService
        socketService.oneServer();

    }

    /**
     * 写入信息到socket，阻塞（等待命令端的文本输入）
     * @param socket
     */
    public static void write(Socket socket, String name) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String readLine = br.readLine();
                writer.println(name + "：" + readLine);// 返回给客户端
                writer.flush();
                if (br.equals("end")) {
                    break;
                }
                br = new BufferedReader(new InputStreamReader(System.in));
            }
            writer.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从socket中读取信息，阻塞
     * @param socket
     */
    public static void read(Socket socket) {
        try {
            BufferedReader clientInfoReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientInfo = clientInfoReader.readLine();
            while (true) {
                // 返回信息给客户端
                System.out.println("【收到的消息】" + clientInfo);// 入参
                if ("end".equalsIgnoreCase(clientInfo)) {
                    break;
                }
                // 持续接收客户端信息
//                System.out.println("================ 等待客户端的消息 =================");
                clientInfo = clientInfoReader.readLine();
            }
            clientInfoReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void oneServer () {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        ServerSocket server = null;
        Socket socket = null;
        try {
            // 1、指定绑定的端口，并监听此端口。
            server = new ServerSocket(9898);
            System.out.println("================ 服务器启动成功 =================");


            System.out.println("================ 阻塞等待客户端的连接 =================");
            socket = server.accept();// 为阻塞方法
            System.out.println("*********************** 【客户端已连接】 ***********************");

            Socket finalSocket = socket;
            pool.execute(() -> write(finalSocket, "server"));
            pool.execute(() -> read(finalSocket));

            pool.shutdown();
            boolean terminated = pool.isTerminated();
            while (!terminated) {
                terminated = pool.awaitTermination(2000, TimeUnit.SECONDS);
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
}
