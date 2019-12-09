package socket.nonblocking;

import org.apache.commons.lang3.StringUtils;
import socket.SocketTool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("all")
public class NonBlockingSocketService {
    //搭建服务器端
    public static void main (String[] args) throws IOException {
        NonBlockingSocketService socketService = new NonBlockingSocketService();
        //1、a)创建一个服务器端Socket，即SocketService
        socketService.oneServer();
    }

    public void oneServer () {
        ServerSocketChannel server = null;
        SocketChannel socket = null;
        try {
            // 1、指定绑定的端口，并监听此端口。
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(9898));
            // 2、设置成非阻塞
            server.configureBlocking(false);
            System.out.println("================ 服务器启动成功 =================");

            Selector selector = Selector.open();
            // server注册接收监听事件
            server.register(selector, SelectionKey.OP_ACCEPT);

            // 轮询是否有新的事件，若没有则主线程在这等待
            while (selector.select() > 0) {// 阻塞方法
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                // 去做此时监听到的所有事件
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        // 接收客户端的连接
                        socket = server.accept();
                        socket.configureBlocking(false);
                        System.out.println("================ 客户端已连接 =================");

                        // 注入可读事件
                        socket.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE);
                    } else if (key.isReadable()) {
                        try {
                            socket = (SocketChannel) key.channel();
                            ByteBuffer buf = ByteBuffer.allocate(2048);

                            // 读取客户端的请求数据，catch异常的原因见：备注1
                            StringBuilder clientMsgBuilder = new StringBuilder();
                            int length = 0;
                            while ((length = socket.read(buf)) > 0) {
                                buf.flip();
                                String content = new String(buf.array(), 0, length, SocketTool.UTF8);
                                if (StringUtils.isNotBlank(content)) {
                                    clientMsgBuilder.append(content);
                                }
                                buf.clear();
                            }
                            String clientMsg = clientMsgBuilder.toString();

                            // 返回客户端信息
                            System.out.println("【收到客户端的消息】：" + clientMsg);
                            System.out.println("服务器处理.............");// 处理
                            String resp = String.format("处理“%s”的结果", clientMsg);// 处理结果
                            System.out.println("处理结果：" + resp);
                            buf.clear();
                            buf.put(resp.getBytes(SocketTool.UTF8));
                            buf.flip();
                            socket.write(buf);
                            buf.clear();
                        } catch (IOException e) {
                            System.out.println("************ " + e.getMessage() + " ***********");
                            key.cancel();
                            socket.socket().close();
                            socket.close();
                        }
                    }
                }
                // 删除已处理事件
                keyIterator.remove();
            }
        } catch (Exception e) {//出错，打印出错信息
            e.printStackTrace();
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


//备注1:Java NIO聊天室 中，若客户端强制关闭，服务器会报“Java.io.IOException: 远程主机强迫关闭了一个现有的连接。”，并且服务器会在报错后停止运行，错误的意思就是客户端关闭了，但是服务器还在从这个套接字通道读取数据，便抛出IOException，导致这种情况出现的原因就是，客户端异常关闭后，服务器的选择器会获取到与客户端套接字对应的套接字通道SelectionKey，并且这个key的兴趣是OP_READ，执行从这个通道读取数据时，客户端已套接字已关闭，所以会出现“java.io.IOException: 远程主机强迫关闭了一个现有的连接”的错误。解决这种问题也很简单，就是服务器在读取数据时，若发生异常，则取消当前key并关闭通道