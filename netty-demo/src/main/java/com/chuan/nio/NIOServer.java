package com.chuan.nio;

import com.chuan.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chuan
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {


        ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector selector = Selector.open();
        ssc.configureBlocking(false);
        // 绑定事件：ServerSocketChannel只用来做连接
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress("127.0.0.1", 8080));
        while (true) {
            int select = selector.select();
            System.out.println(select);
            // 获取所有事件
            Set<SelectionKey> keys = selector.selectedKeys();
            // 遍历所有事件，逐一处理
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 判断事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel c = (ServerSocketChannel) key.channel();
                    SocketChannel sc = c.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                    int read = sc.read(byteBuffer);
                    if (read > 0) {
                        byteBuffer.flip();
                        ByteBufferUtil.debugRead(byteBuffer);
                        byteBuffer.clear();
                    }
                }
                // 处理完毕，必须将事件移除
                iter.remove();
            }
        }
    }
}
