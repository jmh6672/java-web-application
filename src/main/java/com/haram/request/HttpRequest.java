package com.haram.request;

import java.io.InputStream;
import java.util.Map;

public interface HttpRequest {
    String getCharacterEncoding();
    String getMethod();
    String getHost();
    int getPort();
    String getContextPath();
    String getQueryString();
    String getRequestURI();
    String getRequestURL();

    InputStream getInputStream();

    Map<String,String> getParameterMap();
}
