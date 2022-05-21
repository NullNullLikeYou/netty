package com.chuan.nettyio.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 每个Channel绑定了唯一的NioEventLoopGroup，channel过程中的handler 可以使用自定义handler
 * 如果使用自定义的NioEventLoopGroup，也会进行一一绑定
 *
 * 关键代码 `io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead()`
 * 如果两个 handler 绑定的是同一个线程，那么就直接调用
 * 否则，把要调用的代码封装为一个任务对象，由下一个 handler 的线程来调用
 *
 * @author Jonathan
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) throws InterruptedException {

        // 自定义NioEventLoopGroup
        DefaultEventExecutorGroup normalWorkers = new DefaultEventExecutorGroup(2);
        new ServerBootstrap().group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
//                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                ByteBuf byteBuf = msg instanceof ByteBuf ? ((ByteBuf) msg) : null;
//                                if (byteBuf != null) {
//                                    byte[] buf = new byte[16];
//                                    ByteBuf len = byteBuf.readBytes(buf, 0, byteBuf.readableBytes());
//                                    log.debug(new String(buf));
//                                }
//                            }
//                        });
                        // 使用自定义EventLoop处理handler
                        ch.pipeline().addLast(normalWorkers, new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = msg instanceof ByteBuf ? ((ByteBuf) msg) : null;
                                if (byteBuf != null) {
                                    byte[] buf = new byte[16];
                                    ByteBuf len = byteBuf.readBytes(buf, 0, byteBuf.readableBytes());
                                    log.debug(new String(buf));
                                }
                            }
                        });
                    }
                })
                .bind(8080).sync();
    }
}
