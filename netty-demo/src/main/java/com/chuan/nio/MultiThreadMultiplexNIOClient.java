package com.chuan.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chuan.ByteBufferUtil.debugAll;

/**
 * 分两组选择器
 * <p>
 * * 单线程配一个选择器，专门处理 accept 事件
 * * 创建 cpu 核心数的线程，每个线程配一个选择器，轮流处理 read 事件
 *
 * @author Jonathan
 */
public class MultiThreadMultiplexNIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        sc.write(StandardCharsets.UTF_8.encode("helloworld"));
        System.in.read();
    }
}
