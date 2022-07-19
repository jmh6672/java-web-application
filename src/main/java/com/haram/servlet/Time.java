package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

import java.io.IOException;
import java.util.Date;

@ServletMapping(value = "/time")
public class Time implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        StringBuilder body = new StringBuilder();
        body.append("<html><body>");
        body.append("<h1>Current Time</h1>");
        body.append("</body>"+new Date()+"</html>");

        response.setBody(body.toString());
        response.flush();
    }
}