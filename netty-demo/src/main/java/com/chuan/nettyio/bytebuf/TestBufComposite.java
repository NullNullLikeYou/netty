package com.chuan.nettyio.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.logging.LoggingHandler;

import static com.chuan.ByteBufferUtil.debugRead;

/**
 * 由于 Netty 中有堆外内存的 ByteBuf 实现，堆外内存最好是手动来释放，而不是等 GC 垃圾回收。
 * UnpooledHeapByteBuf 使用的是 JVM 内存，只需等 GC 回收内存即可
 * UnpooledDirectByteBuf 使用的就是直接内存了，需要特殊的方法来回收内存
 * PooledByteBuf 和它的子类使用了池化机制，需要更复杂的规则来回收内存
 * <p>
 * Netty 这里采用了引用计数法来控制回收内存，每个 ByteBuf 都实现了 ReferenceCounted 接口
 * 每个 ByteBuf 对象的初始计数为 1
 * 调用 release 方法计数减 1，如果计数为 0，ByteBuf 内存被回收
 * 调用 retain 方法计数加 1，表示调用者没用完之前，其它 handler 即使调用了 release 也不会造成回收
 * 当计数为 0 时，底层内存会被回收，这时即使 ByteBuf 对象还在，其各个方法均无法正常使用
 * <p>
 * 因为 pipeline 的存在，一般需要将 ByteBuf 传递给下一个 ChannelHandler
 * 基本规则是，**谁是最后使用者，谁负责 release**
 * AbstractNioByteChannel.NioByteUnsafe#read 方法中首次创建 ByteBuf 放入 pipeline
 * 入站 ByteBuf 处理原则
 * -- 对原始 ByteBuf 不做处理，调用 ctx.fireChannelRead(msg) 向后传递，这时无须 release
 * -- 将原始 ByteBuf 转换为其它类型的 Java 对象，这时 ByteBuf 就没用了，必须 release
 * -- 如果不调用 ctx.fireChannelRead(msg) 向后传递，那么也必须 release
 * -- 注意各种异常，如果 ByteBuf 没有成功传递到下一个 ChannelHandler，必须 release
 * -- 假设消息一直向后传，那么 TailContext 会负责释放未处理消息（原始的 ByteBuf）
 *
 * 出站 ByteBuf 处理原则
 * -- 出站消息最终都会转为 ByteBuf 输出，一直向前传，由 HeadContext flush 后 release
 * 异常处理原则
 * -- 有时候不清楚 ByteBuf 被引用了多少次，但又必须彻底释放，可以循环调用 release 直到返回 true
 *
 * @author Jonathan
 */
public class TestBufComposite {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        // read index:0 write index:4 capacity:10
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        // 扩容规则是
        // 写入后数据大小未超过 512，则选择下一个 16 的整数倍，例如写入后大小为 12 ，则扩容后 capacity 是 16
        // * 写入后数据大小超过 512，则选择下一个 2^n，例如写入后大小为 513，则扩容后 capacity 是 2^10=1024（2^9=512 已经不够了）

        // 每次一个字节,读过的内容，就属于废弃部分了
        // get 开头的一系列方法，这些方法不会改变 read index
        buffer.readByte();
        ByteBufUtil.prettyHexDump(buffer);
    }
}
