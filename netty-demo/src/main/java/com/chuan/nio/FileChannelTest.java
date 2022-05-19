package com.chuan.nio;

import com.chuan.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chuan
 *
 * - 向 buffer 写入数据，例如调用 channel.read(buffer)
 * - 调用 flip() 切换至**读模式**
 * - 从 buffer 读取数据，例如调用 buffer.get()
 * - 调用 clear() 或 compact() 切换至**写模式
 */
@Slf4j
public class FileChannelTest {
    public static void main(String[] args) {

        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugRead(buffer1);


        try (RandomAccessFile file = new RandomAccessFile("C:\\IdeaProject\\chuan\\netty\\netty-demo\\src\\main\\java\\data.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(20);
            do {
                // 向 buffer 写入
                int len = channel.read(buffer);
                log.debug("读到字节数：{}", len);
                if (len == -1) {
                    break;
                }
                // 切换 buffer 读模式
                buffer.flip();
                while(buffer.hasRemaining()) {
                    log.debug("{}", (char)buffer.get());
                }
                // 切换 buffer 写模式
                buffer.clear();
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
