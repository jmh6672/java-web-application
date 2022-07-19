package com.haram.request;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RequestTest {
    SimpleHttpRequest httpRequest;


    @Test
    public void setRequestTest() throws IOException {
        httpRequest = new SimpleHttpRequest(new ByteArrayInputStream(requestSource().getBytes()),"UTF-8");

        assertEquals("GET",httpRequest.getMethod());
        assertEquals("HTTP/1.1",httpRequest.getVersion());
        assertEquals("/test",httpRequest.getContextPath());
        assertEquals("localhost:8080/test/index.html",httpRequest.getRequestURL());
        assertEquals("/test/index.html",httpRequest.getRequestURI());
        assertEquals("",httpRequest.getQueryString());
    }

    public String requestSource(){
        return "GET /test/index.html HTTP/1.1\n" +
                "Accept: */*\n" +
                "Connection: keep-alive\n" +
                "Host: localhost:8080\n";
    }
}
