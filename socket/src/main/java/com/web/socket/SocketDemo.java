package com.web.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketDemo {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        String host = localHost.getHostName();
        String ip = localHost.getHostAddress();
        System.out.println("主机名："+host);
        System.out.println("ip："+ip);
    }
}
