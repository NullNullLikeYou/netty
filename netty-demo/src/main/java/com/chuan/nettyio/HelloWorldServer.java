package com.chuan.nettyio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author chuan
 */
public class HelloWorldServer {
    public static void main(String[] args) {
        // 创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
        new ServerBootstrap().group(new NioEventLoopGroup())
                // 选择服务 Scoket 实现类，其中 NioServerSocketChannel 表示基于 NIO 的服务器端实现
                .channel(NioServerSocketChannel.class)
                // 为啥方法叫 childHandler，是接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel
                // ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // SocketChannel 的处理器，解码 ByteBuf => String
                        ch.pipeline().addLast(new StringDecoder());
                        // SocketChannel 的业务处理器，使用上一个处理器的处理结果
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // ServerSocketChannel 绑定的监听端口
                .bind(8080);
    }
}
