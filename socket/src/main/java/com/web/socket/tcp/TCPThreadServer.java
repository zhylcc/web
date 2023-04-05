package com.web.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPThreadServer {

    public static void main(String[] args) throws UnknownHostException {
        int port = 8000;
        System.out.println("Listen on "+ InetAddress.getLocalHost().getHostAddress()+":"+port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            int cnt = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                cnt += 1;
                int finalCnt = cnt;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (InputStream inputStream = socket.getInputStream();
                             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            System.out.println("Connected["+finalCnt+"]: ["+
                                    socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]");
                            String line;
                            while ((line=reader.readLine()) != null) {
                                System.out.println("Receive["+ finalCnt +"]: "+line+
                                        " ["+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]");
                            }
                        } catch (IOException e) {
                            System.out.println("Disconnected["+finalCnt+"]: ["+
                                    socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]");
                            throw new RuntimeException(e);
                        }
                    }
                }, cnt+"").start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
