package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

import java.io.IOException;

public interface SimpleServlet {
    public void service(HttpRequest request, HttpResponse response) throws Exception;
}
