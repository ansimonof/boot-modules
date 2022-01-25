package org.myorg.modules.web.security.filter;

import org.myorg.modules.web.security.authentication.token.CustomAbstractAuthenticationToken;
import org.myorg.modules.web.security.authentication.token.CustomAnonymousAuthenticationToken;
import org.myorg.modules.web.security.authentication.resolver.AuthenticationTokenResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final List<AuthenticationTokenResolver> authenticationTokenResolvers;

    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public AuthenticationTokenFilter(List<AuthenticationTokenResolver> authenticationTokenResolvers, RequestMappingHandlerMapping handlerMapping) {
        this.authenticationTokenResolvers = authenticationTokenResolvers;
        this.handlerMapping = handlerMapping;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuthenticationTokenResolver foundedResolver = null;
        for (AuthenticationTokenResolver resolver : authenticationTokenResolvers) {
            if (resolver.supports(request)) {
                if (foundedResolver == null) {
                    foundedResolver = resolver;
                } else {
                    throw new AuthenticationServiceException(
                            "More than one way to authenticate:"
                                    + " (first: " + foundedResolver.getName()
                                    + ", second: " + resolver.getName() + ")");
                }
            }
        }

        CustomAbstractAuthenticationToken authentication;
        if (foundedResolver != null) {
            authentication = foundedResolver.createToken(request);
        } else {
            authentication = new CustomAnonymousAuthenticationToken();
        }

        try {
            Method method = ((HandlerMethod) handlerMapping.getHandler(request).getHandler()).getMethod();
            authentication.setDetails(method);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unknown mapping", e);
        }

        filterChain.doFilter(request, response);
    }
}
