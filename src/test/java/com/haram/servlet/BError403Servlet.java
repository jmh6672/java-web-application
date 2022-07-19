package com.haram.servlet;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

import java.io.IOException;

@ServletMapping(value = "/B403")
public class BError403Servlet implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(403);

        StringBuilder body = new StringBuilder();
        body.append("<html><body>");
        body.append("<h1>B403 Error</h1>");
        body.append("</body></html>");

        response.setBody(body.toString());
        response.flush();
    }
}