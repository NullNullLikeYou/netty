package com.chuan.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.chuan.ByteBufferUtil.debugAll;

/**
 * @author Jonathan
 */
@Slf4j
public class MultiplexMsgLimitClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        sc.write(StandardCharsets.UTF_8.encode("hello\nworld\n"));
        sc.write(StandardCharsets.UTF_8.encode("0123\n456789abcdef"));
        sc.write(StandardCharsets.UTF_8.encode("0123456789abcdef3333\n"));
        System.in.read();
    }
}
