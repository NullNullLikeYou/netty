package com.chuan.nettyio.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;

/**
 * 【零拷贝】的体现之一，对原始 ByteBuf 进行切片成多个 ByteBuf，切片后的 ByteBuf 并没有发生内存复制，
 * 还是使用原始 ByteBuf 的内存，切片后的 ByteBuf 维护独立的 read，write 指针
 * <p>
 * duplicate
 * 零拷贝】的体现之一，就好比截取了原始 ByteBuf 所有内容，并且没有 max capacity 的限制，也是与原始 ByteBuf 使用同一块底层内存，只是读写指针是独立的
 * copy
 * 会将底层内存数据进行深拷贝，因此无论读写，都与原始 ByteBuf 无关
 * CompositeByteBuf
 * 【零拷贝】的体现之一，可以将多个 ByteBuf 合并为一个逻辑上的 ByteBuf，避免拷贝
 * <p>
 * #### 💡 ByteBuf 优势
 * -- 池化 - 可以重用池中 ByteBuf 实例，更节约内存，减少内存溢出的可能
 * -- 读写指针分离，不需要像 ByteBuffer 一样切换读写模式
 * -- 可以自动扩容
 * -- 支持链式调用，使用更流畅
 * -- 很多地方体现零拷贝，例如 slice、duplicate、CompositeByteBuf
 *
 * @author Jonathan
 */
public class TestBufSlice {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1, 2, 3, 4});
        origin.readByte();
        System.out.println(ByteBufUtil.prettyHexDump(origin));
        // 这时调用 slice 进行切片，无参 slice 是从原始 ByteBuf 的 read index 到 write index 之间的内容进行切片，
        // 切片后的 max capacity 被固定为这个区间的大小，因此不能追加 write
        ByteBuf slice = origin.slice();
        System.out.println(ByteBufUtil.prettyHexDump(slice));
        // 如果执行，会报 IndexOutOfBoundsException 异常
        // slice.writeByte(5);
        // 如果原始 ByteBuf 再次读操作（又读了一个字节）
        origin.readByte();
        System.out.println(ByteBufUtil.prettyHexDump(origin));
        // 这时的 slice 不受影响，因为它有独立的读写指针
        System.out.println(ByteBufUtil.prettyHexDump(slice));
        // 修改原始或者slice的 ByteBuf 都会受影响，因为底层都是同一块内存
        origin.setByte(2, 5);
        System.out.println(ByteBufUtil.prettyHexDump(origin));
        System.out.println(ByteBufUtil.prettyHexDump(slice));

        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});
        System.out.println(ByteBufUtil.prettyHexDump(buf1));
        System.out.println(ByteBufUtil.prettyHexDump(buf2));

        // 进行了数据的内存复制操作
        ByteBuf buf3 = ByteBufAllocator.DEFAULT
                .buffer(buf1.readableBytes() + buf2.readableBytes());
        buf3.writeBytes(buf1);
        buf3.writeBytes(buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf3));

        // 方法2：
        // CompositeByteBuf 是一个组合的 ByteBuf，它内部维护了一个 Component 数组，
        // 每个 Component 管理一个 ByteBuf，记录了这个 ByteBuf 相对于整体偏移量等信息，代表着整体中某一段的数据。
        CompositeByteBuf buf4 = ByteBufAllocator.DEFAULT.compositeBuffer();
        // true 表示增加新的 ByteBuf 自动递增 write index, 否则 write index 会始终为 0
        buf4.addComponents(true, buf1, buf2);
    }
}
