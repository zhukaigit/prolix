package socket.blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.Socket;

@SuppressWarnings("all")
public class BlockingSocketClient2 {
    // 搭建客户端
    public static void main (String[] args) throws IOException {
        try {
            // 1、创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("127.0.0.1", 9898);
            System.out.println("================ 客户端启动成功 =================");

            // 2、与服务端交互
            PrintWriter write = new PrintWriter(socket.getOutputStream());
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("================ 请在下方输入消息，按entry发送 =================");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String writeToServer = String.format("[%s] %s",
                    MethodHandles.lookup().lookupClass().getSimpleName(), br.readLine());
            // 从系统标准输入读入一字符串
            while (true) {
                // 发送报文给服务端
                write.println(writeToServer);
                write.flush();
                System.out.println("================ 等待服务端的返回 =================");
                System.out.println("【服务器返回】: " + socketInput.readLine());
                if ("end".equalsIgnoreCase(writeToServer)) {
                    break;
                }
                // 客户端持续请求
                System.out.println("================ 请在下方输入消息，按entry发送 =================");
                writeToServer = String.format("[%s] %s",
                        MethodHandles.lookup().lookupClass().getSimpleName(), br.readLine());
            }
            //4、关闭资源
            socket.shutdownOutput();
            write.close(); // 关闭Socket输出流
            socket.close(); // 关闭Socket
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }
}
