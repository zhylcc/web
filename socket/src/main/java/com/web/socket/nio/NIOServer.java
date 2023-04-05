package com.web.socket.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws UnknownHostException {
        NIOServer nioServer = new NIOServer();
        Map<String,String> param = new HashMap<>();
        for (String arg: args) {
            String[] kv = arg.split("=");
            param.put(kv[0], kv[1]);
        }
        int port = Integer.parseInt(param.getOrDefault("port", "8000"));
        System.out.println("Listen on "+ InetAddress.getLocalHost().getHostAddress()+":"+port);
        if (param.containsKey("mode") && param.get("mode").equals("selector")) {
            nioServer.serveWithSelector(port);
        }
        else {
            nioServer.serve(port);
        }
    }

    public void serve(int port) {
        //1. 打开服务端通道
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            //2. 绑定本机ip（默认）和端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //3. 设置监听通道为非阻塞（默认阻塞）
            serverSocketChannel.configureBlocking(false);
            while (true) {
                //4. 非阻塞监听客户端连接（该实现方式下，只能监听一个客户端连接），并返回客户端通道
                try (SocketChannel channel = serverSocketChannel.accept()) {
                    if (channel == null) continue;
                    System.out.println("Connected: ["+channel.getRemoteAddress()+"]");
                    //5. 设置客户端连接通道为非阻塞
                    channel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //6. 从通道中读数据到缓冲区
                    int len;
                    while ((len=channel.read(buffer)) != -1) {
                        if (len == 0) continue;
                        System.out.println("Receive: "+new String(buffer.array(), 0, len)+
                                " ["+channel.getRemoteAddress()+"]");
                        buffer.clear();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void serveWithSelector(int port) {
        //1. 打开服务端通道
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            //2. 绑定本机ip（默认）和端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //3. 设置监听通道为非阻塞（默认阻塞）
            serverSocketChannel.configureBlocking(false);
            //4. 绑定选择器和通道，监听ACCEPT事件
            try (Selector selector = Selector.open()) {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                while (true) {
                    if (selector.select() == 0) continue;
                    //5. 遍历事件监听key，处理每个客户端连接事件或读事件
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        //5-1. 客户端连接事件，设置连接通道为非阻塞并监听READ事件
                        if (key.isAcceptable()) {
                            SocketChannel channel = serverSocketChannel.accept();
                            System.out.println("Connected: ["+channel.getRemoteAddress()+"]");
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        }
                        //5-2. 可读事件，从通道中读取数据到缓冲区
                        else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len;
                            while ((len=channel.read(buffer)) > 0) {
                                System.out.println("Receive: "+new String(buffer.array(), 0, len)+
                                        " ["+channel.getRemoteAddress()+"]");
                                buffer.clear();
                            }
                        }
                        //6. 事件处理完成后从列表中删除监听key
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
