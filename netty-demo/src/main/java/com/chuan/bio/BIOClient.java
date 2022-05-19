package com.chuan.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author chuan
 */
public class BIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        byte[] bytes = new byte[1];
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 8080));
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(123);
        outputStream.flush();
        Thread.sleep(1000);
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);
        System.out.println(Arrays.toString(bytes));
        while (true) {

        }
    }
}
