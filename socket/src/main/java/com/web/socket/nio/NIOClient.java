package com.web.socket.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {
    public static void main(String[] args) throws UnknownHostException {
        Scanner in = new Scanner(System.in);
        //1. 打开客户端通道（默认阻塞）
        System.out.println("Accept on " + InetAddress.getLocalHost().getHostAddress());
        try (SocketChannel channel = SocketChannel.open()) {
            //2. 绑定目的地址和端口
            channel.connect(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 8000));
            while (true) {
                //3. 向缓冲区写入数据，并传输到通道
                String line = in.nextLine();
                ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4. 关闭通道
    }
}
