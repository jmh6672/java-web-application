package com.haram.singleton;

import com.haram.request.SimpleHttpRequest;
import com.haram.response.SimpleHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class SecureFilterTest {
    private final static Logger logger = LoggerFactory.getLogger(SecureFilterTest.class);
    SecureFilter filter = SecureFilter.getInstance();
    ServerSocket server;
    Socket connection;


    @Before
    public void setUp() throws IOException {
        int port = ThreadLocalRandom.current().nextInt(10000, 20000);

        server = new ServerSocket(port);
        connection = new Socket("localhost", server.getLocalPort());
    }

    @Test
    public void securityCheckTest() throws IOException {
        SimpleHttpResponse response = new SimpleHttpResponse(new BufferedOutputStream(connection.getOutputStream()), "UTF-8","HTTP/1.1");

        SimpleHttpRequest request = new SimpleHttpRequest(new ByteArrayInputStream(requestSource().getBytes()),"UTF-8");
        filter.doFilter(request,response);
        assertEquals(200,response.getStatus());

        request = new SimpleHttpRequest(new ByteArrayInputStream(parentRequestSource().getBytes()),"UTF-8");
        filter.doFilter(request,response);
        assertEquals(403,response.getStatus());

        request = new SimpleHttpRequest(new ByteArrayInputStream(exeRequestSource().getBytes()),"UTF-8");
        filter.doFilter(request,response);
        assertEquals(403,response.getStatus());

    }

    public String requestSource() {
        return "GET /test HTTP/1.1\n" +
                "Accept: */*\n" +
                "Connection: keep-alive\n" +
                "Host: localhost:8080\n";
    }

    public String parentRequestSource(){
        return "GET ../../ HTTP/1.1\n" +
                "Accept: */*\n" +
                "Connection: keep-alive\n" +
                "Host: localhost:8080\n";
    }

    public String exeRequestSource(){
        return "GET /test.exe HTTP/1.1\n" +
                "Accept: */*\n" +
                "Connection: keep-alive\n" +
                "Host: localhost:8080\n";
    }

    public String responseSource(){
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html;charset=utf-8\n";
    }
}
