package com.haram.singleton;

import com.haram.globals.Const;
import com.haram.request.HttpRequest;
import com.haram.request.SimpleHttpRequest;
import com.haram.response.HttpResponse;
import com.haram.response.SimpleHttpResponse;
import com.haram.webserver.Host;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class FilterTest {
    private final static Logger logger = LoggerFactory.getLogger(FilterTest.class);
    private Filter filter = Filter.getInstance();
    private HostMapper hostMapper = HostMapper.getInstance();
    private static final String DEFAULT_ROOT = "/";

    ServerSocket server;
    Socket connection;


    @Before
    public void setUp() throws IOException {
        int port = ThreadLocalRandom.current().nextInt(10000, 20000);

        server = new ServerSocket(port);
        connection = new Socket("localhost", server.getLocalPort());
    }

    @Test
    @Parameters(method = "hostMappingParams")
    public void hostMappingTest(String requestSource, int status, String expect) throws IOException {
        SimpleHttpRequest request = new SimpleHttpRequest(new ByteArrayInputStream(requestSource.getBytes()),"UTF-8");
        SimpleHttpResponse response = new SimpleHttpResponse(new BufferedOutputStream(connection.getOutputStream()), "UTF-8","HTTP/1.1");
        response.setStatus(status);

        String servletPath = hostServlet(request,response);

        assertEquals(expect,servletPath);
    }
    private String hostServlet(HttpRequest request, HttpResponse response){
        String servletPath = null;
        String rootPath = "";

        Host host = hostMapper.getMapper().get(request.getHost());
        if(host == null) {
            //host 정보가 없을 경우 잘못된 응답
            response.setStatus(400);
        }else{
            //루트 경로 지정
            rootPath = (host.getRoot() == null || host.getRoot().isEmpty()) ? DEFAULT_ROOT : host.getRoot();

            //오류 상태의 경우
            if(response.getStatus() >= 300){
                servletPath = host.getErrorServletMap().get(response.getStatus());
            }else {
                //URI에 매핑된 설정 정보가 없을 경우 URI 로 servlet 호출
                String requestURI = request.getRequestURI().equals(DEFAULT_ROOT) ? rootPath : request.getRequestURI();
                servletPath = host.getServletMap().get(requestURI);
                if(servletPath == null || servletPath.equals("")){
                    servletPath = requestURI;
                }
            }
        }

        return servletPath;
    }

    public Object[][] hostMappingParams() {
        return new Object[][] {
                new Object[] {
                        "GET /test HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        200
                        ,
                        "/test"},
                new Object[] {
                        "GET /Atime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        200
                        ,
                        "/time"},
                new Object[] {
                        "GET /Btime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        200
                        ,
                        "/time"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        403
                        ,
                        "/403"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        403
                        ,
                        "/A403"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        403
                        ,
                        "/B403"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        404
                        ,
                        "/404"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        404
                        ,
                        "/A404"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        404
                        ,
                        "/B404"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        500
                        ,
                        "/500"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        500
                        ,
                        "/A500"},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        500
                        ,
                        "/B500"}
        };
    }


    @Test
    @Parameters(method = "filterParams")
    public void doFilterTest(String requestSource, int expect) throws IOException {
        SimpleHttpResponse response = new SimpleHttpResponse(new BufferedOutputStream(connection.getOutputStream()), "UTF-8","HTTP/1.1");
        SimpleHttpRequest request = new SimpleHttpRequest(new ByteArrayInputStream(requestSource.getBytes()),"UTF-8");
        filter.doFilter(request,response);
        assertEquals(expect, response.getStatus());
    }


    public Object[][] filterParams() {
        return new Object[][] {
                new Object[] {
                        "GET /Error HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        500},
                new Object[] {
                        "GET /failed HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        404},
                new Object[] {
                        "GET /failed HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        404},
                new Object[] {
                        "GET /failed HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        404},
                new Object[] {
                        "GET / HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        200},
                new Object[] {
                        "GET /Atime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        200},
                new Object[] {
                        "GET /Btime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        200}
        };
    }
}
