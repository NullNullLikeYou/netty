package com.chuan.nettyio.project.server;

import com.chuan.nettyio.project.enums.HandlerEnum;
import com.chuan.nettyio.project.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class RpcServer {
    public static void main(String[] args) {
        // 用于处理accept事件，只做连接
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        // 用于处理IO事件，读写消息
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 创建服务端启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 给ChannelFactory赋值 NioServerSocketChannel 类型，只做赋值，没有实例化
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 赋值父类中的EventLoopGroup为boss，当前类childGroup为worker
            serverBootstrap.group(boss, worker);
            // 绑定childHandler
            serverBootstrap.childHandler(new RpcChannelInitializer());
            serverBootstrap.bind(8080).sync()
                    .channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            log.error("server error", e);
        }
        finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private static class RpcChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtocolFrameDecoder());
            ch.pipeline().addLast(HandlerEnum.LOGGING_HANDLER.getSupplier().get());
            ch.pipeline().addLast(HandlerEnum.MESSAGE_CODEC.getSupplier().get());
            ch.pipeline().addLast(HandlerEnum.RPC_HANDLER.getSupplier().get());
        }
    }
}
