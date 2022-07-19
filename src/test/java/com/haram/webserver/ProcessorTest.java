package com.haram.webserver;

import com.haram.globals.Const;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class ProcessorTest {
    private final static Logger logger = LoggerFactory.getLogger(ProcessorTest.class);
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
    @Parameters(method = "runParams")
    public void runTest(String requestSource, String expect) throws IOException {

        WebProcessor processor = new WebProcessor(null, server.accept());

        BufferedOutputStream outStream = new BufferedOutputStream(connection.getOutputStream());
        outStream.write(requestSource.getBytes(StandardCharsets.UTF_8));
        outStream.flush();

        processor.run();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line = br.readLine();
        logger.debug(line);

        assertTrue(expect.startsWith(line));
    }


    public Object[][] runParams() {
        return new Object[][] {
                new Object[] {
                        "GET /time HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        message200()},
                new Object[] {
                        "GET /Atime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        message200()},
                new Object[] {
                        "GET /Btime HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        message200()},
                new Object[] {
                        "GET /time.exe HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        message403()},
                new Object[] {
                        "GET /notFound HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        message404()},
                new Object[] {
                        "GET /a HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        message404()},
                new Object[] {
                        "GET /b HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        message404()},
                new Object[] {
                        "GET /500 HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        message500()},
                new Object[] {
                        "GET /500 HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: a.com:8080\n"+ Const.CRLF
                        ,
                        message500()},
                new Object[] {
                        "GET /500 HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: b.com:8080\n"+ Const.CRLF
                        ,
                        message500()},
                new Object[] {
                        "GET /Error HTTP/1.1\n" +
                        "Accept: */*\n" +
                        "Connection: keep-alive\n" +
                        "Host: localhost:8080\n"+ Const.CRLF
                        ,
                        message500()}
        };
    }


    public String message200(){
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html;charset=utf-8\n";
    }

    public String message403(){
        return "HTTP/1.1 403 Forbidden Error\n";
    }

    public String message404(){
        return "HTTP/1.1 404 Not Found Error\n";
    }

    public String message500(){
        return "HTTP/1.1 500 Internal Server Error\n";
    }
}
