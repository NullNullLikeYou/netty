package com.chuan.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {


        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));

        OutputStream outputStream = sc.socket().getOutputStream();
        outputStream.write("HelloWorld".getBytes());
        outputStream.flush();
        System.in.read();

    }
}
