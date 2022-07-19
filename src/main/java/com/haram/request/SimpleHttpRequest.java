package com.haram.request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.haram.globals.Const.BLANK;

public class SimpleHttpRequest implements HttpRequest {
    private String encoding;
    private String method;
    private String host;
    private int port;
    private String uri;
    private String version;
    private String queryString;

    private InputStream inputStream;

    public SimpleHttpRequest(InputStream inputStream, String encoding) throws IOException {
        this.encoding = encoding;
        this.inputStream = inputStream;
        requestMapping(inputStream);
    }

    private void requestMapping(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), this.encoding));

        String get = br.readLine();
        if(get == null) {
            return;
        }

        String[] tokens = get.split(BLANK);
        this.method = tokens[0].toUpperCase();
        if(tokens[1].indexOf("?") > -1){
            String[] uri = tokens[1].split("\\?");
            this.uri = uri[0];
            this.queryString = uri[1];
        }else{
            this.uri = tokens[1];
            this.queryString = "";
        }
        if (tokens.length > 2) {
            this.version = tokens[2];
        }

        while (br.ready()) {
            String readLine = br.readLine();
            if(readLine.startsWith("Host")){
                tokens = readLine.split(BLANK);
                String[] split = tokens[1].split(":");
                this.host = split[0];
                this.port = Integer.parseInt(split[1]);
            }
        }
    }


    @Override
    public String getCharacterEncoding() {
        return this.encoding;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getContextPath() {
        if(this.uri.split("/").length == 0){
            return this.uri;
        }else{
            return this.uri.substring(0,uri.lastIndexOf("/"));
        }
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public String getRequestURI() {
        return this.uri;
    }

    @Override
    public String getRequestURL() {
        return new StringBuffer().append(this.host).append(":").append(this.port).append(this.uri).toString();
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public Map<String, String> getParameterMap() {
        String[] params = this.queryString.split("&");
        Map<String, String> map = new HashMap<>();
        for(String param : params){
            String[] split = param.split("=");
            map.put(split[0],split[1]);
        }

        return map;
    }

    public String getVersion() {
        return this.version;
    }
}
