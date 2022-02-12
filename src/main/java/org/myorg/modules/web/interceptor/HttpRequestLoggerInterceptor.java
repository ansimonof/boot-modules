package org.myorg.modules.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestLoggerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("Http request: {}", request.getRequestURI());
        return true;
    }
}
