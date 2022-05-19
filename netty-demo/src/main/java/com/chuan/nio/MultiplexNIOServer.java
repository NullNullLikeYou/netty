package com.chuan.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.chuan.ByteBufferUtil.debugRead;

/**
 * 单线程可以配合 Selector 完成对多个 Channel 可读写事件的监控，这称之为多路复用
 * 如果不用 Selector 的非阻塞模式，线程大部分时间都在做无用功，而 Selector 能够保证
 * - 有可连接事件时才去连接
 * - 有可读事件才去读取
 * - 有可写事件才去写入
 * - 限于网络传输能力，Channel 未必时时可写，一旦 Channel 可写，会触发 Selector 的可写事件
 * <p>
 * - 一个线程配合 selector 就可以监控多个 channel 的事件，事件发生线程才去处理。避免非阻塞模式下所做无用功
 * - 让这个线程能够被充分利用
 * - 节约了线程的数量
 * - 减少了线程上下文切换
 *
 * select 何时不阻塞
 * 事件发生时
 * - 客户端发起连接请求，会触发 accept 事件
 * - 客户端发送数据过来，客户端正常、异常关闭时，都会触发 read 事件，另外如果发送的数据大于 buffer 缓冲区，会触发多次读取事件
 * - channel 可写，会触发 write 事件
 * - - 在 linux 下 nio bug 发生时
 * - 调用 selector.wakeup()
 * - 调用 selector.close()
 * - selector 所在线程 interrupt
 *
 * 处理消息的边界
 * 固定消息长度，数据包大小一样，服务器按预定长度读取，缺点是浪费带宽
 * 按分隔符拆分，缺点是效率低
 * TLV 格式，即 Type 类型、Length 长度、Value 数据，类型和长度已知的情况下，就可以方便获取消息大小，
 * 分配合适的 buffer，缺点是 buffer 需要提前分配，如果内容过大，则影响 server 吞吐量
 *
 * @author chuan
 */
@Slf4j
public class MultiplexNIOServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 非阻塞模式
        ssc.configureBlocking(false);
        // 创建selector
        Selector selector = Selector.open();
        // 绑定channel事件(如果是注册事件,则只关心连接)
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            int select = selector.select();
            log.debug("select count: {}", select);
            // 获取所有事件
            Set<SelectionKey> keys = selector.selectedKeys();
            // 遍历所有事件，逐一处理
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 判断事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel c = (ServerSocketChannel) key.channel();
                    // 必须处理!事件发生后，要么处理，要么取消（cancel），不能什么都不做，否则下次该事件仍会触发，这是因为 nio 底层使用的是水平触发
                    SocketChannel sc = c.accept();
                    log.debug("连接建立:{}", sc);
                    // 注册读事件
                    // 非阻塞模式
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(3);
                    // 读取sc并写入到buffer中
                    int read = sc.read(buffer);
                    if (read == -1) {
                        // cancel 会取消注册在 selector 上的 channel，并从 keys 集合中删除 key 后续不会再监听事件
                        key.cancel();
                        // 关闭此sc
                        sc.close();
                    }
                    else {
                        buffer.flip();
                        debugRead(buffer);
                    }

                }
                // 处理完毕，必须将事件移除
                iter.remove();
            }
//            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
//            // 非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
//            SocketChannel sc = ssc.accept();
//            if (sc != null) {
//                log.debug("connected... {}", sc);
//                // 非阻塞模式
//                sc.configureBlocking(false);
//                channels.add(sc);
//            }
//            for (SocketChannel channel : channels) {
//                // 5. 接收客户端发送的数据
//                // 非阻塞，线程仍然会继续运行，如果没有读到数据，read 返回 0
//                int read = channel.read(buffer);
//                if (read > 0) {
//                    buffer.flip();
//                    debugRead(buffer);
//                    buffer.clear();
//                    log.debug("after read...{}", channel);
//                }
//            }
        }
    }
}
