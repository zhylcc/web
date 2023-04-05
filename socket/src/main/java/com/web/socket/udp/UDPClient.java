package com.web.socket.udp;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UDPClient {


    public static void main(String[] args) throws UnknownHostException {
        UDPClient client = new UDPClient();
        Map<String,String> param = new HashMap<>();
        for (String arg: args) {
            String[] kv = arg.split("=");
            param.put(kv[0], kv[1]);
        }
        InetAddress address = null;
        if (param.containsKey("address")) {
            address = InetAddress.getByName(param.get("address"));
        }
        else {
            address = InetAddress.getLocalHost();
        }
        int port = Integer.parseInt(param.getOrDefault("port", "8000"));
        client.send(address, port);
    }

    /**
     * 发送数据
     * @param address：目的地址
     * @param port：目的端口
     */
    public void send(InetAddress address, int port) {
        //1. 创建发送端socket
        try (DatagramSocket socket = new DatagramSocket();
             Scanner in = new Scanner(System.in)) {
            System.out.println("Accept on "+InetAddress.getLocalHost().getHostAddress());
            //2. 创建数据包
            while (true) {
                String input=in.nextLine();
                byte[] message = input.getBytes();
                DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
                //3. 发送数据包
                socket.send(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4. 关闭socket（由try-with语法糖完成）
    }
}
