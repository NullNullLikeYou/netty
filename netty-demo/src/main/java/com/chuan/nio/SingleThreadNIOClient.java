package com.chuan.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.chuan.ByteBufferUtil.debugRead;

/**
 * @author chuan
 */
@Slf4j
public class SingleThreadNIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
//        while (true) {
            sc.write(StandardCharsets.UTF_8.encode("hello"));
            System.out.println("waiting...");
//        }
        System.in.read();
    }
}
