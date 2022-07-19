package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

import java.io.IOException;

@ServletMapping(value = "/B404")
public class BError404Servlet implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(404);

        StringBuilder body = new StringBuilder();
        body.append("<html><body>");
        body.append("<h1>B404 Error</h1>");
        body.append("</body></html>");

        response.setBody(body.toString());
        response.flush();
    }
}