package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

import java.io.IOException;

@ServletMapping(value = "/A500")
public class AError500Servlet implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(500);

        StringBuilder body = new StringBuilder();
        body.append("<html><body>");
        body.append("<h1>A500 Error</h1>");
        body.append("</body></html>");

        response.setBody(body.toString());
        response.flush();
    }
}