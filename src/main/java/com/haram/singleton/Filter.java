package com.haram.singleton;

import com.haram.filter.WebFilter;
import com.haram.request.HttpRequest;
import com.haram.response.HttpResponse;
import com.haram.webserver.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Filter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(Filter.class);
    private ServletMapper servletMapper = ServletMapper.getInstance();
    private HostMapper hostMapper = HostMapper.getInstance();

    private static final String DEFAULT_ROOT = "/";

    public Filter(){
    }

    private static class SingletonClass {
        private static final Filter instance = new Filter();
    }

    public static Filter getInstance(){
        return Filter.SingletonClass.instance;
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response) {
        String servletPath = hostServlet(request, response);

        try {
            if(servletMapper.getMapper().get(servletPath)==null){
                if(response.getStatus() == 404){
                    logger.error("Filed to load servlet.");
                    return;
                }
                response.setStatus(404);
                doFilter(request,response);
            }else {
                servletMapper.getMapper().get(servletPath).service(request, response);
            }
        } catch (Throwable throwable) {
            logger.error("Internal Server Error",throwable);
            response.setStatus(500);
            doFilter(request,response);
        }

    }

    private String hostServlet(HttpRequest request, HttpResponse response){
        String servletPath = null;
        String rootPath = null;

        Host host = hostMapper.getMapper().get(request.getHost());
        if(host == null) {
            //host 정보가 없을 경우 잘못된 응답
            response.setStatus(400);
        }else{
            //루트 경로 지정
            rootPath = (host.getRoot() == null || host.getRoot().isEmpty()) ? DEFAULT_ROOT : host.getRoot();

            //오류 상태의 경우
            if(response.getStatus() >= 300){
                servletPath = host.getErrorServletMap().get(response.getStatus());
            }else {
                String requestURI = request.getRequestURI().equals(DEFAULT_ROOT) ? rootPath : request.getRequestURI();
                //URI에 매핑된 설정 정보가 없을 경우 URI 로 servlet 호출
                servletPath = host.getServletMap().get(requestURI);
                if(servletPath == null || servletPath.equals("")){
                    servletPath = requestURI;
                }
            }
        }

        return servletPath;
    }
}
