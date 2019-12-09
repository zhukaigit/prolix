package socket.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("all")
public class BlockingSocketService {
    //搭建服务器端
    public static void main (String[] args) throws IOException {
        BlockingSocketService socketService = new BlockingSocketService();
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
