package com.chuan.nettyio.channel;

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
 * channel 的主要作用
 * * close() 可以用来关闭 channel
 * * closeFuture() 用来处理 channel 的关闭
 *   * sync 方法作用是同步等待 channel 关闭
 *   * 而 addListener 方法是异步等待 channel 关闭
 * * pipeline() 方法添加处理器
 * * write() 方法将数据写入
 * * writeAndFlush() 方法将数据写入并刷出
 *
 *
 * @author Jonathan
 */
@Slf4j
public class ChannelServer {
    public static void main(String[] args) throws InterruptedException {
        new ServerBootstrap().group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
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
