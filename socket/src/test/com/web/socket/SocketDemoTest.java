package com.web.socket;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class SocketDemoTest {

    @Test
    void inetAddress() throws UnknownHostException {
        SocketDemo socketDemo = new SocketDemo();
        socketDemo.inetAddress();
    }
}