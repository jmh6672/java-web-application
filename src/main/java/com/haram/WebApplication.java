package com.haram;

import com.haram.singleton.Environment;
import com.haram.webserver.WebProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebApplication {
    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int DEFAULT_PORT = 8080;



    public static void main(String[] args) {
        Environment environment = Environment.getInstance();

        int port = Integer.parseInt(environment.getOrDefault("server.port",DEFAULT_PORT).toString());
        if (port < 0 || port > 65535) {
            port = DEFAULT_PORT;
        }
        String encoding = environment.getOrDefault("server.index",DEFAULT_ENCODING).toString();

        try {
            WebApplication webApplication = new WebApplication();
            webApplication.start(port, encoding);
        } catch (IOException exception){
            logger.error("Server could not start", exception);
        }
    }

    public void start(int port, String encoding) throws IOException{
        try (ServerSocket server = new ServerSocket(port)){
            logger.info("Web Application Server started. port: {}",server.getLocalPort());

            Socket connection;
            while (true){
                try {
                    connection = server.accept();
                    WebProcessor request = new WebProcessor(encoding,connection);
                    request.start();
                } catch (IOException ex) {
                    logger.warn("Error accepting connection", ex);
                }
            }
        }
    }
}
