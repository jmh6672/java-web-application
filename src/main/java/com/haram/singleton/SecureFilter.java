package com.haram.singleton;

import com.haram.filter.WebFilter;
import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecureFilter.class);

    public SecureFilter(){ }

    private static class SingletonClass {
        private static final SecureFilter instance = new SecureFilter();
    }

    public static SecureFilter getInstance(){
        return SecureFilter.SingletonClass.instance;
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response) {
        logger.debug("contextPath : "+ request.getContextPath());
        logger.debug("requestURL : "+ request.getRequestURL());
        // for security checks
        if(request.getContextPath().startsWith("..") || request.getRequestURL().endsWith(".exe")){
            response.setStatus(403);
        }
    }
}
