package com.chuan.nettyio.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 池化功能是否开启，可以通过下面的系统环境变量来设置
 * <p>
 * -Dio.netty.allocator.type={unpooled|pooled}4.1之后默认开启
 */
public class TestDirectBuf {
    public static void main(String[] args) {
        // 池化基于直接内存的 ByteBuf
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer(10);
        // 创建池化基于堆的 ByteBuf
        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.heapBuffer(10);
        // 池化基于直接内存的 ByteBuf
        ByteBuf buffer3 = ByteBufAllocator.DEFAULT.directBuffer(10);
    }
}
