package com.chuan.nettyio.server;

import com.chuan.nettyio.client.inf.HelloService;
import com.chuan.nettyio.enums.HandlerEnum;
import com.chuan.nettyio.message.RpcRequestMessage;
import com.chuan.nettyio.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new RpcChannelInitializer());
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();

            channel.writeAndFlush(new RpcRequestMessage(
                    1,
                    HelloService.class.getName(),
                    "sayHello",
                    String.class,
                    new Class[]{String.class},
                    new Object[]{"张三"}
            )).addListener(promise -> {
                if (!promise.isSuccess()) {
                    Throwable cause = promise.cause();
                    log.error("error", cause);
                }
            });

            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
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
