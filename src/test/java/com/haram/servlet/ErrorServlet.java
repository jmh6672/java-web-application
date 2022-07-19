package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

@ServletMapping(value = "/Error")
public class ErrorServlet implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        throw new Exception("Failed Test");
    }
}