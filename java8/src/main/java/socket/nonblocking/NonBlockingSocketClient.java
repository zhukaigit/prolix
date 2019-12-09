package socket.nonblocking;

import org.apache.commons.lang3.StringUtils;
import socket.SocketTool;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

@SuppressWarnings("all")
public class NonBlockingSocketClient {
    // 搭建客户端
    public static void main (String[] args) throws IOException {
        try {
            // 1、创建客户端Socket，指定服务器地址和端口
            SocketChannel socket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            socket.configureBlocking(false);
            System.out.println("================ 客户端启动成功 =================");

            Selector selector = Selector.open();
            socket.register(selector, SelectionKey.OP_READ);

            // 2、与服务端交互
            Scanner scanner = new Scanner(System.in);
            System.out.println("================ 请在下方输入消息，按entry发送 =================");
            String readLine = scanner.nextLine();
            ByteBuffer buf = ByteBuffer.allocate(2048);

            // 发送报文给服务端
            String sendMsg = String.format("[%s] %s",
                    MethodHandles.lookup().lookupClass().getSimpleName(), readLine);
            buf.put(sendMsg.getBytes());
            buf.flip();
            socket.write(buf);
            buf.clear();

            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        socket = (SocketChannel) key.channel();

                        // 接收服务端返回消息
                        System.out.println("================ 等待服务端的返回 =================");
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        StringBuilder serverResp = new StringBuilder();
                        int length = 0;
                        while ((length = socket.read(buffer)) > 0) {
                            buffer.flip();
                            String content = new String(buffer.array(), 0, length, SocketTool.UTF8);
                            if (StringUtils.isNotBlank(content)) {
                                serverResp.append(content);
                            }
                            buffer.clear();
                        }
                        String clientMsg = serverResp.toString();
                        System.out.println("【服务器返回】: " + serverResp);
                        if ("end".equalsIgnoreCase(readLine)) {
                            break;
                        }

                        // 客户端持续请求服务端
                        System.out.println("================ 请在下方输入消息，按entry发送 =================");
                        readLine = scanner.nextLine();
                        // 发送报文给服务端
                        sendMsg = String.format("[%s] %s",
                                MethodHandles.lookup().lookupClass().getSimpleName(), readLine);
                        buf.put(sendMsg.getBytes());
                        buf.flip();
                        socket.write(buf);
                        buf.clear();
                    }
                }
                iterator.remove();
            }
            //4、关闭资源
            socket.shutdownOutput();
            socket.close(); // 关闭Socket
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
