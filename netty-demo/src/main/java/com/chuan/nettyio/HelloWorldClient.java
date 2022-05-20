package com.chuan.nettyio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author chuan
 */
public class HelloWorldClient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                // 1 创建 NioEventLoopGroup，同 Server
                .group(new NioEventLoopGroup())
                // 2 选择客户 Socket 实现类
                .channel(NioSocketChannel.class)
                // 3 添加 SocketChannel 的处理器，ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 8 消息会经过通道 handler 处理，这里是将 String => ByteBuf 发出
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 4 指定要连接的服务器和端口
                .connect(new InetSocketAddress(8080))
                // 5 Netty 中很多方法都是异步的，如 connect，这时需要使用 sync 方法等待 connect 建立连接完毕
                .sync()
                // 6 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
                .channel()
                // 7 写入消息并清空缓冲区
                .writeAndFlush(new Date() + ": hello world!");
    }
}
