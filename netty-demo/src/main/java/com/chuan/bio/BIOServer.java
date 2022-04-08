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
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[1024];
        ServerSocket serverSocket = new ServerSocket(8080);
        ServerSocketChannel ssc = serverSocket.getChannel();
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);
        System.out.println(Arrays.toString(bytes));
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(2);
        outputStream.flush();
        while (true) {

        }
    }
}
