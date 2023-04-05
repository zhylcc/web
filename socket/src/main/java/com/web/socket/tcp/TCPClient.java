package com.web.socket.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws UnknownHostException {
        TCPClient tcpClient = new TCPClient();
        tcpClient.send(InetAddress.getLocalHost().getHostAddress(), 8000);
    }

    public void send(String ip, int port) {
        Scanner in = new Scanner(System.in);
        //1. 创建socket，绑定目的地址和目的端口（需要服务端先启动监听）
        //2. 获取输出流
        try (Socket socket = new Socket(ip, port);
             OutputStream outputStream = socket.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            System.out.println("Accept on " + InetAddress.getLocalHost().getHostAddress());
            while (true) {
                //3. 通过控制台输入阻塞地获取数据
                String line = in.nextLine();
                //4. 通过输出流写数据
                writer.write(line);
                writer.flush();
//                socket.shutdownOutput();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4. 关闭流和socket
    }
}
