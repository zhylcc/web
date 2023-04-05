package com.web.socket.udp;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {

    public static void main(String[] args) throws UnknownHostException {
        UDPServer server = new UDPServer();
        Map<String,String> param = new HashMap<>();
        for (String arg: args) {
            String[] kv = arg.split("=");
            param.put(kv[0], kv[1]);
        }
        String mode = param.getOrDefault("mode", "normal");
        int port = Integer.parseInt(param.getOrDefault("port", "8000"));
        if (mode.equals("multicast")) {
            InetAddress address = null;
            address = InetAddress.getByName(param.getOrDefault("address", "224.0.1.0"));
            server.receiveMulticast(address, port);
        }
        else {
            server.receive(port);
        }
    }

    /**
     * 单播或广播方式接收
     * @param port：监听端口
     */
    public void receive(int port) {
        //1. 创建接收端socket，指定端口
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Listen on "+InetAddress.getLocalHost().getHostAddress()+":"+port);
            //2. 创建数据包等待接收
            while (true) {
                byte[] bytes = new byte[1024];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                //3. 接收数据
                socket.receive(packet);
                System.out.println("Receive: "+new String(packet.getData(), 0, packet.getLength())+
                        " [N:"+packet.getAddress().getHostAddress()+":"+packet.getPort()+"]");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4. 关闭socket
    }

    /**
     * 组播方式接收
     * @param groupAddr：绑定组播地址
     * @param port：监听端口
     */
    public void receiveMulticast(InetAddress groupAddr, int port) {
        //1. 创建接收端socket，指定端口
        try (MulticastSocket socket = new MulticastSocket(port)) {
            System.out.println("Listen on "+InetAddress.getLocalHost().getHostAddress()+":"+port);
            //2. 绑定组播地址
            socket.joinGroup(groupAddr);
            //3. 创建数据包等待接收
            while (true) {
                byte[] bytes = new byte[1024];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                //3. 接收数据
                socket.receive(packet);
                System.out.println("Receive: "+new String(packet.getData(), 0, packet.getLength())+
                        " [M:"+packet.getAddress().getHostAddress()+":"+packet.getPort()+"]");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4. 关闭socket
    }
}
