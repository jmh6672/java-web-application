package com.haram.response;

import com.haram.globals.Const;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

public class SimpleHttpResponse implements HttpResponse {

    private String encoding;
    private String contentType = "text/html";
    private int status = 200;
    private String body;
    private OutputStream outputStream;

    private String version;

    public SimpleHttpResponse(OutputStream outputStream, String encoding, String version){
        this.outputStream = outputStream;
        this.version = version;
        setEncoding(encoding);
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(this.outputStream);
    }

    @Override
    public void flush() throws IOException {
        Writer out = getWriter();

        if(version.startsWith("HTTP/")) {
            sendHeader(out);
        }
        out.write(this.body!=null ? body : "");
        out.flush();
    }

    public String getVersion(){
        return this.version;
    }

    void sendHeader(Writer out) throws IOException {
        out.write(this.version+" "+this.status + Const.CRLF);
        Date now = new Date();
        out.write("Date: " + now + Const.CRLF);
        out.write("Content-length: " + (this.body!=null ? body.length() : 0) + Const.CRLF);
        out.write("Content-type: " + this.contentType + Const.CRLF);
        out.write(Const.CRLF);
        out.flush();
    }

}
