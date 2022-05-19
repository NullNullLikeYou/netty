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
 * @author chuan
 */
@Slf4j
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        // 创建连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();
            log.debug("connected... {}", sc);
            channels.add(sc);
            for (SocketChannel channel : channels) {
                // 拿到客户端连接
                // 接收客户端发送的数据
                log.debug("before read... {}", channel);
                // 阻塞读sc并且往buffer写数据
                sc.read(buffer);
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read...{}", channel);
            }
        }
    }
}
