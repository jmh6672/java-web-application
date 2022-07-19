package com.haram.response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class ResponseTest {
    SimpleHttpResponse httpResponse;

    ServerSocket server;

    Socket connection;

    @Before
    public void setUp() throws IOException {
        int port = ThreadLocalRandom.current().nextInt(10000, 20000);

        server = new ServerSocket(port);
        connection = new Socket("localhost", server.getLocalPort());
    }

    @After
    public void after() throws IOException {
        server.close();
        connection.close();
    }


    @Test
    public void setResponseTest() throws IOException {
        httpResponse = new SimpleHttpResponse(new BufferedOutputStream(connection.getOutputStream()), "UTF-8","HTTP/1.1");

        httpResponse.setBody(responseSource());
        assertEquals(200,httpResponse.getStatus());
        assertEquals("HTTP/1.1",httpResponse.getVersion());

        httpResponse.flush();
    }

    public String responseSource(){
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html;charset=utf-8\n";
    }
}
