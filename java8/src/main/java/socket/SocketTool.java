package socket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SocketTool {

    public static final Charset UTF8 = Charset.forName("utf-8");

    public static void main (String[] args) {

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("你好".getBytes());



        buf.flip();
        System.out.println(new String(buf.array(), 0, buf.remaining()));
    }


}
