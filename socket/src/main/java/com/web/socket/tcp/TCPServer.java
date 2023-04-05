package com.web.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer {

    public static void main(String[] args) throws UnknownHostException {
        TCPServer tcpServer = new TCPServer();
        tcpServer.receive(8000);
    }
    public void receive(int port) throws UnknownHostException {
        System.out.println("Listen on "+ InetAddress.getLocalHost().getHostAddress()+":"+port);
        //1. 创建服务端serverSocket，绑定端口和本机ip
        //2. accept阻塞地监听客户端连接，并返回socket
        //3. 获取输入流
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            System.out.println("Connected: ["+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]");
            //4. 通过输入流阻塞地接收数据
            int len;
            char[] buffer = new char[1024];
            while ((len=reader.read(buffer, 0, buffer.length)) != -1) {
                System.out.println("Receive: "+new String(buffer, 0, len)+
                        " ["+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //5. 关闭流和socket
    }
}
