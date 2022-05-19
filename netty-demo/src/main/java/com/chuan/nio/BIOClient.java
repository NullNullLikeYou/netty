package com.chuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author chuan
 */
public class BIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        while (true) {
            sc.write(StandardCharsets.UTF_8.encode("hello"));
            System.out.println("waiting...");
        }
    }
}
