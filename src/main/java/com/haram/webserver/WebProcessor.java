package com.haram.webserver;

import com.haram.singleton.Filter;
import com.haram.request.SimpleHttpRequest;
import com.haram.response.SimpleHttpResponse;
import com.haram.singleton.SecureFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class WebProcessor extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(WebProcessor.class);

    private SecureFilter secureFilter = SecureFilter.getInstance();
    private Filter filter = Filter.getInstance();
    private String encoding = "UTF-8";
    private Socket connection;


    public WebProcessor(String encoding, Socket connection) {
        if (encoding != null)
            this.encoding = encoding;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            logger.debug(connection.getRemoteSocketAddress().toString());

            SimpleHttpRequest request = new SimpleHttpRequest(connection.getInputStream(), encoding);
            SimpleHttpResponse response = new SimpleHttpResponse(connection.getOutputStream(), encoding, request.getVersion());

            logger.info("[Request]: " + request.getMethod() + " " + request.getRequestURL());

            secureFilter.doFilter(request,response);
            filter.doFilter(request,response);

            logger.info("[Response]: " + response.getStatus() + " " + response.getContentType());

        } catch (IOException ex) {
            logger.warn("Error talking to " + connection.getRemoteSocketAddress(), ex);
        } finally {
            try {
                connection.close();
                logger.debug("connection closed");
            } catch (IOException ex) {
                logger.warn("Failed to connection close",ex);
            }
        }
    }
}