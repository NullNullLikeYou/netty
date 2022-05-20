package com.chuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static com.chuan.ByteBufferUtil.debugRead;

/**
 * @author chuan
 */
public class UDPServer {
    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress(9999));
            System.out.println("waiting...");
            ByteBuffer buffer = ByteBuffer.allocate(32);
            channel.receive(buffer);
            buffer.flip();
            debugRead(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
