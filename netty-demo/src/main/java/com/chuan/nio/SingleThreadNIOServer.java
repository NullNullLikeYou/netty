package com.chuan.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.chuan.ByteBufferUtil.debugRead;

/**
 * 非阻塞模式下，相关方法都会不会让线程暂停
 * - 在 ServerSocketChannel.accept 在没有连接建立时，会返回 null，继续运行
 * - SocketChannel.read 在没有数据可读时，会返回 0，但线程不必阻塞，可以去执行其它 SocketChannel 的 read 或是去执行 ServerSocketChannel.accept
 * - - 写数据时，线程只是等待数据写入 Channel 即可，无需等 Channel 通过网络把数据发送出去
 * - 但非阻塞模式下，即使没有连接建立，和可读数据，线程仍然在不断运行，白白浪费了 cpu
 * - 数据复制过程中，线程实际还是阻塞的（AIO 改进的地方）
 *
 * @author chuan
 */
@Slf4j
public class SingleThreadNIOServer {
    public static void main(String[] args) throws IOException {
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 非阻塞模式
        ssc.configureBlocking(false);
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            // 非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected... {}", sc);
                // 非阻塞模式
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 5. 接收客户端发送的数据
                // 非阻塞，线程仍然会继续运行，如果没有读到数据，read 返回 0
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", channel);
                }
            }
        }
    }
}
