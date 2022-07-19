package com.haram.filter;

import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;

public interface WebFilter {
    void doFilter(HttpRequest request, HttpResponse response);
}
