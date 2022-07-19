package com.haram.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface HttpResponse {

    String getEncoding();
    void setEncoding(String encoding);

    String getContentType();
    void setContentType(String contentType);

    int getStatus();
    void setStatus(int status);

    String getBody();
    void setBody(String body);

    OutputStream getOutputStream() throws IOException;
    Writer getWriter() throws IOException;

    void flush() throws IOException;
}
