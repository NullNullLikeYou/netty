package com.chuan.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author chuan
 * <p>
 * ServerSocketChannel.accept 会在没有连接建立时让线程暂停
 * - SocketChannel.read 会在没有数据可读时让线程暂停
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            byte[] bytes = new byte[1];
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            inputStream.read(bytes);
            System.out.println(Arrays.toString(bytes));
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(2);
            outputStream.flush();
        }
    }
}
